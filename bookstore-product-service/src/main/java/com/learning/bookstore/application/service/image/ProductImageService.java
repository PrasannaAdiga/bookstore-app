package com.learning.bookstore.application.service.image;

import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.image.IProductImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class ProductImageService implements IProductImageService {
    @Override
    public Map<String, String> uploadImage(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Files.createDirectories(Paths.get("images"));
        Path path = Paths.get("images/" + uuid.toString() + "__" + fileName);
        Path absolutePath = path.toAbsolutePath();
        try {
            Files.copy(file.getInputStream(), absolutePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("IOException in ProductImageService.uploadImage: e", e.getMessage());
        }
        Map<String, String> response = new HashMap<>();
        response.put("imageId", uuid.toString() + "__" + fileName);
        return response;
    }

    @Override
    public ByteArrayResource getImage(String imageId) throws IOException {
        Optional<Path> images = Files.list(Paths.get("images")).filter(img -> img.getFileName().toString().equals(imageId)).findFirst();
        images.orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductImageService.getImage: Image with Id {} not found", imageId);
            throw new ResourceNotFoundException("Image with id " + imageId + " not found!");
        });
        return new ByteArrayResource(Files.readAllBytes(images.get()));
    }

}
