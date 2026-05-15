package gcpservices;

import firestore.DocumentCollection;
import forumpubsub.PubSubForumClient;
import gcpservices.cloud.CloudStorageGateway;
import gcpservices.model.ProcessingJobMessage;
import gcpservices.model.ProcessingRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class AppMain {

    private static final DateTimeFormatter ISO_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static DocumentCollection repository;
    private static CloudStorageGateway storageGateway;
    private static PubSubForumClient pubSubClient;

    public AppMain() throws IOException {
        repository = new DocumentCollection();
        storageGateway = new CloudStorageGateway();
        pubSubClient = new PubSubForumClient();
    }

    private static String read(String msg, Scanner input) {
        System.out.println(msg);
        return input.nextLine();
    }

    private static int menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    ========== MENU ==========");
            System.out.println(" 0 - List Pub/Sub topics");
            System.out.println(" 1 - Submit image for processing");
            System.out.println(" 2 - Get processing result by request ID");
            System.out.println(" 3 - List files by label and date");
            System.out.println("99 - Exit");
            System.out.println("    ==========================");
            System.out.println("Choose an option: ");
            try {
                op = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                scan.nextLine();
                op = -1;
            }
        } while (!((op >= 0 && op <= 3) || op == 99));
        return op;
    }

    private static void listTopics() {
        try {
            pubSubClient.listTopics();
        } catch (Exception e) {
            System.out.println("Error listing topics: " + e.getMessage());
        }
    }

    private static void submitImage(Scanner input) {
        try {
            String topicId = read("Topic ID to publish processing job to?", input);
            String bucketName = read("Bucket name?", input);
            String imagePathText = read("Local image path?", input);
            String blobName = read("Blob name (leave blank to auto-generate)?", input);
            String contentType = read("Content type (leave blank for auto-detect)?", input);

            Path imagePath = Path.of(imagePathText);
            byte[] imageBytes = Files.readAllBytes(imagePath);
            String fileName = imagePath.getFileName().toString();
            String requestId = UUID.randomUUID().toString();
            String submittedAt = ISO_DATE_TIME.format(LocalDateTime.now());

            if (blobName == null || blobName.isBlank()) {
                blobName = requestId + "-" + fileName;
            }
            if (contentType == null || contentType.isBlank()) {
                contentType = guessContentType(fileName);
            }

            storageGateway.storeImage(bucketName, blobName, imageBytes, contentType);

            ProcessingRecord pending = new ProcessingRecord(
                    requestId,
                    fileName,
                    bucketName,
                    blobName,
                    contentType,
                    submittedAt,
                    null,
                    "SUBMITTED",
                    List.of()
            );
            repository.saveOrUpdate(pending).get();

            ProcessingJobMessage job = new ProcessingJobMessage(
                    requestId,
                    bucketName,
                    blobName,
                    fileName,
                    contentType,
                    submittedAt
            );

            String messageId = pubSubClient.publishProcessingJob(topicId, job);

            System.out.println();
            System.out.println("Image submitted successfully.");
            System.out.println("Request ID = " + requestId);
            System.out.println("Pub/Sub message ID = " + messageId);
            System.out.println("Bucket = " + bucketName);
            System.out.println("Blob = " + blobName);
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error submitting image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void getProcessingResult(Scanner input) {
        try {
            String requestId = read("Insert request ID: ", input);
            Optional<ProcessingRecord> maybeRecord = repository.findByRequestId(requestId);

            if (maybeRecord.isEmpty()) {
                System.out.println("No processing record found for request ID " + requestId);
                return;
            }

            ProcessingRecord record = maybeRecord.get();
            System.out.println("\n=== Processing Result ===");
            System.out.println("Request ID: " + record.requestId);
            System.out.println("File Name: " + record.fileName);
            System.out.println("Bucket Name: " + record.bucketName);
            System.out.println("Blob Name: " + record.blobName);
            System.out.println("Content Type: " + record.contentType);
            System.out.println("Submitted At: " + record.submittedAt);
            System.out.println("Processed At: " + record.processedAt);
            System.out.println("Status: " + record.status);
            if (record.labels == null || record.labels.isEmpty()) {
                System.out.println("Labels: none");
            } else {
                System.out.println("Labels:");
                record.labels.forEach(label -> {
                    System.out.println("  - " + label.label + " -> " + label.translatedLabel + " (" + label.confidence + ")");
                });
            }
            System.out.println("========================\n");
        } catch (Exception e) {
            System.out.println("Error fetching result: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listFilesByLabel(Scanner input) {
        try {
            String label = read("Insert label: ", input);
            String fromDateText = read("Insert from date (yyyy-MM-dd): ", input);
            String toDateText = read("Insert to date (yyyy-MM-dd): ", input);

            LocalDate fromDate = LocalDate.parse(fromDateText);
            LocalDate toDate = LocalDate.parse(toDateText);
            String fromIso = fromDate.atStartOfDay().format(ISO_DATE_TIME);
            String toIso = toDate.plusDays(1).atStartOfDay().minusNanos(1).format(ISO_DATE_TIME);

            List<ProcessingRecord> records = repository.findRecordsByLabelBetweenDates(label, fromIso, toIso);
            System.out.println("\n=== Matching Files ===");
            if (records.isEmpty()) {
                System.out.println("No files found.");
            } else {
                for (ProcessingRecord record : records) {
                    System.out.println("Request ID: " + record.requestId);
                    System.out.println("File Name: " + record.fileName);
                    System.out.println("Processed At: " + record.processedAt);
                    System.out.println("Status: " + record.status);
                    System.out.println();
                }
            }
            System.out.println("======================\n");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        } catch (Exception e) {
            System.out.println("Error listing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String guessContentType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".png")) {
            return "image/png";
        }
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lower.endsWith(".gif")) {
            return "image/gif";
        }
        if (lower.endsWith(".bmp")) {
            return "image/bmp";
        }
        return "application/octet-stream";
    }

    public static void main(String[] args) throws Exception {
        new AppMain();
        Scanner scanInput = new Scanner(System.in);

        System.out.println("\nWelcome to CN2026Labels!\n");

        while (true) {
            try {
                int option = menu();
                switch (option) {
                    case 0:
                        listTopics();
                        break;
                    case 1:
                        submitImage(scanInput);
                        break;
                    case 2:
                        getProcessingResult(scanInput);
                        break;
                    case 3:
                        listFilesByLabel(scanInput);
                        break;
                    case 99:
                        System.out.println("\nStopping application...");
                        if (pubSubClient != null) {
                            pubSubClient.stopAllSubscriptions();
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
