package com.cofetarie.controller;

import com.cofetarie.dto.CartResponseDto;
import com.cofetarie.model.Cart;
import com.cofetarie.model.User;
import com.cofetarie.service.CartService;
import com.cofetarie.service.OrderService;
import com.cofetarie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping
    public CartResponseDto getCart(Authentication auth) {

        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartService.getCartByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        double total = cart.getItems().stream()
                .mapToDouble(ci -> ci.getProduct().getPrice() * ci.getQuantity())
                .sum();

        return new CartResponseDto(cart.getItems(), total);
    }

    @PostMapping("/add/{productId}")
    public CartResponseDto addProductToCart(@PathVariable Long productId,
                                            Authentication auth) {

        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartService.addProductToCart(user, productId);

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();
        return new CartResponseDto(cart.getItems(), total);
    }


    @DeleteMapping("/remove/{productId}")
    public Cart removeItem(@PathVariable Long productId,
                           Authentication auth) {
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartService.removeProductFromCart(user, productId);
    }

    @PutMapping("/update/{productId}")
    public CartResponseDto updateProductQuantityPathVar(
            Authentication auth,
            @PathVariable("productId") Long productId,
            @RequestParam("quantity") int quantity
    ) {
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart updatedCart;
        if (quantity == 0) {
            updatedCart = cartService.removeProductFromCart(user, productId);
        } else {
            updatedCart = cartService.updateProductQuantity(user, productId, quantity);
        }

        double total = updatedCart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        return new CartResponseDto(updatedCart.getItems(), total);
    }


    @DeleteMapping("/{username}/clear")
    public Cart clearCart(@PathVariable String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartService.clearCart(user);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(Authentication authentication, Principal principal) {
        String username = authentication.getName();
        orderService.placeOrder(username);
        messagingTemplate.convertAndSend("/topic/orders",
                "Comandă nouă de la " + principal.getName());
        return ResponseEntity.ok("Comanda a fost plasată cu succes!");
    }

}
