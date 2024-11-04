package crowdworks.image.api.service.impl;

import crowdworks.image.api.dto.GetImageResponse;

import crowdworks.image.api.service.ImageService;
import crowdworks.image.common.exception.FileServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static crowdworks.image.api.service.ImageExtension.isValidExtension;
import static crowdworks.image.common.Response.ErrorCode.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.s3.path}")
    private String path;

    public List<GetImageResponse> getAllImage() {
        List<GetImageResponse> images = new ArrayList<>();

        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

        for (S3Object obj : listObjectsResponse.contents()) {
                String key = obj.key();
                if (isValidExtension(key)) {
                    String fileName = key.substring(key.lastIndexOf('/') + 1);
                    long sizeInBytes = obj.size();
                    double sizeInKilobytes = sizeInBytes / 1024.0;
                    GetImageResponse response = new GetImageResponse(
                            s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(key).build()).toString(),
                            fileName,
                            sizeInKilobytes,
                            LocalDateTime.ofInstant(Instant.parse(obj.lastModified().toString()), ZoneId.systemDefault())
                    );
                    images.add(response);
                }

        }
        return images;
    }

    public void uploadImage(MultipartFile file, String path) {
        try {
            String fileName = path.isBlank() ? this.path + file.getOriginalFilename() : path + "/" + file.getOriginalFilename();

            validateFile(file);

            if(isFileExist(fileName)) {
                throw new FileServiceException(FILE_NAME_DUPLICATED_ERROR);
            }

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String s3Url = s3Client.utilities().getUrl(r -> r.bucket(bucketName).key(fileName)).toString();
            log.info("filename : {}", file.getOriginalFilename());
            log.info("s3 url : {}", s3Url);
            log.info("time : {}", LocalDateTime.now());

        } catch (IOException e) {
            throw new FileServiceException(FILE_UPLOAD_FAIL_ERROR);
        }

    }

    public void deleteImage(String file, String path) {
        String fileName = path.isBlank() ? this.path + file : path + "/" + file;
        if(!isFileExist(fileName)) {
            throw new FileServiceException(FILE_NOT_FOUND_ERROR);
        }

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public void validateFile(MultipartFile file) {

        if(file.isEmpty()){
            throw new FileServiceException(FILE_EMPTY_ERROR);
        }
        if(!isValidExtension(file.getOriginalFilename())){
            throw new FileServiceException(FILENAME_EXTENSION_ERROR);
        }
        long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new FileServiceException(FILE_SIZE_ERROR);
        }
    }

    public boolean isFileExist(String fileName){
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

}


