package com.example.backend.PaymentMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(UUID id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment createOne(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updateOne(Payment payment) {
        Payment paymentToUpdate = paymentRepository.findById(payment.getId()).orElse(null);
        if (paymentToUpdate == null) {
            return null;
        }
        paymentToUpdate.setCardNumber(payment.getCardNumber());
        paymentToUpdate.setPaymentType(payment.getPaymentType());
        paymentToUpdate.setExpirationDate(payment.getExpirationDate());
        return paymentRepository.save(paymentToUpdate);
    }

    public Payment deleteById(UUID id) {
        Payment paymentToDelete = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));

        paymentRepository.deleteById(id);
        return paymentToDelete;
    }
}
