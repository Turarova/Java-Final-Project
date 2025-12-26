package com.onlineshop.controller;

import com.onlineshop.dto.ProductDto;
import com.onlineshop.dto.ProductListDto;
import com.onlineshop.entity.Category;
import com.onlineshop.entity.Product;
import com.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setPhotoUrl(dto.getPhotoUrl());

        if (dto.getCategoryId() != null) {
            Category category = productService.getCategoryById(dto.getCategoryId());
            product.setCategory(category);
        }

        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        Product updated = new Product();
        updated.setName(dto.getName());
        updated.setPrice(dto.getPrice());
        updated.setDescription(dto.getDescription());
        updated.setPhotoUrl(dto.getPhotoUrl());

        if (dto.getCategoryId() != null) {
            Category category = productService.getCategoryById(dto.getCategoryId());
            updated.setCategory(category);
        }

        return ResponseEntity.ok(productService.updateProduct(id, updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductListDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getAllProducts(pageable);

        Page<ProductListDto> dtoPage = products.map(p -> {
            ProductListDto dto = new ProductListDto();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setPrice(p.getPrice());
            dto.setDescription(p.getDescription());
            dto.setPhotoUrl(p.getPhotoUrl());
            if (p.getCategory() != null) {
                dto.setCategoryName(p.getCategory().getName());
            }
            return dto;
        });

        return ResponseEntity.ok(dtoPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(productService.createCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.searchProducts(name, categoryId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(products);
    }
}