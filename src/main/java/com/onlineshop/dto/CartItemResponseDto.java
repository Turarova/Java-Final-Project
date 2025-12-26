package com.onlineshop.dto;

import lombok.Data;

@Data
public class CartItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productPhotoUrl;
    private Integer quantity;
    private Double subtotal;
}