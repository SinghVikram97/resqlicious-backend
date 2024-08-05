package com.vikram.resqliciousbackend.controller;

import com.stripe.model.Charge;
import com.vikram.resqliciousbackend.dto.PaymentRequest;
import com.vikram.resqliciousbackend.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentGatewayController {
    private final StripeService stripeService;

    @PostMapping("/charge")
    public ResponseEntity<Charge> chargeCard(@RequestBody PaymentRequest paymentRequest) throws Exception {
        Charge charge = stripeService.chargeNewCard(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(charge);
    }
}
