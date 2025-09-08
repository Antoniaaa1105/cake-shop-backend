package com.cofetarie.dto;

import com.cofetarie.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String username;
    private Date orderDate;
    private List<String> items;
    private double total;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.username = order.getUser().getUsername();
        this.orderDate = order.getOrderDate();
        this.items = order.getItems().stream()
                .map(item -> item.getProduct().getName() + " x" + item.getQuantity())
                .toList();
        this.total = order.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
