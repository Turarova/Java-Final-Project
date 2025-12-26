// Nurkamila

package com.onlineshop.controller;

import com.onlineshop.dto.ReviewDto;
import com.onlineshop.dto.ReviewResponseDto;
import com.onlineshop.entity.Product;
import com.onlineshop.entity.Review;
import com.onlineshop.service.ProductService;
import com.onlineshop.service.ReviewService;
import com.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponseDto> addReview(Authentication authentication, @RequestBody ReviewDto dto) {
        String email = authentication.getName();
        com.onlineshop.entity.User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review();
        Product product = productService.getProductById(dto.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        review.setProduct(product);
        review.setUser(user);
        review.setText(dto.getText());
        review.setRating(dto.getRating());

        Review savedReview = reviewService.addReview(review);

        // Создаём DTO вручную
        ReviewResponseDto response = new ReviewResponseDto();
        response.setId(savedReview.getId());
        response.setProductId(savedReview.getProduct().getId());
        response.setProductName(savedReview.getProduct().getName());
        response.setProductPrice(savedReview.getProduct().getPrice());
        response.setProductDescription(savedReview.getProduct().getDescription());
        response.setProductPhotoUrl(savedReview.getProduct().getPhotoUrl());

        response.setUserId(savedReview.getUser().getId());
        response.setUserEmail(savedReview.getUser().getEmail());

        response.setText(savedReview.getText());
        response.setRating(savedReview.getRating());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);

        List<ReviewResponseDto> dtos = reviews.stream().map(review -> {
            ReviewResponseDto dto = new ReviewResponseDto();
            dto.setId(review.getId());
            dto.setProductId(review.getProduct().getId());
            dto.setProductName(review.getProduct().getName());
            dto.setProductPrice(review.getProduct().getPrice());
            dto.setProductDescription(review.getProduct().getDescription());
            dto.setProductPhotoUrl(review.getProduct().getPhotoUrl());

            dto.setUserId(review.getUser().getId());
            dto.setUserEmail(review.getUser().getEmail());

            dto.setText(review.getText());
            dto.setRating(review.getRating());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }
}