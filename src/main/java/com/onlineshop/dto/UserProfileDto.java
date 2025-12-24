package com.onlineshop.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private String email;
    private String photoUrl;

    public UserProfileDto() {}

    public UserProfileDto(String email, String photoUrl) {
        this.email = email;
        this.photoUrl = photoUrl;
    }
}