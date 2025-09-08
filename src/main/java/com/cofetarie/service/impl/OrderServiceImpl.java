package com.cofetarie.service.impl;

import com.cofetarie.dto.OrderDto;
import com.cofetarie.model.Order;
import com.cofetarie.repository.OrderRepository;
import com.cofetarie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends OrderService {
    @Autowired
    private OrderRepository orderRepository;


    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
