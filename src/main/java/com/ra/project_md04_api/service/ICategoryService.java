package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long cateId);
    Category addCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(Long cateId);
    Page<Category> getCategoryPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction);
    List<Category> findEnableCategory();
}
