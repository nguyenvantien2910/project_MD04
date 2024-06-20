package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.CategoryFromRequest;
import com.ra.project_md04_api.model.dto.response.EnableCategoryResponse;
import com.ra.project_md04_api.model.entity.Category;
import com.ra.project_md04_api.model.entity.Product;
import com.ra.project_md04_api.repository.ICategoryRepository;
import com.ra.project_md04_api.repository.IProductRepository;
import com.ra.project_md04_api.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ICategoryServiceImpl implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;

    @Override
    public Category getCategoryById(Long cateId) throws CustomException {
        return categoryRepository.findById(cateId).orElseThrow(() -> new CustomException("Category not found: " + cateId, HttpStatus.BAD_GATEWAY));
    }

    @Override
    @Transactional
    public Category addCategory(CategoryFromRequest categoryFromRequest) throws CustomException {
        Category category = Category.builder()
                .categoryName(categoryFromRequest.getCategoryName())
                .description(categoryFromRequest.getDescription())
                .status(categoryFromRequest.getStatus())
                .build();
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    @Transactional
    public Category updateCategory(CategoryFromRequest categoryFromRequest, Long categoryId) throws CustomException {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException("Category not found: " + categoryFromRequest.getCategoryName(), HttpStatus.NOT_FOUND));
        Category category = Category.builder()
                .categoryId(categoryId)
                .categoryName(categoryFromRequest.getCategoryName())
                .description(categoryFromRequest.getDescription())
                .status(categoryFromRequest.getStatus())
                .build();
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long cateId) throws CustomException {
        Category category = categoryRepository.findById(cateId).orElseThrow(() -> new CustomException("Category not found: " + cateId, HttpStatus.BAD_REQUEST));
        if (productRepository.findAll().stream().anyMatch(product -> product.getCategory().getCategoryId().equals(cateId))) {
            throw new CustomException("Delete category failed because category has product!", HttpStatus.BAD_REQUEST);
        }
        try {
            category.setStatus(!category.getStatus());
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    public Page<Category> getCategoryPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction) {
        Pageable pageable = null;

        if (orderBy != null && !orderBy.isEmpty()) {
            // co sap xep
            Sort sort = null;
            switch (direction) {
                case "ASC":
                    sort = Sort.by(orderBy).ascending();
                    break;
                case "DESC":
                    sort = Sort.by(orderBy).descending();
                    break;
            }
            pageable = PageRequest.of(page, perPage, sort);
        } else {
            //khong sap xep
            pageable = PageRequest.of(page, perPage);
        }

        //xu ly ve tim kiem
        if (searchName != null && !searchName.isEmpty()) {
            //co tim kiem
            return categoryRepository.findCategoryByCategoryNameAndSorting(searchName, pageable);
        } else {
            //khong tim kiem
            return categoryRepository.findAll(pageable);
        }
    }

    @Override
    public List<EnableCategoryResponse> findEnableCategory() {
        List<Category> categories = categoryRepository.findAll().stream()
                .filter(Category::getStatus)
                .toList();

        if (categories.isEmpty()) {
            log.error("Enable category not found!");
            return Collections.emptyList();
        }

        return categories.stream()
                .map(category -> EnableCategoryResponse.builder()
                        .categoryId(category.getCategoryId())
                        .categoryName(category.getCategoryName())
                        .description(category.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
