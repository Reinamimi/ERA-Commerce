package me.emma.paymentservice.controller;

import lombok.AllArgsConstructor;
import me.emma.paymentservice.pojo.dto.Orders;
import me.emma.paymentservice.pojo.entity.Payment;
import me.emma.paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<String> createPayment(@RequestParam Long orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        if (payment == null) {
            paymentService.createPayment(orderId);
            return new ResponseEntity<>("Payment created", HttpStatus.OK);
        }
        return new ResponseEntity<>("Order is already paid!", HttpStatus.BAD_REQUEST);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Object> getPaymentByOrderId(@RequestParam("orderId") Long orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        if (payment == null) {
            return new ResponseEntity<>("Payment not found!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

}
