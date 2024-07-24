package com.vikram.resqliciousbackend.controller;

import com.stripe.exception.StripeException;
import com.vikram.resqliciousbackend.dto.OrderDTO;
import com.vikram.resqliciousbackend.service.OrderService;
import com.vikram.resqliciousbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService; // Inject PaymentService

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            // Charge the card using the payment token and total price
            paymentService.chargeCard(orderDTO.getPaymentToken(), orderDTO.getTotalPrice());

            // Create the order
            OrderDTO createdOrder = orderService.createOrder(orderDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order creation failed: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrder(id);
        return ResponseEntity.ok(orderDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
