package gcpservices.worker;

import firestore.DocumentCollection;
import forumpubsub.MessageReceiveHandler;
import forumpubsub.PubSubForumClient;
import gcpservices.labels.ImageLabelingService;
import com.google.cloud.pubsub.v1.Subscriber;

public class LabelsWorkerApp {

    public static void main(String[] args) throws Exception {
        String topicId = args.length > 0 ? args[0] : System.getenv().getOrDefault("CN2026_TOPIC_ID", "cn2026-labels-jobs");
        String subscriptionId = args.length > 1 ? args[1] : System.getenv().getOrDefault("CN2026_SUBSCRIPTION_ID", "cn2026-labels-workers");
        String workerName = args.length > 2 ? args[2] : System.getenv().getOrDefault("CN2026_WORKER_NAME", "worker-1");

        DocumentCollection repository = new DocumentCollection();
        ImageLabelingService labelingService = new ImageLabelingService();
        PubSubForumClient pubSubClient = new PubSubForumClient();
        MessageReceiveHandler receiver = new MessageReceiveHandler(repository, labelingService, workerName);

        Subscriber subscriber = pubSubClient.startSharedSubscription(topicId, subscriptionId, receiver);

        System.out.println("Worker '" + workerName + "' subscribed to topic '" + topicId + "' using subscription '" + subscriptionId + "'.");
        System.out.println("Press Ctrl+C to stop the worker.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            pubSubClient.stopSubscription(subscriptionId);
        }));

        Thread.currentThread().join();
    }
}

