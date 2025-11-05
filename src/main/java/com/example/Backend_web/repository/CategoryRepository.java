package com.example.Backend_web.repository;

import com.example.Backend_web.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // 1️⃣ Tìm 1 category theo slug
    Optional<Category> findBySlug(String slug);

    // 2️⃣ Lấy tất cả danh mục cha (parent = null)
    List<Category> findByParentIsNull();

    // 3️⃣ Lấy tất cả danh mục con của 1 danh mục cha
    List<Category> findByParent_CategoryId(Integer parentId);

    // 4️⃣ Tìm tất cả danh mục theo trạng thái (active/inactive)
    List<Category> findByStatus(Boolean status);
}
