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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* class to demonstrate use of Drive files list API */
@Component
@RequiredArgsConstructor
@Log4j2
public class GoogleDriveService {

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


    public Drive getInstance() throws GeneralSecurityException, IOException {


//        String clientId = System.getenv("CLIENT_ID");
//        String clientMail = System.getenv("CLIENT_EMAIL");
//        String privateKey = System.getenv("CLIENT_PRIVATEKEY");
//        String privateId = System.getenv("CLIENT_PRIVATEID");

        String clientId = "113588968404965585121";
        String clientMail = "litum-477@litumdesign-398209.iam.gserviceaccount.com";
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDJxhEg1AR1lVez\n" +
                "1L8WUKbGjhwiKPmgIDJ99Rjpj64fSDn4scZxVG9TLNV+zWN1upFADa7y0/SP9tuI\n" +
                "1jvPbcHGVteovWXBjYeAm5i6jX/vI68yL47UxvguoJt1ljy2G0udMAXYK5HNvr/7\n" +
                "ceX4DvV8QpFbwUtWIMQoXmFzHwLABQw3CuzzH0oFKphc4kbObAMNsQ1ixW3q0bmD\n" +
                "ZbdoDV1cbhJHh9CgTFZQqUezfoV2EvgEKXxXJMy8i0JS5lNNihzODsuU00naCyQF\n" +
                "cHqjkaiAse9lOuRcGidPfc0DYPBpbRFQ6JsWZ0wDuAlEX54cF0FECaK53iyIpiIQ\n" +
                "63zvhkNtAgMBAAECggEAGzB+yeDwGeUuPIwoVMo48wwe0+aYHsD/9NO0j/Kd2NbL\n" +
                "gwz3q/lgCAIEYfIs2yYxIQPHBa9Upxqxghq2VBEa+c5lgLZG8kJPVmABQHBFY1T4\n" +
                "bIeWlGsccfIQCNQeKb77XTrlqrfSTbqM1cYHInpnj59LxaTCMWC4diUjthbhcLzw\n" +
                "HzKxvx8wg7l04Ny1/C7AGYjqOdCDc268UdAV5Io71v1N7cdUDph6pELEKxOOZDrk\n" +
                "KblSjJdNPtvHKyDJMH5G8WoKt5RYkiscgE6TK7gzFeJivuup6K9s9aS+nmDBley/\n" +
                "mNDR6DMHbdiYiSUj2Z6ELA9qpK5B0Vn4KiBGzhchSQKBgQDo/NGTbcwNLzfo/0mP\n" +
                "/e8n97brRlusaG1y+LOo94JEzdLdmwMMSHTaR/gI3CLSY/7wpE4AkY569oY4udwq\n" +
                "8c0gC1BOLDpiHJOzVB1iuwk+cdsho07k0UfXbklIMWA192H8X1uzkFRXSKYT20o3\n" +
                "mBsThfEvGJijcVRgMb2XCON4JQKBgQDds/5JzrIPgh90NrMIScYXzNUwEb0U1F8b\n" +
                "vzPm7OXney/5cRuJxJTxaBKcYfq6edqt4MRsQi6KPMhfRGkeyp5BM/i3NQLo7Z4X\n" +
                "Ox3fQ7m8WFp4d01d0qEoop8XvgbJsBql0PpDJZqb1AkKf97LZLfaU9SOUzeXo9DT\n" +
                "WyQLTJw3qQKBgQC74YxbmBIRm785bAx1SnA+Sbs/VTh9+qQwL7AplJZ/R39A50z4\n" +
                "7cEVeS3ibyrBgJPImKayPK6qJ/yq5+5Mzzr8GUOEFpRqI+NTnIQp5uwYaBYutRPc\n" +
                "34tQhYYg6nhMbJaevoMjdUMeM96BargZxtYm0z9gHDSSsMQ64kGh9y//5QKBgQC2\n" +
                "24xzB95pWCPlUHBd96tBWhrEKP1RPtCrLAr8/oupXzt0e8wA13kZm4MuJMoOIA8z\n" +
                "tt2LhRKpxfRmTyB82xWoJduQEFM0Fzh7ZLEAwvVg2WbVUWEob1+pCmg5xuwGUg/I\n" +
                "kBK6azuYJ0zFsi3qiEnz84yvTwfdNKo6iUOzZh668QKBgEJ2T4lVdkO7YhlzDy0K\n" +
                "N9pM3T63uIFyByBbfe6Fo3JUo2VQWy/yG3fSssyQz5uwmL0w5Su2vhOSkK18AVPX\n" +
                "LhLbpYgQr2JFRag+qQPbw/e73XaE6uvvaALvcNwFwBcV1CXTx+6DrG6Ty7Q0Vjqv\n" +
                "SsV4kah3nti68VVQSDK/Wxvk\n" +
                "-----END PRIVATE KEY-----";
        String privateId = "6601b79b8c09f9da01abd54108aca0d5be737406";




        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(ServiceAccountCredentials.fromPkcs8(
                clientId, clientMail, privateKey, privateId, SCOPES).createScoped(SCOPES));

        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }


    public void main(String... args) throws IOException, GeneralSecurityException {
        Drive service = getInstance();


    }


    @Transactional
    public String uploadFile(MultipartFile file) throws GeneralSecurityException, IOException {


        try {
            System.out.println(file.getOriginalFilename());

            File fileMetadata = new File();
            fileMetadata.setName(file.getOriginalFilename());
            fileMetadata.setParents(Collections.singletonList("1stc_iqTpWk7hJJwn7y95X3DUoonfSMy2"));
            File uploadFile = getInstance()
                    .files()
                    .create(fileMetadata, new InputStreamContent(
                            file.getContentType(),
                            new ByteArrayInputStream(file.getBytes()))
                    )
                    .setFields("id, name")
                    .execute();


            String fileId = uploadFile.getId();

//            System.out.println("GDFILEID ->>> " + uploadFile.getName());
//            System.out.println("GDFILEID ->>> " + uploadFile.getId());

            return uploadFile.getId();

        } catch (Exception e) {
            System.out.printf("Error: " + e);
        }
        return null;
    }


    public List<String> uploadProductPhotos(List<MultipartFile> photos) {
        List<String> uploadedFileIds = new ArrayList<>();

        try {
            for (MultipartFile photo:photos) {

                log.info("Uploading photo: {}", photo.getOriginalFilename());

                File fileMetadata = new File();
                fileMetadata.setName(photo.getOriginalFilename());
                fileMetadata.setParents(Collections.singletonList("1epednn6iQdmuEW7W-uGN3Y-o64HAyuEx"));
                File uploadFile = getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                photo.getContentType(),
                                new ByteArrayInputStream(photo.getBytes()))
                        )
                        .setFields("id, name")
                        .execute();
                    System.out.println("FILE CREATE -> " + uploadFile);

                String photoId = uploadFile.getId();
                uploadedFileIds.add(photoId);

                System.out.println("PHOTOID -> " + photoId);
            };

        } catch (Exception e) {
            log.error("Error during file upload process", e);
        }
        return uploadedFileIds;
    }




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


    public void deleteFile(String fileId)  {

        Drive service = null;
        try {
            service = getInstance();
            service.files().delete(fileId).execute();
            System.out.println("DELETE GD FILE -->" + fileId);
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error occurred while getting the Google Drive instance", e);
            throw new RuntimeException("Error initializing Google Drive service: " + e.getMessage(), e);
        }

    }
}



