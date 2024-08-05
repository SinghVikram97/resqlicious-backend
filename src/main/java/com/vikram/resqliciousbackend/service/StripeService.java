package com.vikram.resqliciousbackend.service;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.vikram.resqliciousbackend.dto.PaymentRequest;
import com.vikram.resqliciousbackend.entity.Payment;
import com.vikram.resqliciousbackend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
    private PaymentRepository paymentRepository;

    @Autowired
    StripeService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = "sk_test_51PjxlURoDih2UVCy8a7ZegMqoiNHNfISBAENxPfJMHrC6HWeJdr0MVC860mQ6g3h6oBaSLSZ61RmqQpIbn7WvJbg00o3V8TdgB";
    }

    public Charge chargeNewCard(PaymentRequest paymentRequest) throws Exception {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", paymentRequest.getAmount().intValue() * 100);
        chargeParams.put("currency", "CAD");
        chargeParams.put("source", paymentRequest.getToken());
        Charge charge = Charge.create(chargeParams);
        //Create Payment Entity in Our Database
        createPayment(paymentRequest);
        return charge;
    }

    private void createPayment(PaymentRequest paymentRequest) {
        paymentRepository.save(Payment.builder()
                .amount(paymentRequest.getAmount())
                .cartId(paymentRequest.getCartId())
                .userId(paymentRequest.getUserId())
                .restaurantId(paymentRequest.getRestaurantId())
                .build());
    }
}
