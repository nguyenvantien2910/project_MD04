package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.model.entity.Category;
import com.ra.project_md04_api.repository.ICategoryRepository;
import com.ra.project_md04_api.repository.IProductRepository;
import com.ra.project_md04_api.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ICategoryServiceImpl implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;

    @Override
    public Category getCategoryById(Long cateId) {
        return categoryRepository.findById(cateId).orElseThrow(() -> new RuntimeException("Category not found" + cateId));
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        categoryRepository.findById(category.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found" + category.getCategoryName()));
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long cateId) {
        categoryRepository.findById(cateId).orElseThrow(() -> new RuntimeException("Category not found" + cateId));
        if (productRepository.findAll().stream().anyMatch(product -> product.getCategory().getCategoryId().equals(cateId))) {
            log.error("Delete category failed because category has product!");
        }
        categoryRepository.deleteById(cateId);
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
    public List<Category> findEnableCategory() {
        List<Category> categories = categoryRepository.findAll().stream().filter(Category::getStatus).toList();
        if (categories.isEmpty()) {
            log.error("Enable category not found!");
        }
        return categories;
    }
}
