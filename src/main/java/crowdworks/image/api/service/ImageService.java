package crowdworks.image.api.service;

import crowdworks.image.api.dto.GetImageResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface ImageService {
    List<GetImageResponse> getAllImage();
    void uploadImage(MultipartFile file, String path);
    void deleteImage(String file, String path);
    void validateFile(MultipartFile file);
}
