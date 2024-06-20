package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.OrderStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormAddProduct;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.dto.response.FeaturedProductsResponse;
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
import org.springframework.http.HttpStatus;
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
    public Product getProductById(Long proId) throws CustomException {
        return productRepository.findById(proId)
                .orElseThrow(() -> new CustomException("Product not found with id: " + proId, HttpStatus.NOT_FOUND));
    }

    @Override
    public Product addProduct(FormAddProduct formAddProduct) throws CustomException {
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
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    public Product updateProduct(FormAddProduct formAddProduct, Long productId) throws CustomException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found" + productId, HttpStatus.NOT_FOUND));

        boolean isNameExist = productRepository.findAll().stream().anyMatch(p -> p.getProductName().equals(formAddProduct.getProductName()));

        if (isNameExist) {
            throw new CustomException("Product name already exist", HttpStatus.CONFLICT);
        }

        Category category = categoryService.getCategoryById(formAddProduct.getCategoryId());
        product.setProductId(productId);
        product.setProductName(formAddProduct.getProductName());
        product.setDescription(formAddProduct.getDescription());
        product.setStockQuantity(formAddProduct.getStockQuantity());
        product.setUnitPrice(formAddProduct.getUnitPrice());
        product.setCategory(category);
        product.setUpdateAt(new Date());

        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    public void deleteProduct(Long proId) throws CustomException {
        productRepository.findById(proId)
                .orElseThrow(() -> new CustomException("Product not found: " + proId, HttpStatus.NOT_FOUND));
        try {
            productRepository.deleteById(proId);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
        }
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
    public List<Product> findProductByProductNameAndDescription(String keyword) throws CustomException {
        try {
            return productRepository.findProductByProductNameAndDescription(keyword);
        } catch (NullPointerException e) {
            throw new CustomException("Product not found by keyword: " + keyword, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Product> findProductByCategoryCategoryId(Long categoryId) throws CustomException {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            throw new CustomException("Category not found by categoryId:  {}" + categoryId, HttpStatus.NOT_FOUND);
        } else {
            if (!category.getStatus()) {
                throw new CustomException("Category status is not correct", HttpStatus.BAD_REQUEST);
            } else {
                return productRepository.findProductByCategoryCategoryId(categoryId);
            }
        }
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
    public List<Product> getNewProduct() throws CustomException {
        List<Product> products;
        try {
            products = productRepository.findAll();
        } catch (NullPointerException e) {
            throw new CustomException("Product data is empty", HttpStatus.NOT_FOUND);
        }
        Comparator<Product> byCreatedAtDesc = Comparator.comparing(Product::getCreatedAt).reversed();
        return products
                .stream()
                .sorted(byCreatedAtDesc)
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getBestSellerProducts() throws CustomException {
        //Lay tat ca order da thanh cong
        List<Order> successfulOrders;
        try {
            successfulOrders = orderRepository.findAll()
                    .stream()
                    .filter(order -> order.getOrderStatus().equals(OrderStatus.SUCCESS))
                    .toList();
        } catch (NullPointerException e) {
            throw new CustomException("No such successful order!", HttpStatus.NOT_FOUND);
        }

        //lay tat ca order detail
        List<OrderDetail> orderDetails = successfulOrders.stream()
                .flatMap(order -> orderDetailRepository.findAllByOrderId(order.getOrderId())
                        .stream())
                .toList();

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
    public List<FeaturedProductsResponse> getFeaturedProducts() throws CustomException {
        //lay tat ca
        List<WishList> wishLists;
        try {
            wishLists = wishListRepository.findAll();
        } catch (NullPointerException e) {
            throw new CustomException("No such wish list!", HttpStatus.NOT_FOUND);
        }

        // dem so luong yeu thich
        Map<Product, Long> featuredProductsMap = wishLists.stream()
                .collect(Collectors.groupingBy(WishList::getProduct, Collectors.counting()));

        // sap xep lai theo so luong yeu thich
        List<Product> featuredProducts = featuredProductsMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();

        return featuredProducts.stream()
                .map(product -> FeaturedProductsResponse.builder()
                        .productId(product.getProductId())
                        .sku(product.getSku())
                        .productName(product.getProductName())
                        .description(product.getDescription())
                        .unitPrice(product.getUnitPrice())
                        .stockQuantity(product.getStockQuantity())
                        .image(product.getImage())
                        .categoryName(product.getCategory().getCategoryName())
                        .createdAt(product.getCreatedAt())
                        .updateAt(product.getUpdateAt())
                        .wishlistNumber(featuredProductsMap.get(product).intValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getBestSellerProductsFromAndTo(RevenueRequest revenueRequest) throws CustomException {
        Date fromDate = revenueRequest.getFrom();
        Date toDate = revenueRequest.getTo();
        if (fromDate.after(toDate)) {
            throw new CustomException("Invalid date range: 'from' date must be before 'to' date.", HttpStatus.BAD_REQUEST);
        }
        //lay order theo khoang thoi gian
        List<Order> orders = orderRepository.findAllByCreatedAtBetween(fromDate, toDate)
                .stream()
                .filter(order -> order.getOrderStatus().equals(OrderStatus.SUCCESS))
                .toList();

        if (orders.isEmpty()) {
            throw new CustomException("No orders found within the specified time range", HttpStatus.NOT_FOUND);
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
