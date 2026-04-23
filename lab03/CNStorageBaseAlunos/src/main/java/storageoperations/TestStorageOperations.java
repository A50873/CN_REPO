package storageoperations;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;


import java.io.FileInputStream;
import java.util.Scanner;

public class TestStorageOperations {

    static int Menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("######## MENU ##########");
            System.out.println("Options for Google Storage Operations:");
            System.out.println(" 0: List Buckets in Project");
            System.out.println(" 1: Create a new Bucket");
            System.out.println(" 2: Upload Blob to Bucket");
            System.out.println(" 3: Download Blob from Bucket");
            System.out.println(" 4: Delete a Bucket");
            System.out.println(" 5: Give public access to Blob");
            System.out.println("..........");
            System.out.println("99: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 5) || option == 99));
        return option;
    }

    public static void main(String[] args) throws Exception {
        // Service Accounts:
        //
        // Use the environment variable GOOGLE_APPLICATION_CREDENTIALS
        // to point to the file name with the service account private key
        //   Windows:
        //   $ set GOOGLE_APPLICATION_CREDENTIALS = c:\keys\my-service-account.json
        //   Linux:
        //   $ export GOOGLE_APPLICATION_CREDENTIALS = /home/user/my-service-account.json
        //
        // environment variable -> GOOGLE_APPLICATION_CREDENTIALS=C:\ISEL\6_semestre\CN\keys\cn2526-t3-g07-a1dcad586982.json
        Storage storage;
        StorageOptions storageOptions = StorageOptions.getDefaultInstance();
        storage = storageOptions.getService();
        String projID = storageOptions.getProjectId();
        if (projID != null) System.out.println("Current Project ID:" + projID);
        else {
            System.out.println("The environment variable GOOGLE_APPLICATION_CREDENTIALS isn't well defined!!");
            System.exit(-1);
        }
        StorageOperations storageOper = new StorageOperations(storage);
        while (true) {
            try {
                int option = Menu();
                switch (option) {
                    case 0:
                        storageOper.listBuckets(projID);
                        break;
                    case 1:
                        storageOper.createBucket();
                        break;
                    case 2:
                        storageOper.uploadBlobToBucket();
                        break;
                    case 3:
                        storageOper.downloadBlobFromBucket();
                        break;
                    case 4:
                        storageOper.deleteBucket();
                        break;
                    // TODO: Other Operations
                    case 5:
                        storageOper.blobWithPublicAccess();
                        break;
                    //
                    case 99:
                        System.exit(0);
                }
            } catch (Exception ex) {
                System.out.println("Error executing operations!");
                ex.printStackTrace();
            }
        }
    }
}
