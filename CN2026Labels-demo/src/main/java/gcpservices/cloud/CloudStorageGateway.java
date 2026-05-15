package gcpservices.cloud;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.util.Objects;

public class CloudStorageGateway {

    private final Storage storage;

    public CloudStorageGateway() {
        this(StorageOptions.getDefaultInstance().getService());
    }

    public CloudStorageGateway(Storage storage) {
        this.storage = storage;
    }

    public void storeImage(String bucketName, String blobName, byte[] data, String contentType) {
        Objects.requireNonNull(bucketName, "bucketName");
        Objects.requireNonNull(blobName, "blobName");
        Objects.requireNonNull(data, "data");

        BlobInfo.Builder builder = BlobInfo.newBuilder(BlobId.of(bucketName, blobName));
        if (contentType != null && !contentType.isBlank()) {
            builder.setContentType(contentType);
        }
        storage.create(builder.build(), data);
    }
}

