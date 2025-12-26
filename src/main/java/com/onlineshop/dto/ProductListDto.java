package com.onlineshop.dto;

import lombok.Data;

@Data
public class ProductListDto {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String photoUrl;
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}