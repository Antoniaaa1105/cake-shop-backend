package com.cofetarie.service;

import com.cofetarie.dto.NotificationRequest;
import com.cofetarie.dto.OrderDto;
import com.cofetarie.model.*;
import com.cofetarie.repository.CartRepository;
import com.cofetarie.repository.OrderRepository;
import com.cofetarie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderDto::new)
                .toList();
    }
    public void deleteOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void placeOrder(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setItems(new ArrayList<>());

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.getItems().add(orderItem);
        }

        orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
