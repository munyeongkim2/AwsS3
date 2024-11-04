package crowdworks.image.api.controller;

import crowdworks.image.api.dto.GetImageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface ImageController {

    ResponseEntity<List<GetImageResponse>> getAllImage();

    ResponseEntity<Void> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam(value = "path", required = false, defaultValue = "") String path);

    ResponseEntity<Void> deleteImage(@RequestParam(required = false, defaultValue = "") String path,
                                     @RequestParam String file);
}
