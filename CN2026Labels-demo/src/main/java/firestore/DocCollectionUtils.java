package firestore;

import com.google.cloud.firestore.DocumentSnapshot;
import gcpservices.model.DetectedLabel;
import gcpservices.model.ProcessingRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DocCollectionUtils {

	private DocCollectionUtils() {
	}

	public static Map<String, Object> toDocumentMap(ProcessingRecord record) {
		Map<String, Object> data = new HashMap<>();
		data.put("requestId", record.requestId);
		data.put("fileName", record.fileName);
		data.put("bucketName", record.bucketName);
		data.put("blobName", record.blobName);
		data.put("contentType", record.contentType);
		data.put("submittedAt", record.submittedAt);
		data.put("processedAt", record.processedAt);
		data.put("status", record.status);

		List<Map<String, Object>> labels = new ArrayList<>();
		List<String> labelNames = new ArrayList<>();
		if (record.labels != null) {
			for (DetectedLabel label : record.labels) {
				Map<String, Object> labelMap = new HashMap<>();
				labelMap.put("label", label.label);
				labelMap.put("translatedLabel", label.translatedLabel);
				labelMap.put("confidence", label.confidence);
				labels.add(labelMap);
				if (label.label != null) {
					labelNames.add(label.label);
				}
			}
		}
		data.put("labels", labels);
		data.put("labelNames", labelNames);
		return data;
	}

	@SuppressWarnings("unchecked")
	public static ProcessingRecord fromDocument(DocumentSnapshot document) {
		ProcessingRecord record = new ProcessingRecord();
		record.requestId = document.getString("requestId");
		record.fileName = document.getString("fileName");
		record.bucketName = document.getString("bucketName");
		record.blobName = document.getString("blobName");
		record.contentType = document.getString("contentType");
		record.submittedAt = document.getString("submittedAt");
		record.processedAt = document.getString("processedAt");
		record.status = document.getString("status");

		List<DetectedLabel> labels = new ArrayList<>();
		List<Map<String, Object>> rawLabels = (List<Map<String, Object>>) document.get("labels");
		if (rawLabels != null) {
			for (Map<String, Object> labelMap : rawLabels) {
				DetectedLabel label = new DetectedLabel();
				label.label = (String) labelMap.get("label");
				label.translatedLabel = (String) labelMap.get("translatedLabel");
				Object confidence = labelMap.get("confidence");
				if (confidence instanceof Number number) {
					label.confidence = number.doubleValue();
				}
				labels.add(label);
			}
		}
		record.labels = labels;
		return record;
	}

	public static List<String> extractFileNames(Iterable<? extends DocumentSnapshot> documents) {
		List<String> fileNames = new ArrayList<>();
		for (DocumentSnapshot document : documents) {
			String fileName = document.getString("fileName");
			if (fileName != null && !fileName.isBlank()) {
				fileNames.add(fileName);
			}
		}
		return fileNames;
	}
}
