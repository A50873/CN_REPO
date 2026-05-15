package gcpservices.model;

public class ProcessingJobMessage {
    public String requestId;
    public String bucketName;
    public String blobName;
    public String fileName;
    public String contentType;
    public String submittedAt;

    public ProcessingJobMessage() {
    }

    public ProcessingJobMessage(String requestId, String bucketName, String blobName, String fileName, String contentType, String submittedAt) {
        this.requestId = requestId;
        this.bucketName = bucketName;
        this.blobName = blobName;
        this.fileName = fileName;
        this.contentType = contentType;
        this.submittedAt = submittedAt;
    }
}

