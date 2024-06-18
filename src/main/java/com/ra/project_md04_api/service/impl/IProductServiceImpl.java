package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.model.dto.request.FormAddProduct;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.entity.*;
import com.ra.project_md04_api.repository.IOrderDetailRepository;
import com.ra.project_md04_api.repository.IOrderRepository;
import com.ra.project_md04_api.repository.IProductRepository;
import com.ra.project_md04_api.repository.IWishListRepository;
import com.ra.project_md04_api.service.ICategoryService;
import com.ra.project_md04_api.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IProductServiceImpl implements IProductService {
    private final IProductRepository productRepository;
    private final ICategoryService categoryService;
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderRepository orderRepository;
    private final IWishListRepository wishListRepository;

    @Override
    public Product getProductById(Long proId) {
        return productRepository.findById(proId).orElseThrow(() -> new RuntimeException("Product not found" + proId));
    }

    @Override
    public Product addProduct(FormAddProduct formAddProduct) {
        Product product = Product.builder()
                .productName(formAddProduct.getProductName())
                .sku(UUID.randomUUID().toString())
                .image(formAddProduct.getImage())
                .description(formAddProduct.getDescription())
                .stockQuantity(formAddProduct.getStockQuantity())
                .unitPrice(formAddProduct.getUnitPrice())
                .category(categoryService.getCategoryById(formAddProduct.getCategoryId()))
                .createdAt(new Date())
                .updateAt(null)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(FormAddProduct formAddProduct, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found" + productId));

        product.setProductName(formAddProduct.getProductName());
        product.setDescription(formAddProduct.getDescription());
        product.setStockQuantity(formAddProduct.getStockQuantity());
        product.setUnitPrice(formAddProduct.getUnitPrice());
        product.setCategory(categoryService.getCategoryById(formAddProduct.getCategoryId()));
        product.setUpdateAt(new Date());

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long proId) {
        productRepository.findById(proId).orElseThrow(() -> new RuntimeException("Product not found" + proId));
        productRepository.deleteById(proId);
    }

    @Override
    public Page<Product> getProductPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction) {
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
            return productRepository.findProductByProductNameAndSorting(searchName, pageable);
        } else {
            //khong tim kiem
            return productRepository.findAll(pageable);
        }
    }

    @Override
    public List<Product> findProductByProductNameAndDescription(String keyword) {
        List<Product> products = productRepository.findProductByProductNameAndDescription(keyword);
        if (products.isEmpty()) {
            log.error("Product not found by keyword: {}", keyword);
        }
        return products;
    }

    @Override
    public List<Product> findProductByCategoryCategoryId(Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            log.error("Category not found by categoryId: {}", categoryId);
            return Collections.emptyList();
        } else {
            if (!category.getStatus()) {
                log.error("Category status is not correct");
            } else {
                return productRepository.findProductByCategoryCategoryId(categoryId);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Page<Product> getProductsIsSalePaging(Integer page, Integer perPage, String orderBy, String direction) {
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

        return productRepository.findProductsIsSaleAndSorting(pageable);
    }

    @Override
    public List<Product> getNewProduct() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new NoSuchElementException("Product data is empty");
        }
        Comparator<Product> byCreatedAtDesc = Comparator.comparing(Product::getCreatedAt).reversed();
        return products
                .stream()
                .sorted(byCreatedAtDesc)
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getBestSellerProducts() {
        //lay tat ca order detail
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        // dem so luong ban
        Map<Product, Integer> productSalesMap = orderDetails.stream()
                .collect(Collectors.groupingBy(OrderDetail::getProduct,
                        Collectors.summingInt(OrderDetail::getOrderQuantity)));

        // sap xep lai theo so luong ban
        return productSalesMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getFeaturedProducts() {
        //lay tat ca
        List<WishList> wishLists = wishListRepository.findAll();

        // dem so luong yeu thich
        Map<Product, Long> featuredProductsMap = wishLists.stream()
                .collect(Collectors.groupingBy(WishList::getProduct, Collectors.counting()));

        // sap xep lai theo so luong yeu thich
        return featuredProductsMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

    }

    @Override
    public List<Product> getBestSellerProductsFromAndTo(RevenueRequest revenueRequest) {
        //lay order theo khoang thoi gian
        List<Order> orders = orderRepository.findAllByCreatedAtBetween(revenueRequest.getFrom(), revenueRequest.getTo());

        if (orders.isEmpty()) {
            throw new NoSuchElementException("No orders found within the specified time range");
        } else {
            //lay ra danh sach Order Detail cua Order
            List<OrderDetail> orderDetails = orders.stream()
                    .flatMap(order -> orderDetailRepository.findAllByOrderId(order.getOrderId()).stream())
                    .toList();

            //dem do luong da ban cua tung product
            Map<Product, Long> productSalesMap = orderDetails.stream()
                    .collect(Collectors.groupingBy(OrderDetail::getProduct, Collectors.summingLong(OrderDetail::getOrderQuantity)));

            // sap xep lai theo so luong ban
            return productSalesMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(10)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }
}
