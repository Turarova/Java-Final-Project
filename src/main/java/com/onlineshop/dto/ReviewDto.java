package com.onlineshop.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long productId;
    private String text;
    private Integer rating;
}