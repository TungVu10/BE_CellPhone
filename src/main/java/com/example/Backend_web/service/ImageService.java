package com.example.Backend_web.service;

import com.example.Backend_web.dto.response.VariantImageResponse;
import com.example.Backend_web.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

//    private final String basePath = "D:/FileAnh_DoAn/"; // thư mục chứa ảnh
//    private final ProductImageRepository productImageRepository;
//
//    public byte[] getImage(String fileName) throws IOException {
//        Path path = Paths.get(basePath, fileName); // dùng Paths.get(basePath, fileName) an toàn hơn
//        return Files.readAllBytes(path);
//    }
//
//    public Resource getImageAsResource(String fileName) throws MalformedURLException {
//        Path path = Paths.get(basePath, fileName);
//        if (!Files.exists(path)) {
//            throw new RuntimeException("File not found: " + fileName);
//        }
//        return new UrlResource(path.toUri());
//    }

    private static final String basePath = "D:/ServerImages/";
    private final ProductImageRepository productImageRepository;

    public Resource getImageAsResource(String fileName) throws MalformedURLException {
        Path path = Paths.get(basePath).resolve(fileName).normalize();
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + fileName);
        }
        return new UrlResource(path.toUri());
    }

    //Lấy danh sách tất cả ảnh Sản phẩm
//    public List<VariantImageResponse> getImagesByVariant(Integer variantId){
//        return productImageRepository
//                .findByVariant_VariantId(variantId)
//                .stream()
//                .map(img -> new VariantImageResponse(
//                        img.getId(),
//                        img.getImageUrl()
//                ))
//                .toList();
//    }

    public List<VariantImageResponse> getImagesByProduct(Integer productId) {
        return productImageRepository
                .findByVariant_Product_ProductId(productId)
                .stream()
                .map(img -> new VariantImageResponse(
                        img.getId(),
                        img.getImageUrl(),
                        img.getVariant().getVariantId()
                ))
                .toList();
    }

}


