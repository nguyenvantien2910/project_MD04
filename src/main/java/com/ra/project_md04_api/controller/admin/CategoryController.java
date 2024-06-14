package com.ra.project_md04_api.controller.admin;

import com.ra.project_md04_api.model.entity.Category;
import com.ra.project_md04_api.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<Category>> getCategories(@RequestBody String searchName, Integer page, Integer perPage, String orderBy, String direction) {
        return new ResponseEntity<>(categoryService.getCategoryPaging(searchName,page,perPage,orderBy,direction), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId){
        return new ResponseEntity<>(categoryService.getCategoryById(categoryId),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> insertCategory(@RequestBody Category category){
        return new ResponseEntity<>(categoryService.addCategory(category),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Category> updateCategory(@RequestBody Category category){
        return new ResponseEntity<>(categoryService.updateCategory(category),HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
