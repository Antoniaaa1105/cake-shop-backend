package com.cofetarie.service.impl;

import com.cofetarie.model.Cart;
import com.cofetarie.repository.CartRepository;
import com.cofetarie.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl extends CartService {
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }


    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }


    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }
}
