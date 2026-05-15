package forumpubsub;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.gson.Gson;
import gcpservices.labels.ImageLabelingService;
import gcpservices.model.DetectedLabel;
import gcpservices.model.ProcessingJobMessage;
import gcpservices.model.ProcessingRecord;
import firestore.DocumentCollection;
import com.google.pubsub.v1.PubsubMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessageReceiveHandler implements MessageReceiver {
    private final DocumentCollection repository;
    private final ImageLabelingService labelingService;
    private final String workerName;
    private final Gson gson = new Gson();

    public MessageReceiveHandler(DocumentCollection repository, ImageLabelingService labelingService, String workerName) {
        this.repository = repository;
        this.labelingService = labelingService;
        this.workerName = workerName;
    }

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        try {
            ProcessingJobMessage job = gson.fromJson(message.getData().toStringUtf8(), ProcessingJobMessage.class);
            String gsUri = "gs://" + job.bucketName + "/" + job.blobName;
            List<DetectedLabel> labels = labelingService.detectAndTranslate(gsUri);

            ProcessingRecord record = new ProcessingRecord(
                    job.requestId,
                    job.fileName,
                    job.bucketName,
                    job.blobName,
                    job.contentType,
                    job.submittedAt,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()),
                    "COMPLETED",
                    labels
            );

            repository.saveOrUpdate(record).get();

            System.out.println("Processed request " + job.requestId + " by worker " + workerName + " with " + labels.size() + " labels.");
            consumer.ack();
        } catch (Exception e) {
            System.out.println("Error processing message: " + e.getMessage());
            consumer.nack();
        }
    }
}
