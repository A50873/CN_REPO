package grpcclientapp;

import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import forum.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Client {
    // generic ClientApp for Calling a grpc Service
    //private static String svcIP = "34.175.114.71";
    private static String svcIP = "localhost";
    private static int svcPort = 8000;
    private static ForumGrpc.ForumBlockingStub blockingStub;
    private static ForumGrpc.ForumStub noBlockStub;
    private static String userName;
    private static final Storage storage = null;

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
            blockingStub = ForumGrpc.newBlockingStub(channel);
            noBlockStub = ForumGrpc.newStub(channel);
            // Call service operations for example ping server
            userName = read("Insira o seu userName", new Scanner(System.in));
            while (true) {
                try {
                    int option = Menu();
                    switch (option) {
                        case 1:
                            topicSubscribe(); break;
                        case 2:
                            topicUnSubscribe(); break;
                        case 3:
                            getAllTopics(); break;
                        case 4:
                            publishMessage(); break;
                        case 5:
                            uploadBlobToBucket(); break;
                        case 6:
                            blobWithPublicAccess(); break;
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

    static void topicSubscribe() {
        String topicName = read("Insert topic name", new Scanner(System.in));

        SubscribeUnSubscribe request = SubscribeUnSubscribe.newBuilder()
                .setUsrName(userName)
                .setTopicName(topicName)
                .build();

        noBlockStub.topicSubscribe(request, new StreamObserver<ForumMessage>() {
            @Override
            public void onNext(ForumMessage forumMessage) {
                System.out.println("\n");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error subscribing to topic " + topicName);
            }

            @Override
            public void onCompleted() {
                System.out.println("topicSubscribe completed");
            }
        });
    }

    static void topicUnSubscribe() throws InterruptedException { // get N even numbers
        String topicName = read("Insert topic name", new Scanner(System.in));

        SubscribeUnSubscribe request = SubscribeUnSubscribe.newBuilder()
                .setUsrName(userName)
                .setTopicName(topicName)
                .build();

        blockingStub.topicUnSubscribe(request);

        System.out.println("User " + userName + " unsubscribed to topic " + topicName);
    }

    static void getAllTopics() {
        ExistingTopics topics = blockingStub.getAllTopics(Empty.newBuilder().build());
        System.out.println("All topics: " + topics.getTopicNameList());
    }

    static void publishMessage() {
        String topicName = read("Insert topic name", new Scanner(System.in));
        String message = read("Insert message", new Scanner(System.in));

        ForumMessage request = ForumMessage.newBuilder()
                .setFromUser(userName)
                .setTopicName(topicName)
                .setTxtMsg(message)
                .build();

        blockingStub.publishMessage(request);
        System.out.println("Message sent to topic " + topicName + " by user " + userName);
    }

    private static void uploadBlobToBucket() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("The name of Bucket? ");
        String bucketName = scan.nextLine();
        System.out.println("The name of Blob? ");
        String blobName = scan.nextLine();
        System.out.println("Enter the pathname of the file to upload? ");
        String absFileName = scan.nextLine();
        Path uploadFrom = Paths.get(absFileName);
        String contentType = Files.probeContentType(uploadFrom);
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        if (Files.size(uploadFrom) > 1_000_000) {
            // When content is not available or large (1MB or more) it is recommended
            // to write it in chunks via the blob's channel writer.
            try (WriteChannel channel = storage.writer(blobInfo)) {
                byte[] buffer = new byte[1024];
                try (InputStream input = Files.newInputStream(uploadFrom)) {
                    int limit;
                    while ((limit = input.read(buffer)) >= 0) {
                        try {
                            channel.write(ByteBuffer.wrap(buffer, 0, limit));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } else {
            byte[] bytes = Files.readAllBytes(uploadFrom);
            // create the blob in one request.
            storage.create(blobInfo, bytes);
        }
        System.out.println("Blob " + blobName + " created in bucket " + bucketName);
    }

    private static void blobWithPublicAccess() {
        Scanner scan = new Scanner(System.in);
        System.out.println("The name of Bucket? ");
        String bucketName = scan.nextLine();
        System.out.println("The name of Blob? ");
        String blobName = scan.nextLine();

        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        Acl.Entity aclEnt = Acl.User.ofAllUsers();
        Acl.Role[] roles = Acl.Role.values();
        Acl.Role role = Acl.Role.READER;
        Acl acl = Acl.newBuilder(aclEnt, role).build();
        blob.createAcl(acl);
        System.out.println("Blob " + blobName + " permissions changed to public access");
    }

    private static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Subscribe to a topic");
            System.out.println(" 2 - Unsubscribe from a topic");
            System.out.println(" 3 - List all topics");
            System.out.println(" 4 - Publish a message in a topic of your choice");
            System.out.println(" 5 - Upload blob to bucket");
            System.out.println(" 6 - Give public access to a blob");
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
