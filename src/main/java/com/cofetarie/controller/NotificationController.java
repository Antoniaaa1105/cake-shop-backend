package com.cofetarie.controller;

import com.cofetarie.dto.NotificationRequest;
import com.cofetarie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notify")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
@Autowired
private OrderService orderService;

    @PostMapping("/admin")
    public ResponseEntity<Void> notifyAdmin(@RequestBody NotificationRequest notificationRequest) {
        messagingTemplate.convertAndSend("/topic/orders", notificationRequest.getMessage());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{username}")
    public ResponseEntity<?> notifyUser(
            @PathVariable String username,
            @RequestBody Map<String, String> body
    ) {
        String message = body.get("message");
        Long orderId = Long.parseLong(body.get("orderId"));
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
        orderService.deleteOrderById(orderId);
        return ResponseEntity.ok("Notificare trimisă și comanda ștearsă");
    }


}
