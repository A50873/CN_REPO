package grpcclientapp;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import servicestubs.IntervalNumbers;
import servicestubs.NumbersServiceGrpc;

import java.util.concurrent.CountDownLatch;

public class FindPrimesClient {
    private static String svcIP = "localhost";
    private static int svcPort = 8000;
    private static NumbersServiceGrpc.NumbersServiceStub noBlockStub;

    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                svcIP = args[0];
                svcPort = Integer.parseInt(args[1]);
            }

            System.out.println("connect to " + svcIP + ":" + svcPort);
            ManagedChannel channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                    .usePlaintext()
                    .build();

            noBlockStub = NumbersServiceGrpc.newStub(channel);
            findPrimes();

            channel.shutdown();
        } catch (Exception ex) {
            System.out.println("Unhandled exception");
            ex.printStackTrace();
        }
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
}

