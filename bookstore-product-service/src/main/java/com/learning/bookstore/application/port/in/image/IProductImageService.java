package com.learning.bookstore.application.port.in.image;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IProductImageService {
    Map<String, String> uploadImage(MultipartFile file) throws IOException;
    ByteArrayResource getImage(String imageId) throws IOException;
}
