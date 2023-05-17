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

    public List<Payment> findPaymentsByUserId(UUID id) {
        return paymentRepository.findAllPaymentsByUserId(id);
    }

    public Payment createOne(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updateOne(Payment payment) {
        Payment paymentToUpdate = paymentRepository.findById(payment.getId()).orElse((null));
        if (paymentToUpdate == null) {
            return null;
        }
        paymentToUpdate.setPaymentType(payment.getPaymentType());
        paymentToUpdate.setProvider(payment.getProvider());
        paymentToUpdate.setCardNumber(payment.getCardNumber());
        paymentToUpdate.setExpirationDate(payment.getExpirationDate());
        paymentToUpdate.setCardHolderName(payment.getCardHolderName());
        return paymentRepository.save(paymentToUpdate);
    }

    public Payment deleteById(UUID id) {
        Payment paymentToDelete = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));

        paymentRepository.deleteById(id);
        return paymentToDelete;
    }
}
