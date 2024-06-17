package com.ra.project_md04_api.controller.admin;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.model.entity.Category;
import com.ra.project_md04_api.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestBody String searchName, Integer page, Integer perPage, String orderBy, String direction) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(categoryService.getCategoryPaging(searchName, page, perPage, orderBy, direction))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(categoryService.getCategoryById(categoryId))
                        .build()
                , HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertCategory(@RequestBody Category category) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(categoryService.addCategory(category))
                        .build()
                , HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(categoryService.updateCategory(category))
                        .build()
                , HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data("Delete successfully")
                        .build()
                , HttpStatus.OK);
    }
}
