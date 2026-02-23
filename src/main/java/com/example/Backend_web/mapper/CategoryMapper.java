package com.example.Backend_web.mapper;


import com.example.Backend_web.dto.response.CategoryResponse;
import com.example.Backend_web.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "categoryName", source = "name")
    @Mapping(target = "categorySlug", source = "slug")
    //@Mapping(target = "parentId", expression = "java(category.getParent() != null ? category.getParent().getCategoryId() : null)")
    @Mapping(target = "parent", expression = "java(mapParent(category))")
    CategoryResponse toCategoryResponse(Category category);

    default CategoryResponse mapParent(Category category) {
        if (category.getParent() == null) return null;

        Category parent = category.getParent();
        CategoryResponse parentRes = new CategoryResponse();
        parentRes.setCategoryId(parent.getCategoryId());
        parentRes.setCategoryName(parent.getName());
        parentRes.setCategorySlug(parent.getSlug());

        return parentRes;
    }
}

