package com.litumdesign.LitumDesign.googledrive;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.Entity.ProductPhotoEntity;
import com.litumdesign.LitumDesign.Entity.UserEntity;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE);


    public Drive getInstance() throws GeneralSecurityException, IOException {


        String clientId = System.getenv("CLIENT_ID");
        String clientMail = System.getenv("CLIENT_EMAIL");
        String privateKey = System.getenv("CLIENT_PRIVATEKEY");
        String privateId = System.getenv("CLIENT_PRIVATEID");





        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(ServiceAccountCredentials.fromPkcs8(
                clientId, clientMail, privateKey, privateId, SCOPES).createScoped(SCOPES));

        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public void main(String... args) throws IOException, GeneralSecurityException {
//        Drive service = getInstance();


    }


    public String uploadFile(MultipartFile file) {


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




//            System.out.println("GDFILEID ->>> " + uploadFile.getName());
//            System.out.println("GDFILEID ->>> " + uploadFile.getId());

            return uploadFile.getId();

        } catch (Exception e) {
            System.out.printf("Error: " + e);
        }
        return null;
    }


    public List<ProductPhotoEntity> uploadProductPhotos(ProductEntity productEntity, List<MultipartFile> photos) {
//        List<String> uploadedFileIds = new ArrayList<>();
        List<ProductPhotoEntity> productPhotos = productEntity.getPhotoLink();

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



                productPhotos.add(new ProductPhotoEntity(productEntity, uploadFile.getId()));
                System.out.println("PHOTO_LIST -> " + productPhotos);
            }

        } catch (Exception e) {
            log.error("Error during file upload process", e);
        }
        return productPhotos;
    }
    public ProductEntity uploadMainProductPhotos(ProductEntity productEntity, MultipartFile photo) {

        try {
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

                productEntity.setTitleImageLink(uploadFile.getId());

        } catch (Exception e) {
            log.error("Error during file upload process", e);
        }
        return productEntity;
    }

    public UserEntity uploadUserAvatar(UserEntity userEntity, MultipartFile photo) {

        try {

            log.info("Uploading photo: {}", photo.getOriginalFilename());

            File fileMetadata = new File();
            fileMetadata.setName("Avatar " + userEntity.getName() + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy | HH:mm:ss")));
            fileMetadata.setParents(Collections.singletonList("1QpfYnVHb-qJlglXUoA9alEFNvLT765SW"));
            File uploadFile = getInstance()
                    .files()
                    .create(fileMetadata, new InputStreamContent(
                            photo.getContentType(),
                            new ByteArrayInputStream(photo.getBytes()))
                    )
                    .setFields("id, name")
                    .execute();
            System.out.println("FILE CREATE -> " + uploadFile);

            userEntity.setImageUrl(uploadFile.getId());

        } catch (Exception e) {
            log.error("Error during file upload process", e);
        }
        return userEntity;
    }


    public void uploadUserBanner(UserEntity userEntity, MultipartFile photo) {

        try {

            log.info("Uploading photo: {}", photo.getOriginalFilename());

            File fileMetadata = new File();
            fileMetadata.setName("Banner " + userEntity.getName() + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy | HH:mm:ss")));
            fileMetadata.setParents(Collections.singletonList("1dj7SYK4WfWjVoJSQWW-D0_s5jCgH-nhz"));
            File uploadFile = getInstance()
                    .files()
                    .create(fileMetadata, new InputStreamContent(
                            photo.getContentType(),
                            new ByteArrayInputStream(photo.getBytes()))
                    )
                    .setFields("id, name")
                    .execute();
            System.out.println("FILE CREATE -> " + uploadFile);

            userEntity.setBanner(uploadFile.getId());

        } catch (Exception e) {
            log.error("Error during file upload process", e);
        }
    }

//    public String getAllAudio() throws IOException, GeneralSecurityException {
//        Drive service = getInstance();
//
//
//        try {
//            FileList result = service.files().list()
//                    .setPageSize(10)
//                    .setFields("nextPageToken, files(id, name)")
//                    .execute();
//            System.out.println(result.getFiles().toString());
//            return result.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

//    public Drive.Files.Get getFileById(String fileId) throws IOException, GeneralSecurityException {
//        Drive service = getInstance();
//
//        try {
//
//            Drive.Files.Get result = service.files().get(fileId);
//
//            System.out.println(result.toString());
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

//    public static ByteArrayOutputStream getFile(String realFileId, Drive service) throws IOException {
//        try {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//            service.files().get(realFileId)
//                    .executeMediaAndDownloadTo(outputStream);
//
//            return outputStream;
//        } catch (GoogleJsonResponseException e) {
//            // TODO(developer) - handle error appropriately
//            System.err.println("Unable to move file: " + e.getDetails());
//            throw e;
//        }
//    }


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

        Drive service;
        try {
            service = getInstance();
            service.files().delete(fileId).execute();
            System.out.println("DELETE GD FILE -->" + fileId);
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error occurred while deleting file" + e.getMessage(), e);
        }

    }


//    public void renameFileToUnused(String fileId)  {
//
//        Drive service = null;
//        try {
//            service = getInstance();
//            File file = service.files().get(fileId).execute();
//
//            file.setName("UNUSED");
//
//            service.files().update(fileId, file).execute();
//
//            System.out.println("File has bean rename to UNUSED successful");
//        } catch (GeneralSecurityException | IOException e) {
//            log.error("Error occurred while renaming file"+ e.getMessage(), e);
//        }
//
//    }

}



