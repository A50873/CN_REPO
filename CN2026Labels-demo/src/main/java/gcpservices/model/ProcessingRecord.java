package gcpservices.model;

import java.util.ArrayList;
import java.util.List;

public class ProcessingRecord {
    public String requestId;
    public String fileName;
    public String bucketName;
    public String blobName;
    public String contentType;
    public String submittedAt;
    public String processedAt;
    public String status;
    public List<DetectedLabel> labels = new ArrayList<>();

    public ProcessingRecord() {
    }

    public ProcessingRecord(String requestId, String fileName, String bucketName, String blobName,
                            String contentType, String submittedAt, String processedAt, String status,
                            List<DetectedLabel> labels) {
        this.requestId = requestId;
        this.fileName = fileName;
        this.bucketName = bucketName;
        this.blobName = blobName;
        this.contentType = contentType;
        this.submittedAt = submittedAt;
        this.processedAt = processedAt;
        this.status = status;
        if (labels != null) {
            this.labels = labels;
        }
    }
}

