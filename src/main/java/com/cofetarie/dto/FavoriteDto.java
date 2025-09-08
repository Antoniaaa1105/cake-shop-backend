package com.cofetarie.dto;

import com.cofetarie.model.Favorite;
import com.cofetarie.model.Product;

public record FavoriteDto(Long id, Long productId, String name, double price, String image) {
    public static FavoriteDto from(Favorite f) {
        Product p = f.getProduct();
        return new FavoriteDto(f.getId(), p.getId(), p.getName(), p.getPrice(), p.getImage());
    }
}

