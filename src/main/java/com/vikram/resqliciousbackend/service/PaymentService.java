package com.vikram.resqliciousbackend.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public PaymentService() {
        Stripe.apiKey = stripeApiKey;
    }

    public Charge chargeCard(String token, double amount) throws StripeException {
        return Charge.create(Map.of(
                "amount", (int) (amount * 100), // amount in cents
                "currency", "usd",
                "source", token,
                "description", "Payment for cart"
        ));
    }
}
