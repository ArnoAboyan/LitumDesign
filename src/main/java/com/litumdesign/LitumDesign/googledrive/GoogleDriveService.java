package com.litumdesign.LitumDesign.googledrive;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/* class to demonstrate use of Drive files list API */
@Component
@RequiredArgsConstructor
@Log4j2
public class GoogleDriveService {
    private final ProductEntityService productEntityService;
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "LitumDesign";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE);
    //    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/litumdesign-398209-83c385017db8.json";


//    /**
//     * Creates an authorized Credential object.
//     *
//     * @param HTTP_TRANSPORT The network HTTP Transport.
//     * @return An authorized Credential object.
//     * @throws IOException If the credentials.json file cannot be found.
//     */


//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
//            throws IOException {
//        // Load client secrets.
//        InputStream in = GoogleDriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in == null) {
//            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//        }
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//
//
//
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8082).build();
//        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//        //returns an authorized Credential object.
//        return credential;
//    }


//    public static Drive getInstance() throws GeneralSecurityException, IOException {
//        // Build a new authorized API client service.
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//        return service;
//    }


//    public static Drive getInstance() throws GeneralSecurityException, IOException {
//        // Build a new authorized API client service.
//    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(ServiceAccountCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
//            .createScoped(SCOPES));
//    Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
//            .setApplicationName(APPLICATION_NAME)
//            .build();
//        return service;
//    }

    @Value("${google_service_client_privateid}")
     String GOOGLE_CREDENTIALS_PRIVATEID;

    @Value("${google_service_client_private}")
     String GOOGLE_CREDENTIALS_PRIVATE;

    @Value("${google_service_clientemail}")
     String GOOGLE_CREDENTIALS_CLIENTEMAIL;

    @Value("${google_service_clientid}")
     String GOOGLE_CREDENTIALS_CLIENTID;


    public  Drive getInstance() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        System.out.println(GOOGLE_CREDENTIALS_PRIVATEID);
        System.out.println(GOOGLE_CREDENTIALS_PRIVATE);
        System.out.println(GOOGLE_CREDENTIALS_CLIENTEMAIL);
        System.out.println(GOOGLE_CREDENTIALS_CLIENTID);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(ServiceAccountCredentials.fromPkcs8(GOOGLE_CREDENTIALS_CLIENTID,
                        GOOGLE_CREDENTIALS_CLIENTEMAIL,
                        GOOGLE_CREDENTIALS_PRIVATE,
                        GOOGLE_CREDENTIALS_PRIVATEID,
                        SCOPES)
                .createScoped(SCOPES));
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }


    public  void main(String... args) throws IOException, GeneralSecurityException {
        Drive service = getInstance();


    }


    @Transactional
    public String uploadFile(MultipartFile file) throws GeneralSecurityException, IOException {


        try {
            System.out.println(file.getOriginalFilename());

            File fileMetadata = new File();
            fileMetadata.setName(file.getOriginalFilename());
            File uploadFile = getInstance()
                    .files()
                    .create(fileMetadata, new InputStreamContent(
                            file.getContentType(),
                            new ByteArrayInputStream(file.getBytes()))
                    )
                    .setFields("id, name")
//                .setFields("name")
                    .execute();


            String fileId = uploadFile.getId();

            System.out.println("GDFILEID ->>> " + uploadFile.getName());
            System.out.println("GDFILEID ->>> " + uploadFile.getId());

            return uploadFile.getId();

        } catch (Exception e) {
            System.out.printf("Error: " + e);
        }
        return null;
    }
//
//

    public String getAllAudio() throws IOException, GeneralSecurityException {
        Drive service = getInstance();


        try {
            FileList result = service.files().list()
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            System.out.println(result.getFiles().toString());
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Drive.Files.Get getFileById(String fileId) throws IOException, GeneralSecurityException {
        Drive service = getInstance();

        try {

            Drive.Files.Get result = service.files().get(fileId);

            System.out.println(result.toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static ByteArrayOutputStream getFile(String realFileId, Drive service) throws IOException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            service.files().get(realFileId)
                    .executeMediaAndDownloadTo(outputStream);

            return outputStream;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to move file: " + e.getDetails());
            throw e;
        }
    }


//    public void downloadFile() throws IOException, GeneralSecurityException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        Drive service = getInstance();
//        service.files().get("1gLh3ZVQpJl494jnT9L1keJm1bb1xiGPN")
//                .executeMediaAndDownloadTo(outputStream);
//
//        String filePath = "E:";
//        String fileName = "5e3bd17765e820213981ad5d80fbce34_L.jpg";
//
//
//
//
//
//        try (FileOutputStream fos = new FileOutputStream(fileName)) {
//            outputStream.writeTo(fos);
//            System.out.println("Файл успешно сохранен: ");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public ResponseEntity<Resource> downloadFile(String fileId) throws IOException, GeneralSecurityException {
        // Загрузите файл с помощью вашей службы
        Drive service = getInstance();

        System.out.println(service.about().toString());

        log.info("DRIVE-SERVICE ----->" + service.about().get().toString());

        File file = service.files().get(fileId).execute();

        // Определите MIME-тип файла (например, image/jpeg для JPEG-файлов)
        MediaType mediaType = MediaType.parseMediaType("image/jpeg");

        // Создайте ресурс из файла
        InputStream inputStream = service.files().get(fileId).executeMediaAsInputStream();
        byte[] fileBytes = IOUtils.toByteArray(inputStream);
        ByteArrayResource resource = new ByteArrayResource(fileBytes);

        log.info("RESOURCES ----->" + resource.getDescription());

        // Настройте заголовки для браузера
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("attachment", file.getName());

        // Отправьте файл как ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}



