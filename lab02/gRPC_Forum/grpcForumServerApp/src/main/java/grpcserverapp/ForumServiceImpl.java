package grpcserverapp;

import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import forum.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ForumServiceImpl extends ForumGrpc.ForumImplBase {

    private final ConcurrentMap<String, ConcurrentMap<String, StreamObserver<ForumMessage>>> forumMessages = new ConcurrentHashMap<>();
    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    public ForumServiceImpl() {}

    @Override
    public void topicSubscribe(SubscribeUnSubscribe request, StreamObserver<ForumMessage> responseObserver ) {
        String user = request.getUsrName();
        String topic = request.getTopicName();

        forumMessages.computeIfAbsent(topic, _ -> new ConcurrentHashMap<>()).put(user, responseObserver);
    }

    @Override
    public void topicUnSubscribe(SubscribeUnSubscribe request, StreamObserver<Empty> responseObserver ) {
        String user = request.getUsrName();
        String topic = request.getTopicName();

        ConcurrentMap<String, StreamObserver<ForumMessage>> topicMap = forumMessages.get(topic);

        if(topicMap != null) {
            StreamObserver<ForumMessage> streamObserver = topicMap.remove(user);

            if(streamObserver != null) {
                streamObserver.onCompleted();
            }
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllTopics(Empty request, StreamObserver<ExistingTopics> responseObserver ) {
        ExistingTopics topics = ExistingTopics.newBuilder().addAllTopicName(forumMessages.keySet()).build();

        responseObserver.onNext(topics);
        responseObserver.onCompleted();
    }

    @Override
    public void publishMessage(ForumMessage request, StreamObserver<Empty> responseObserver ) {
        String user = request.getFromUser();
        String topic = request.getTopicName();
        String message = request.getTxtMsg();

        if(!forumMessages.containsKey(topic) || message.isEmpty() ||!forumMessages.get(topic).containsKey(user)) {
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription(
                    "User " + user + " is not subscribed to " + topic
            ).asRuntimeException());
        }

        ConcurrentMap<String, StreamObserver<ForumMessage>> topicMap = forumMessages.get(topic);

        if(message.contains(";")) {
            int i = message.indexOf(';');
            String bucketMessage = message.substring(i + 1);
            String[] bucket = bucketMessage.split(";", 2);

            if(bucket.length != 2) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(
                        "Message " + bucketMessage + " is not a valid bucket message. Must be [;<bucketName>;<blobName>]"
                ).asRuntimeException());
            }

            String bucketName = bucket[0];
            String blobName = bucket[1];

            try {
                downloadBlobFromBucket(bucketName, blobName);
            } catch (IOException e) {
                responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
            }
        }

        for(StreamObserver<ForumMessage> s : topicMap.values()) {
            try {
                s.onNext(request);
            } catch (StatusRuntimeException e) {
                topicMap.remove(user, s);
            }
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private void downloadBlobFromBucket(String bucketName, String blobName) throws IOException {
        Path downloadDir;
        String osName = System.getProperty("os.name").toLowerCase();

        // Detetar SO
        if (osName.contains("win")) {
            // Windows
            downloadDir = Paths.get("C:", "Users", System.getProperty("user.name"), "Downloads");
        } else {
            // Linux/Unix/macOS
            downloadDir = Paths.get(System.getProperty("user.home"), "Downloads");
        }

        Files.createDirectories(downloadDir);

        // Caminho completo do ficheiro a gravar
        Path downloadTo = downloadDir.resolve(blobName);
        System.out.println("download to: " + downloadTo);

        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        if (blob == null) {
            System.out.println("No such Blob exists !");
            return;
        }

        try (PrintStream writeTo = new PrintStream(Files.newOutputStream(downloadTo))) {
            if (blob.getSize() < 1_000_000) {
                // Blob is small read all its content in one request
                byte[] content = blob.getContent();
                writeTo.write(content);
            } else {
                // When Blob size is big or unknown use the blob's channel reader.
                try (ReadChannel reader = blob.reader()) {
                    WritableByteChannel channel = Channels.newChannel(writeTo);
                    ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
                    while (reader.read(bytes) > 0) {
                        bytes.flip();
                        channel.write(bytes);
                        bytes.clear();
                    }
                }
            }
        }
        System.out.println("Blob " + blobName + " downloaded to " + downloadTo);
    }
}
