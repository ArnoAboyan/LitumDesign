package com.litumdesign.LitumDesign.googledrive;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.http.HttpTransportFactory;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.litumdesign.LitumDesign.service.ProductEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
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

    static InputStream inputStream = GoogleDriveService.class.getResourceAsStream("/litumdesign-398209-83c385017db8.json");

    public static Drive getInstance() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(ServiceAccountCredentials.fromStream(inputStream)
            .createScoped(SCOPES));
    Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
            .setApplicationName(APPLICATION_NAME)
            .build();
        return service;
    }


//    public static Drive getInstance() throws GeneralSecurityException, IOException {
//        // Build a new authorized API client service.
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(ServiceAccountCredentials.fromPkcs8("113588968404965585121","litum-477@litumdesign-398209.iam.gserviceaccount.com","-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCWMeAQSFWc1P9J\nldhdQBRlKIPg8Kzge8A36545P0ImgSgMY/K+3H+Qiu7BehjbJt/fZGbKPazUGOuy\n2K/5XU+XHpJh81VunSyk6DN0lHZnU0F1ftFf+FPeHpfG+ROLAr16U9r8qvrfKbHk\nUrk0smmKCkhyDN16kVpPY9sHHZsb5uL4QsSyeVwNeohbQEXVQyGdXutx+ziu9XIp\nbbJ866ciCOh6Vhm8S8rT+MyzQrzScQSslHF3yt1/GN8GQz0KU12c09sbBNJKBqft\n9cYC/LDH3qWs1F9hKP8QxGe88V9KqkP+uIi1Y32sR4M3b6q+zCSJ6iQqKnCKPGm4\nsig0EQqlAgMBAAECggEAATaN6hPA0pogexEBb7S2ICBfpaV2qql/4p5okhXMtvrm\nvpLgFw043+CqD63+HWcG+oruZEZzQD34nrfZVAOeQkjg7cxFWGC0nPBd2gcznZ9d\nMVHA5ywzoTZQti601bPuoS+kM9OTCye5uW+28edZqt/CY17EkITesd118MIbH/KO\nSKmCqW31fcGx2QFm03ufORf5pO4McslgAzpjl35P8yf5C4LhuUqc++aQS/2GeAd3\nMPrYpa4kV113ZlTZTY1ZZgZcDtVBXnQi+avZpXebWZjFXuJOabHeh1FXSxKCs09m\nttkLopfhE+YbVQFs1B2UZ5RtNvsb4y0aFwkBQeOG0QKBgQDR0EtyELuHg/Ez3TYs\nePR/WjEX6D2wUfaeLHWK01L4nyN0tOB5TczRTelXLNNWtQY0zSyjl+TzGGnYjTsq\nl4HKQClLgEDpTbPVPHuiK0HE5M0Oqk8qHaYkNAemDb/wlnoCthKK5wZB5/UTgj1O\nirgForQC3fKhrQEiEStXeV6iDwKBgQC3Qdvr+H6DbOmaHlJm9T00S04InysiEz8/\nYlY6GOCHHGecpUDMMlz80l6tri251jHMbt0FuID9BHBtvo264Re5tcVIGISeU0sJ\n62+B/xqbWF7TJX9sEwMRvn48ANwZ/p4wEz5C+nKsE2Lt4VMBLmZof6K5HuxDw5q/\nhxPknAKsCwKBgQCRRbjHTMCbjwa2E2eMGV+owgkV9Y6pA8dPM5dbPQDI9l71UrvO\nIck77scuvfHCqmqWqq1KxEQY8YFwGDGsRuTt5cjx5uaK42strMgpu+u46Xb1vYQ8\ns/SyQng9SG3qpjXqpKhoFpflkDbHgDj0peXOuR4MkiKKQSB0txCca4PTjwKBgBGu\n9T3L+MlN1SzeCC2PNJy7N4h1c4ASQ2QmZROuGtxuzUC8YYhSw85Rrnkr+ZWZKa0E\ndad7qla/+ulcHekOl2+ALs1KMGHmv7LxWQ0+RJ8A5AWT9LvFCeLIcv3vR03q1/Dj\nqnpkGHSqWj2CJLRUeeVTi6w5Y6sWUPRyYGUK+OcJAoGAftjnjcNe3vDnBqIeZ9kB\n0MwocTRguf7y0mLDjJ8yUp/myOMS3xI7ZPNSYmkahh3zfXX5LPWNScPd/niv75yZ\nz974P9PBWhC6DxqSgJ8gYmJG6Fa6ztTV0f5hmWCtRgcO38iwmEAE9GXrYEhpxOrS\nRXtMXJ7oPJ2/1v9nOItPCXM=\n-----END PRIVATE KEY-----\n",
//                "83c385017db891aeaeb7bc73fd3372b96b18f026",SCOPES,SCOPES,HttpTransportFactory.,"https://oauth2.googleapis.com/token","https://www.googleapis.com/robot/v1/metadata/x509/litum-477%40litumdesign-398209.iam.gserviceaccount.com")
//                .createScoped(SCOPES));
//        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//        return service;
//    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
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



