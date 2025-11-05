package com.example.Backend_web.service;

import com.example.Backend_web.entity.Color;
import com.example.Backend_web.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    // Lấy tất cả màu sắc đang hoạt động
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    // Lấy màu theo ID
    public Optional<Color> getColorById(Integer id) {
        return colorRepository.findById(id);
    }

    // Thêm màu mới
    public Color createColor(Color color) {
        return colorRepository.save(color);
    }

    // Cập nhật màu
    public Color updateColor(Integer id, Color color) {
        color.setColorId(id);
        return colorRepository.save(color);
    }

    // Xóa màu
    public void deleteColor(Integer id) {
        colorRepository.deleteById(id);
    }
}
