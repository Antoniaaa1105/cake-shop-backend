package com.cofetarie.service;

import com.cofetarie.model.*;
import com.cofetarie.repository.CartItemRepository;
import com.cofetarie.repository.CartRepository;
import com.cofetarie.repository.OrderRepository;
import com.cofetarie.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Optional<Cart> getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart addProductToCart(User user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        cart.addItem(product);
        return cartRepository.save(cart);
    }


    @Transactional
    public Cart updateProductQuantity(User user, Long productId, int quantity) {

        if (quantity < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cantitatea trebuie să fie ≥ 1");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Coșul nu există"));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new EntityNotFoundException("Produsul nu există în coș"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        recalcTotal(cart);
        return cart;
    }

    @Transactional
    public Cart removeProductFromCart(User user, Long productId) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Coșul nu există"));

        CartItem toRemove = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Produsul nu e în coș"));

        cart.getItems().remove(toRemove);

        recalcTotal(cart);
        return cart;
    }


    private void recalcTotal(Cart cart) {
        int total = cart.getItems().stream()
                .mapToInt(ci -> (int) (ci.getQuantity() * ci.getProduct().getPrice()))
                .sum();
        cart.setTotal(total);
    }


    public Cart clearCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user " + user.getUsername()));

        cart.getItems().clear();

        return cartRepository.save(cart);
    }

}