package firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import gcpservices.model.ProcessingRecord;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DocumentCollection {

    private static final String DEFAULT_COLLECTION = "processingRequests";

    private final Firestore db;
    private final String collectionName;

    public DocumentCollection() throws IOException {
        this(FirestoreOptions.newBuilder()
                .setCredentials((GoogleCredentials) GoogleCredentials.getApplicationDefault())
                .build()
                .getService(), DEFAULT_COLLECTION);
    }

    public DocumentCollection(Firestore db) {
        this(db, DEFAULT_COLLECTION);
    }

    public DocumentCollection(Firestore db, String collectionName) {
        this.db = Objects.requireNonNull(db, "db");
        this.collectionName = Objects.requireNonNull(collectionName, "collectionName");
    }

    public ApiFuture<WriteResult> saveOrUpdate(ProcessingRecord record) {
        Objects.requireNonNull(record, "record");
        if (record.requestId == null || record.requestId.isBlank()) {
            throw new IllegalArgumentException("record.requestId is required");
        }
        return db.collection(collectionName)
                .document(record.requestId)
                .set(DocCollectionUtils.toDocumentMap(record));
    }

    public Optional<ProcessingRecord> findByRequestId(String requestId) throws Exception {
        DocumentSnapshot snapshot = db.collection(collectionName).document(requestId).get().get();
        if (!snapshot.exists()) {
            return Optional.empty();
        }
        return Optional.of(DocCollectionUtils.fromDocument(snapshot));
    }

    public List<String> findFileNamesByLabelBetweenDates(String label, String fromIsoDate, String toIsoDate) throws Exception {
        Query query = db.collection(collectionName)
                .whereArrayContains("labelNames", label)
                .whereGreaterThanOrEqualTo("processedAt", fromIsoDate)
                .whereLessThanOrEqualTo("processedAt", toIsoDate);
        QuerySnapshot snapshot = query.get().get();
        return DocCollectionUtils.extractFileNames(snapshot.getDocuments());
    }

    public List<ProcessingRecord> findRecordsByLabelBetweenDates(String label, String fromIsoDate, String toIsoDate) throws Exception {
        Query query = db.collection(collectionName)
                .whereArrayContains("labelNames", label)
                .whereGreaterThanOrEqualTo("processedAt", fromIsoDate)
                .whereLessThanOrEqualTo("processedAt", toIsoDate);
        QuerySnapshot snapshot = query.get().get();
        return snapshot.getDocuments().stream().map(DocCollectionUtils::fromDocument).toList();
    }

    public void markCompleted(ProcessingRecord record, String processedAtIso) throws Exception {
        record.status = "COMPLETED";
        record.processedAt = processedAtIso != null ? processedAtIso : LocalDateTime.now().toString();
        saveOrUpdate(record).get();
    }

    public Firestore getDb() {
        return db;
    }

    public String getCollectionName() {
        return collectionName;
    }
}
