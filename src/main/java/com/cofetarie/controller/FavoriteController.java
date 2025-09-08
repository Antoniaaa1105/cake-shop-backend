package com.cofetarie.controller;

import com.cofetarie.dto.FavoriteDto;
import com.cofetarie.model.User;
import com.cofetarie.service.FavoriteService;
import com.cofetarie.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService service;
    private final UserService userService;

    public FavoriteController(FavoriteService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public List<FavoriteDto> getFavorites(Authentication auth) {
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return service.getFavorites(user).stream().map(FavoriteDto::from).toList();
    }

    @PostMapping("/add/{productId}")
    public List<FavoriteDto> add(@PathVariable Long productId, Authentication auth) {
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return service.addFavorite(user, productId).stream().map(FavoriteDto::from).toList();
    }

    @DeleteMapping("/remove/{productId}")
    public List<FavoriteDto> remove(@PathVariable Long productId, Authentication auth) {
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return service.removeFavorite(user, productId).stream().map(FavoriteDto::from).toList();
    }
}
