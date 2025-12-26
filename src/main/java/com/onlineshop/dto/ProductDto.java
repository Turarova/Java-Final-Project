// Nurkamila

package com.onlineshop.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private Double price;
    private String description;
    private String photoUrl;
    private Long categoryId;
}