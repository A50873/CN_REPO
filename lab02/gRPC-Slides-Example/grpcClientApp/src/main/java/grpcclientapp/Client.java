package grpcclientapp;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import servicestubs.*;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Client {
    // generic ClientApp for Calling a grpc Service
    private static String svcIP = "localhost";
    private static int svcPort = 8000;
    private static NumbersServiceGrpc.NumbersServiceBlockingStub blockingStub;
    private static NumbersServiceGrpc.NumbersServiceStub noBlockStub;

    static void main(String[] args) {
        try {
            if (args.length == 2) {
                svcIP = args[0]; svcPort = Integer.parseInt(args[1]);
            }
            System.out.println("connect to " + svcIP + ":" + svcPort);
            // Channels are secure by default (via SSL/TLS).
            // For the example we disable TLS to avoid
            // needing certificates.
            ManagedChannel channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                    // Channels are secure by default (via SSL/TLS).
                    // For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();
            blockingStub = NumbersServiceGrpc.newBlockingStub(channel);
            noBlockStub = NumbersServiceGrpc.newStub(channel);
            // Call service operations for example ping server
            while (true) {
                try {
                    int option = Menu();
                    switch (option) {
                        case 1:
                            isAliveCall();  break;
                        case 2:
                            getNevenNumbersSynchronousCall(); break;
                        case 3:
                            getNevenNumbersAsynchronousCall(); break;
                        case 4:
                            addSequenceOfNumbersCall(); break;
                        case 5:
                            bidirectionalStreamingCall(); break;
                        case 6:
                            findPrimes(); break;
                        case 99:  System.exit(0);
                    }
                } catch (Exception ex) {
                    System.out.println("Execution call Error  !");
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.out.println("Unhandled exception");
            ex.printStackTrace();
        }
    }

    static void isAliveCall() {
        //ping server
        TextMessage reply = blockingStub.isAlive(ProtoVoid.newBuilder().build());
        System.out.println("Ping server:" + reply.getTxt());
    }

    static void getNevenNumbersSynchronousCall() { // get N even numbers
        //Synchronous blocking call
        try {
            int N = Integer.parseInt(read("How many even numbers?", new Scanner(System.in)));
            IntNumber intNumber=IntNumber.newBuilder().setIntnumber(N).build();
            Iterator<IntNumber> iterator = blockingStub.getEvenNumbers(intNumber);
            while (iterator.hasNext()) {
                System.out.println("Another even number: " + iterator.next().getIntnumber());
            }
        } catch(Exception ex) {
            System.out.println("Synchronous call error: "+ex.getMessage());
        }
    }

    static void getNevenNumbersAsynchronousCall() throws InterruptedException { // get N even numbers
        // Asynchronous non-blocking call
        int N = Integer.parseInt(read("How many even numbers?", new Scanner(System.in)));
        EvenNumbersStream evenStream = new EvenNumbersStream();
        noBlockStub.getEvenNumbers(IntNumber.newBuilder().setIntnumber(N).build(), evenStream);
        while (!evenStream.isCompleted()) {
            System.out.println("Continue working until receive all even numbers");
            Thread.sleep(1000); // Simulate processing time (1 seg)
        }
    }

    static void addSequenceOfNumbersCall() {
        // Add sequence of numbers
        int N = Integer.parseInt(read("Enter N?", new Scanner(System.in)));
        StreamObserver<IntNumber> streamNumbers = noBlockStub.addSeqOfNumbers(new StreamObserver<>() {
            @Override
            public void onNext(IntNumber intNumber) {
                System.out.println("Add total: " + intNumber.getIntnumber());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Add sequence completed");
            }
        });
        for (int i = 1; i <= N; i++) { // send N numbers
            streamNumbers.onNext(IntNumber.newBuilder().setIntnumber(i).build());
            System.out.println("sent number "+i);
        }
        streamNumbers.onCompleted();
        // Note that client has sent all requests, but needs synchronization
        // to terminate after get the final result
    }

    static void bidirectionalStreamingCall() {
        // bidirectional streaming - Do multiple add operations
        StreamObserver<AddOperands> streamDoAddOperations = noBlockStub.multipleAdd(new StreamObserver<>() {
            @Override
            public void onNext(AddResult addResult) {
                System.out.println("Add Result ID(" + addResult.getAddID() + ")=" + addResult.getResult());
            }
            @Override
            public void onError(Throwable throwable) {

                System.out.println("onError:"+throwable.getMessage());
            }
            @Override
            public void onCompleted() {
                System.out.println("Add operations requests completed");
            }
        });
        // Do a sequence of 20 add operations
        for (int i = 0; i < 20; i++) {
            int x = new Random().nextInt(10) + 1;
            int y = new Random().nextInt(10) + 1;
            System.out.println("Call to operation:(" + "ID" + i + "," + x + "," + y + ")");
            AddOperands oper = AddOperands.newBuilder()
                    .setAddID("ID" + i).setOp1(x).setOp2(y)
                    .build();
            streamDoAddOperations.onNext(oper);
        }
        streamDoAddOperations.onCompleted();
        // Note that client has sent all requests, but needs synchronization
        // to terminate after get all results
    }

    static void findPrimes() throws InterruptedException {
        // Asynchronous non-blocking call
        int chunkSize = 100;
        int totalEnd = 500;
        int calls = totalEnd / chunkSize; // 5

        CountDownLatch done = new CountDownLatch(calls);

        for (int i = 0; i < calls; i++) {
            int start = i * chunkSize + 1;
            int end = (i + 1) * chunkSize;

            PrimeNumbersStream primeStream = new PrimeNumbersStream(done);

            IntervalNumbers req = IntervalNumbers.newBuilder()
                    .setStart(start)
                    .setEnd(end)
                    .build();

            noBlockStub.findPrimes(req, primeStream);
        }

        // wait for all streams
        done.await();
        System.out.println("Todas as 5 chamadas findPrimes terminaram.");
    }


    private static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Case Unary call: server isAlive");
            System.out.println(" 2 - Case server stream: get N even numbers: Synchronous call");
            System.out.println(" 3 - Case server stream: get N even numbers: Asynchronous call");
            System.out.println(" 4 - Case client stream: add sequence of numbers between 1 and N");
            System.out.println(" 5 - Case bidirectional streaming (client and server): multiple add operations)");
            System.out.println(" 6 - Case server stream: get prime numbers between 1 and 500: Asynchronous call");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 6) || op == 99));
        return op;
    }

    private static String read(String msg, Scanner input) {
        System.out.println(msg);
        return input.nextLine();
    }
}
