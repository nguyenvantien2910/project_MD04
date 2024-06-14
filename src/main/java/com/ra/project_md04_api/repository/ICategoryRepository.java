package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICategoryRepository extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {
    @Query("select c from Category c where c.categoryName like concat('%',:categoryName,'%') and c.status = true ")
    Page<Category> findCategoryByCategoryNameAndSorting (String categoryName, Pageable pageable);
}
