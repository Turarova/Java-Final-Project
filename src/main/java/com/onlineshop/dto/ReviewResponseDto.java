// Nurkamila

package com.onlineshop.dto;

import lombok.Data;

@Data
public class ReviewResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productDescription;
    private String productPhotoUrl;

    private Long userId;
    private String userEmail;

    private String text;
    private Integer rating;
}