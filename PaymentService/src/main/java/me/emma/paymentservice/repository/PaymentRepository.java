package me.emma.paymentservice.repository;

import me.emma.paymentservice.pojo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderId(Long orderId);

}
