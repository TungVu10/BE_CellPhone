package com.example.Backend_web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final String basePath = "D:/FileAnh_DoAn/"; // thư mục chứa ảnh

    public byte[] getImage(String fileName) throws IOException {
        Path path = Paths.get(basePath, fileName); // dùng Paths.get(basePath, fileName) an toàn hơn
        return Files.readAllBytes(path);
    }

    public Resource getImageAsResource(String fileName) throws MalformedURLException {
        Path path = Paths.get(basePath, fileName);
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + fileName);
        }
        return new UrlResource(path.toUri());
    }
}


