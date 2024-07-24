package com.vikram.resqliciousbackend.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.vikram.resqliciousbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public ResponseEntity<String> chargeCard(@RequestParam String token, @RequestParam double amount) {
        try {
            Charge charge = paymentService.chargeCard(token, amount);
            return ResponseEntity.ok("Charge successful: " + charge.getId());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Charge failed: " + e.getMessage());
        }
    }
}
