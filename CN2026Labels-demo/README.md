# CN2026Labels

Project restructured to follow the same style as the other labs: simple console applications, no custom gRPC server.

## Applications

- `gcpservices.AppMain` — interactive client to submit images, query results, and list files.
- `gcpservices.worker.LabelsWorkerApp` — background worker that consumes Pub/Sub jobs, runs Vision + Translate, and stores the result in Firestore.

## Requirements

- Google Cloud credentials via `GOOGLE_APPLICATION_CREDENTIALS`
- Pub/Sub topic for jobs
- Cloud Storage bucket for uploaded images
- Firestore database for processing records

## Build

```powershell
cd C:\ISEL\6_semestre\CN\CN_REPO\CN2026Labels
mvn clean package
```

## Run client

```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS = "C:\path\to\service-account.json"
java -cp target\CN2026Labels-1.0-cn-jar-with-dependencies.jar gcpservices.AppMain
```

## Run worker

```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS = "C:\path\to\service-account.json"
java -cp target\CN2026Labels-1.0-cn-jar-with-dependencies.jar gcpservices.worker.LabelsWorkerApp
```

## Console menu

- `0` - list Pub/Sub topics
- `1` - submit image for processing
- `2` - get processing result by request ID
- `3` - list files by label and date
- `99` - exit

## Notes

- The old gRPC server class is kept only as a compatibility stub.
- Date queries use `yyyy-MM-dd` and are converted to ISO local date-time ranges internally.

