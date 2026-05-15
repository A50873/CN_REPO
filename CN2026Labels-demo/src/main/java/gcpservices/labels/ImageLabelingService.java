package gcpservices.labels;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import gcpservices.model.DetectedLabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageLabelingService {

    private final Translate translateService;

    public ImageLabelingService() {
        this(TranslateOptions.getDefaultInstance().getService());
    }

    public ImageLabelingService(Translate translateService) {
        this.translateService = translateService;
    }

    public List<DetectedLabel> detectAndTranslate(String gsUri) throws IOException {
        List<DetectedLabel> result = new ArrayList<>();

        Image img = Image.newBuilder()
                .setSource(ImageSource.newBuilder().setImageUri(gsUri).build())
                .build();

        Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(img)
                .build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(List.of(request));
            for (AnnotateImageResponse imageResponse : response.getResponsesList()) {
                if (imageResponse.hasError()) {
                    throw new IOException("Vision API error: " + imageResponse.getError().getMessage());
                }
                for (EntityAnnotation annotation : imageResponse.getLabelAnnotationsList()) {
                    String englishLabel = annotation.getDescription();
                    String translated = translateLabel(englishLabel);
                    result.add(new DetectedLabel(englishLabel, translated, annotation.getScore()));
                }
            }
        }

        return result;
    }

    public String translateLabel(String label) {
        Translation translation = translateService.translate(
                label,
                Translate.TranslateOption.sourceLanguage("en"),
                Translate.TranslateOption.targetLanguage("pt"));
        return translation.getTranslatedText();
    }
}

