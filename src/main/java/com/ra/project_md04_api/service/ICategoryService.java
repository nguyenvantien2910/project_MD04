package com.ra.project_md04_api.service;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.CategoryFromRequest;
import com.ra.project_md04_api.model.dto.response.EnableCategoryResponse;
import com.ra.project_md04_api.model.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long cateId) throws CustomException;
    Category addCategory(CategoryFromRequest categoryFromRequest) throws CustomException;
    Category updateCategory(CategoryFromRequest categoryFromRequest,Long categoryId) throws CustomException;
    void deleteCategory(Long cateId) throws CustomException;
    Page<Category> getCategoryPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction);
    List<EnableCategoryResponse> findEnableCategory();
}
