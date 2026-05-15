package forumpubsub;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectName;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.TopicName;
import com.google.pubsub.v1.Topic;
import gcpservices.model.ProcessingJobMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PubSubForumClient {
    private final Firestore db;
    private final String projectId;
    private final Gson gson = new Gson();
    private final Map<String, Subscriber> activeSubscribers = new ConcurrentHashMap<>();

    public PubSubForumClient() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirestoreOptions options = FirestoreOptions
                .newBuilder()
                .setDatabaseId("trab-final-db")
                .setCredentials(credentials)
                .build();
        db = options.getService();

        // Extract projectId from credentials
        projectId = credentials.getProjectId();
    }

    public PubSubForumClient(String projectId, Firestore db) {
        this.projectId = Objects.requireNonNull(projectId, "projectId");
        this.db = Objects.requireNonNull(db, "db");
    }


    public Firestore getDb() {
        return db;
    }

    public void listTopics() throws IOException {
        try (TopicAdminClient topicAdmin = TopicAdminClient.create()) {
            TopicAdminClient.ListTopicsPagedResponse res = topicAdmin.listTopics(ProjectName.of(projectId));
            System.out.println("\n=== Available Topics ===");
            boolean hasTopics = false;
            for (Topic topic : res.iterateAll()) {
                System.out.println("  - " + topic.getName().split("/")[3]);
                hasTopics = true;
            }
            if (!hasTopics) {
                System.out.println("  No topics available");
            }
            System.out.println("=======================\n");
        }
    }

    public String publishProcessingJob(String topicId, ProcessingJobMessage job) throws Exception {
        TopicName topicName = TopicName.of(projectId, topicId);
        Publisher publisher = Publisher.newBuilder(topicName).build();
        try {
            String jsonMsg = gson.toJson(job);
            ByteString msgData = ByteString.copyFromUtf8(jsonMsg);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(msgData).build();
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            return future.get();
        } finally {
            publisher.shutdown();
        }
    }

    public Subscriber startSharedSubscription(String topicId, String subscriptionId, com.google.cloud.pubsub.v1.MessageReceiver receiver) throws Exception {
        TopicName topicName = TopicName.of(projectId, topicId);
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            try {
                subscriptionAdminClient.getSubscription(subscriptionName);
            } catch (Exception e) {
                subscriptionAdminClient.createSubscription(subscriptionName, topicName, PushConfig.getDefaultInstance(), 0);
            }
        }

        ExecutorProvider executorProvider = InstantiatingExecutorProvider.newBuilder()
                .setExecutorThreadCount(1)
                .build();

        Subscriber subscriber = Subscriber.newBuilder(subscriptionName, receiver)
                .setExecutorProvider(executorProvider)
                .build();
        subscriber.startAsync().awaitRunning();
        activeSubscribers.put(subscriptionId, subscriber);
        return subscriber;
    }

    public void stopSubscription(String subscriptionId) {
        Subscriber subscriber = activeSubscribers.remove(subscriptionId);
        if (subscriber != null) {
            subscriber.stopAsync();
        }
    }

    public void stopAllSubscriptions() {
        activeSubscribers.forEach((id, subscriber) -> subscriber.stopAsync());
        activeSubscribers.clear();
    }

    public Map<String, Subscriber> getActiveSubscribers() {
        return activeSubscribers;
    }
}



