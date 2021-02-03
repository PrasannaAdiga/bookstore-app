package com.learning.bookstore.adapter.web.v1.impl;

import com.learning.bookstore.adapter.web.v1.IProductImageController;
import com.learning.bookstore.application.port.in.image.IProductImageService;
import com.learning.bookstore.infrastructure.annotation.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class ProductImageController implements IProductImageController {
    private final IProductImageService productImageService;

    @Override
    public ResponseEntity<?> uploadImage(MultipartFile file) throws IOException {
        return ResponseEntity.ok(productImageService.uploadImage(file));
    }

    @Override
    public ResponseEntity<?> getImage(String id) throws IOException {
        ByteArrayResource inputStream = productImageService.getImage(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }
}
