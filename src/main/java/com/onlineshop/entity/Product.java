package com.onlineshop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String description;
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}