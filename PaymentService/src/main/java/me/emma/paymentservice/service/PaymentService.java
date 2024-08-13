package me.emma.paymentservice.service;

import lombok.AllArgsConstructor;
import me.emma.paymentservice.pojo.dto.Orders;
import me.emma.paymentservice.pojo.dto.Status;
import me.emma.paymentservice.pojo.entity.Payment;
import me.emma.paymentservice.repository.PaymentRepository;
import me.emma.paymentservice.serviceClient.OrderServiceClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderServiceClient orderServiceClient;


    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public void createPayment(Long orderId) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(orderServiceClient.getOrderByOrderId(orderId).getTotalPrice());
        paymentRepository.save(payment);
        orderServiceClient.updateOrderStatus(orderId, Status.PAID);
    }
}
