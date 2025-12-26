package com.onlineshop.controller;

import com.onlineshop.entity.User;
import com.onlineshop.service.CartService;
import com.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<CartItemResponseDto> addToCart(Authentication authentication,
                                                         @RequestParam Long productId,
                                                         @RequestParam Integer quantity) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem saved = cartService.addToCart(user.getId(), productId, quantity);

        return ResponseEntity.ok(toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponseDto>> getCart(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> items = cartService.getCartItems(user.getId());
        List<CartItemResponseDto> dtos = items.stream().map(this::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    // Вспомогательный метод
    private CartItemResponseDto toDto(CartItem item) {
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductPrice(item.getProduct().getPrice());
        dto.setProductPhotoUrl(item.getProduct().getPhotoUrl());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getProduct().getPrice() * item.getQuantity());
        return dto;
    }

    @DeleteMapping("/remove")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeFromCart(Authentication authentication, @RequestParam Long productId) {
        String email = authentication.getName();
        com.onlineshop.entity.User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartService.removeFromCart(user.getId(), productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/total")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Double> getCartTotal(Authentication authentication) {
        String email = authentication.getName();
        com.onlineshop.entity.User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(cartService.getCartTotal(user.getId()));
    }
}