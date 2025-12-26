package com.onlineshop.controller;

import com.onlineshop.dto.UserProfileDto;
import com.onlineshop.entity.User;
import com.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserProfileDto dto = new UserProfileDto(user.getEmail(), user.getPhotoUrl());
        dto.setEmail(user.getEmail());
        dto.setPhotoUrl(user.getPhotoUrl());

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(Authentication authentication, @RequestBody UserProfileDto dto) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhotoUrl() != null) {
            user.setPhotoUrl(dto.getPhotoUrl());
        }

        User updated = userService.updateProfile(user.getId(), dto.getEmail(), dto.getPhotoUrl());

        UserProfileDto response = new UserProfileDto();
        response.setEmail(updated.getEmail());
        response.setPhotoUrl(updated.getPhotoUrl());

        return ResponseEntity.ok(updated);
    }
}
