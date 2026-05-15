package gcpservices.model;

public class DetectedLabel {
    public String label;
    public String translatedLabel;
    public double confidence;

    public DetectedLabel() {
    }

    public DetectedLabel(String label, String translatedLabel, double confidence) {
        this.label = label;
        this.translatedLabel = translatedLabel;
        this.confidence = confidence;
    }
}

