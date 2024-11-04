package crowdworks.image.api.controller.impl;

import crowdworks.image.api.controller.ImageController;
import crowdworks.image.api.dto.GetImageResponse;
import crowdworks.image.api.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3/images")
public class ImageControllerImpl implements ImageController {

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<List<GetImageResponse>> getAllImage() {
        return ResponseEntity.ok(imageService.getAllImage());
    }

    @PostMapping
    public ResponseEntity<Void> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam(value = "path", required = false, defaultValue = "") String path) {
        imageService.uploadImage(file, path);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestParam(required = false, defaultValue = "") String path, @RequestParam String file) {
        imageService.deleteImage(file,  path);
        return ResponseEntity.ok().build();
    }

}
