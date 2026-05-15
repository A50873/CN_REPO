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
import com.google.pubsub.v1.*;

import java.io.IOException;
import java.util.*;

public class PubSubForumClient {
    private static Firestore db;
    private static String projectId;
    private static String userName;
    private static Map<String, Subscriber> activeSubscribers = new HashMap<>();
    private static Gson gson = new Gson();

    public PubSubForumClient() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirestoreOptions options = FirestoreOptions
                .newBuilder()
                .setDatabaseId("db-lab05")
                .setCredentials(credentials)
                .build();
        db = options.getService();

        // Extract projectId from credentials
        projectId = credentials.getProjectId();
    }

    private static String read(String msg, Scanner input) {
        System.out.println(msg);
        return input.nextLine();
    }

    public static void listTopics() throws IOException {
        TopicAdminClient topicAdmin = TopicAdminClient.create();
        TopicAdminClient.ListTopicsPagedResponse res = topicAdmin.listTopics(ProjectName.of(projectId));
        System.out.println("\n=== Available Topics ===");
        boolean hasTopics = false;
        for (Topic top : res.iterateAll()) {
            System.out.println("  - " + top.getName().split("/")[3]);
            hasTopics = true;
        }
        if (!hasTopics) {
            System.out.println("  No topics available");
        }
        System.out.println("=======================\n");
        topicAdmin.close();
    }

    public static void publishMessage(Scanner input) throws Exception {
        String topicID = read("Topic ID to publish to?", input);
        String message = read("Message to publish?", input);

        ForumMessage msg = new ForumMessage(userName, message);
        String jsonMsg = gson.toJson(msg);

        TopicName topicName = TopicName.ofProjectTopicName(projectId, topicID);
        Publisher publisher = Publisher.newBuilder(topicName).build();

        ByteString msgData = ByteString.copyFromUtf8(jsonMsg);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(msgData)
                .build();

        ApiFuture<String> future = publisher.publish(pubsubMessage);
        String msgID = future.get();
        System.out.println("\nMessage published with ID: " + msgID + "\n");
        publisher.shutdown();
    }

    public static void subscribeToTopic(Scanner input) throws IOException {
        String topicID = read("Topic ID to subscribe to?", input);
        String subscriptionID = read("Subscription ID?", input);

        try {
            // Create the subscription if it doesn't exist
            TopicName topicName = TopicName.of(projectId, topicID);
            ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionID);

            SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create();

            try {
                subscriptionAdminClient.getSubscription(subscriptionName);
                System.out.println("Subscription already exists.");
            } catch (Exception e) {
                PushConfig pConfig = PushConfig.getDefaultInstance();
                subscriptionAdminClient.createSubscription(subscriptionName, topicName, pConfig, 0);
                System.out.println("Subscription created.");
            }

            subscriptionAdminClient.close();

            // Create subscriber
            ExecutorProvider executorProvider = InstantiatingExecutorProvider
                    .newBuilder()
                    .setExecutorThreadCount(1) // ensures only 1 message is processed
                    .build();

            MessageReceiveHandler receiver = new MessageReceiveHandler(db, topicID, userName);
            Subscriber subscriber = Subscriber.newBuilder(subscriptionName, receiver)
                    .setExecutorProvider(executorProvider)
                    .build();

            subscriber.startAsync().awaitRunning();
            activeSubscribers.put(subscriptionID, subscriber);

            System.out.println("\nSubscribed to topic '" + topicID + "' with subscription '" + subscriptionID + "'");

        } catch (Exception e) {
            System.out.println("Error subscribing to topic: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void unsubscribeFromTopic(Scanner input) throws IOException {
        String subscriptionID = read("Subscription ID to unsubscribe from?", input);

        if (activeSubscribers.containsKey(subscriptionID)) {
            activeSubscribers.get(subscriptionID).stopAsync();
            activeSubscribers.remove(subscriptionID);
            System.out.println("\nUnsubscribed from subscription '" + subscriptionID + "\n");
        }

        try {
            ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionID);
            SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create();
            subscriptionAdminClient.deleteSubscription(subscriptionName);
            subscriptionAdminClient.close();
            System.out.println("Subscription deleted from Pub/Sub.\n");
        } catch (Exception e) {
            System.out.println("Note: Could not delete subscription from Pub/Sub: " + e.getMessage());
        }
    }

    public static void listActiveSubscriptions() {
        System.out.println("\n=== Active Subscriptions ===");
        if (activeSubscribers.isEmpty()) {
            System.out.println("  No active subscriptions");
        } else {
            for (String subId : activeSubscribers.keySet()) {
                System.out.println("  - " + subId);
            }
        }
        System.out.println("===========================\n");
    }

    private static int menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    ========== MENU ==========");
            System.out.println(" 1 - List all topics");
            System.out.println(" 2 - Subscribe to a topic");
            System.out.println(" 3 - Unsubscribe from a topic");
            System.out.println(" 4 - List active subscriptions");
            System.out.println(" 5 - Publish a message");
            System.out.println("99 - Exit");
            System.out.println("    ==========================");
            System.out.println("Choose an option: ");
            try {
                op = scan.nextInt();
                scan.nextLine(); // consume newline
            } catch (InputMismatchException e) {
                scan.nextLine(); // consume invalid input
                op = 0;
            }
        } while (!((op >= 1 && op <= 5) || op == 99));
        return op;
    }

    public static void main(String[] args) throws Exception {
        // Set environment variable:
        //     GOOGLE_APPLICATION_CREDENTIALS="pathname to AccountServiceKEY.json"

        PubSubForumClient client = new PubSubForumClient();

        Scanner scanInput = new Scanner(System.in);
        userName = read("\nEnter your username: ", scanInput);

        System.out.println("\nWelcome, " + userName + "!\n");

        while (true) {
            try {
                int option = menu();
                switch (option) {
                    case 1:
                        listTopics();
                        break;
                    case 2:
                        subscribeToTopic(scanInput);
                        break;
                    case 3:
                        unsubscribeFromTopic(scanInput);
                        break;
                    case 4:
                        listActiveSubscriptions();
                        break;
                    case 5:
                        publishMessage(scanInput);
                        break;
                    case 99:
                        System.out.println("\nStopping all subscriptions...");
                        for (Subscriber subscriber : activeSubscribers.values()) {
                            subscriber.stopAsync();
                        }
                        System.out.println("Goodbye!");
                        System.exit(0);
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}


