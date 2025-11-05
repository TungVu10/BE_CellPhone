package com.example.Backend_web.controller;

import com.example.Backend_web.entity.Color;
import com.example.Backend_web.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/colors")
@CrossOrigin(origins = "*") // cho phép gọi từ mọi domain
public class ColorController {

    @Autowired
    private ColorService colorService;

    // Lấy tất cả màu
    @GetMapping
    public List<Color> getAllColors() {
        return colorService.getAllColors();
    }

    // Lấy màu theo ID
    @GetMapping("/{id}")
    public Optional<Color> getColorById(@PathVariable Integer id) {
        return colorService.getColorById(id);
    }

    // Thêm màu mới
    @PostMapping
    public Color createColor(@RequestBody Color color) {
        return colorService.createColor(color);
    }

    // Cập nhật màu
    @PutMapping("/{id}")
    public Color updateColor(@PathVariable Integer id, @RequestBody Color color) {
        return colorService.updateColor(id, color);
    }

    // Xóa màu
    @DeleteMapping("/{id}")
    public void deleteColor(@PathVariable Integer id) {
        colorService.deleteColor(id);
    }
}
