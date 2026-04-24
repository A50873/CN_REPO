package cn;

import cn.operations.*;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DocumentCollection {
    GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

    FirestoreOptions options = FirestoreOptions
            .newBuilder().setDatabaseId("lab04-db").setCredentials(credentials)
            .build();
    Firestore db = options.getService();

    public DocumentCollection() throws IOException {
    }

    private void showDocumentFromDocId() {
        try {
            String ID = read("Insert document ID: ", new Scanner(System.in));
            DocumentReference docRef = db.collection("Locations").document(ID);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            // ler objeto: obtém campos do documento para campos
            // com o mesmo nome na classe
            OcupacaoTemporaria ocup = document.toObject(OcupacaoTemporaria.class);
            if (ocup == null) {
                System.out.println("Documento nao encontrado ou sem dados.");
                return;
            }

            System.out.println("ocup.ID = " + ocup.ID);

            System.out.println("--- Location ---");
            if (ocup.location != null) {
                System.out.println("Point = " + ocup.location.point);
                System.out.println("Freguesia = " + ocup.location.freguesia);
                System.out.println("Local = " + ocup.location.local);
            } else {
                System.out.println("location = null");
            }

            System.out.println("--- Event ---");
            if (ocup.event != null) {
                System.out.println("ID = " + ocup.event.evtID);
                System.out.println("Nome = " + ocup.event.nome);
                System.out.println("Tipo = " + ocup.event.tipo);
                System.out.println("DtInicio = " + ocup.event.dtInicio);
                System.out.println("DtFinal = " + ocup.event.dtFinal);

                if (ocup.event.licenciamento != null) {
                    System.out.println("Licenciamento cod = " + ocup.event.licenciamento.code);
                    System.out.println("DtLicenc = " + ocup.event.licenciamento.dtLicenc);
                } else {
                    System.out.println("Licenciamento = null");
                }

                if (ocup.event.details != null) {
                    for (Map.Entry<String, String> entry : ocup.event.details.entrySet()) {
                        System.out.println("Details[" + entry.getKey() + "] = " + entry.getValue());
                    }
                } else {
                    System.out.println("Details = null");
                }
            } else {
                System.out.println("event = null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCamp() {
        try {
            String ID = read("Insert document ID: ", new Scanner(System.in));
            String campName = read("Insert camp name: ", new Scanner(System.in));
            DocumentReference docRef = db.collection("Locations").document(ID);
            // apagar campo
            Map<String, Object> updates = new HashMap<>();
            updates.put(campName, FieldValue.delete());
            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            System.out.println("Update time : " + writeResult.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryFreguesia() {
        try {
            String freguesia = read("Insert freguesia: ", new Scanner(System.in));
            // Single query
            Query query = db.collection("Locations")
                    .whereEqualTo("location.freguesia", freguesia);
            // retrieve query results asynchronously using query.get()
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            for (DocumentSnapshot doc: querySnapshot.get().getDocuments()) {
                System.out.print("DocID: " + doc.getId());
                System.out.println(" Freguesia: " + doc.get("location.freguesia"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void complexQuery() {
        try {
            String ID = read("Insert ID: ", new Scanner(System.in));
            String freguesia = read("Insert freguesia: ", new Scanner(System.in));
            String eventType = read("Insert event type: ", new Scanner(System.in));
            // complex query
            Query query = db.collection("Locations")
                    .whereGreaterThan("ID", Integer.parseInt(ID))
                    .whereEqualTo("location.freguesia", freguesia)
                    .whereEqualTo("event.tipo", eventType);
            // retrieve query results asynchronously using query.get()
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            for (DocumentSnapshot doc: querySnapshot.get().getDocuments()) {
                System.out.print("DocID: " + doc.getId());
                System.out.println(" Freguesia: " + doc.get("location.freguesia"));
                System.out.println(" Event type: " + doc.get("event.tipo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void complexQueryDate() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date init = formatter.parse("2017-01-31");
            Date end = formatter.parse("2017-03-01");
            // complex query
            Query query = db.collection("Locations")
                    .whereGreaterThan("event.dtInicio", init)
                    .whereLessThan("event.dtInicio", end);
            // retrieve query results asynchronously using query.get()
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            for (DocumentSnapshot doc: querySnapshot.get().getDocuments()) {
                System.out.print("DocID: " + doc.getId());
                System.out.println(" Inicio: " + doc.get("event.dtInicio"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void complexQueryDateV2() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date init = formatter.parse("2017-01-31");
            Date end = formatter.parse("2017-03-01");
            // complex query
            Query query = db.collection("Locations")
                    .whereGreaterThan("event.dtInicio", init)
                    .whereLessThan("event.dtFinal", end);
            // retrieve query results asynchronously using query.get()
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            for (DocumentSnapshot doc: querySnapshot.get().getDocuments()) {
                System.out.print("DocID: " + doc.getId());
                System.out.println(" Inicio: " + doc.get("event.dtInicio"));
                System.out.println(" Fim: " + doc.get("event.dtFinal"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void main() {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Menu ===");
            System.out.println("1 - Show document by ID");
            System.out.println("2 - Delete field from document");
            System.out.println("3 - Query by freguesia");
            System.out.println("4 - Complex query");
            System.out.println("5 - Complex query by date (v1)");
            System.out.println("6 - Complex query by date (v2)");
            System.out.println("99 - Stop server");

            String optionText = read("Choose option: ", input);
            int option;

            try {
                option = Integer.parseInt(optionText);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please insert a number.");
                continue;
            }

            switch (option) {
                case 1:
                    showDocumentFromDocId();
                    break;
                case 2:
                    deleteCamp();
                    break;
                case 3:
                    queryFreguesia();
                    break;
                case 4:
                    complexQuery();
                    break;
                case 5:
                    complexQueryDate();
                    break;
                case 6:
                    complexQueryDateV2();
                    break;
                case 99:
                    System.out.println("Stopping server...");
                    running = false;
                    break;
                default:
                    System.out.println("Option not available.");
                    break;
            }
        }
    }

    private static String read(String msg, Scanner input) {
        System.out.println(msg);
        return input.nextLine();
    }
}
