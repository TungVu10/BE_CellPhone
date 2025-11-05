package com.example.Backend_web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "colors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer colorId;

    @Column(nullable = false, length = 50)
    private String name; // Tên màu: "Đen", "Trắng"

    @Column(length = 7)
    private String hexCode; // Mã màu HEX: "#000000"

    @Column
    private Boolean status = true;
}
