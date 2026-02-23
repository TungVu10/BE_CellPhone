package com.example.Backend_web.repository;

import com.example.Backend_web.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // Tìm 1 category theo slug
    Optional<Category> findBySlug(String slug);

    //  Lấy tất cả danh mục cha (parent = null)
    List<Category> findByParentIsNull();

    // Lấy tất cả danh mục con của 1 danh mục cha
    List<Category> findByParent_CategoryId(Integer parentId);

    // Tìm tất cả danh mục theo trạng thái (active/inactive)
    List<Category> findByStatus(Boolean status);

    @Query("""
SELECT c FROM Category c
WHERE c.parent.categoryId = :parentId
""")
    List<Category> findByParentId(Integer parentId);


}
