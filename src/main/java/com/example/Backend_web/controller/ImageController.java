package com.example.Backend_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final String UPLOAD_DIR = "D:/ServerImages"; // Thư mục lưu ảnh trên server

    // Upload ảnh
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String url = "http://localhost:8082/api/images/" + fileName; // URL dùng frontend hiển thị

            Map<String, String> res = new HashMap<>();
            res.put("name", fileName);
            //res.put("url", url);

            return ResponseEntity.ok(res);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload thất bại");
        }
    }

    // Lấy ảnh
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) throws MalformedURLException {
        Path path = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Tự động gán MediaType
        String lower = fileName.toLowerCase();
        MediaType mediaType;
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) mediaType = MediaType.IMAGE_JPEG;
        else if (lower.endsWith(".png")) mediaType = MediaType.IMAGE_PNG;
        else if (lower.endsWith(".webp")) mediaType = MediaType.parseMediaType("image/webp");
        else mediaType = MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok().contentType(mediaType).body(resource);
    }
}
