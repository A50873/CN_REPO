package forumpubsub;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.firestore.Firestore;
import com.google.gson.Gson;
import com.google.pubsub.v1.PubsubMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MessageReceiveHandler implements MessageReceiver {
    private final Firestore db;
    private final String topicName;
    private final String receiverName;
    private final Gson gson = new Gson();

    public MessageReceiveHandler(Firestore db, String topicName, String receiverName) {
        this.db = db;
        this.topicName = topicName;
        this.receiverName = receiverName;
    }

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        String messageData = message.getData().toStringUtf8();
        try {
            // Parse the JSON message
            ForumMessage forumMsg = gson.fromJson(messageData, ForumMessage.class);

            // Display the message
            System.out.println("\n=== New Message Received ===");
            System.out.println("Sender: " + forumMsg.sender);
            System.out.println("Message: " + forumMsg.message);
            System.out.println("Topic: " + topicName);
            System.out.println("=============================\n");

            // Save to Firestore
            Map<String, Object> messageDoc = new HashMap<>();
            messageDoc.put("sender", forumMsg.sender);
            messageDoc.put("message", forumMsg.message);
            messageDoc.put("topicName", topicName);
            messageDoc.put("receiverName", receiverName);
            messageDoc.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            db.collection("messages")
                    .add(messageDoc)
                    .get();

            // Acknowledge the message
            consumer.ack();
        } catch (Exception e) {
            System.out.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
            consumer.nack();
        }
    }
}
