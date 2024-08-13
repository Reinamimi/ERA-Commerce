package me.emma.orderservice.serviceClient;

import lombok.extern.slf4j.Slf4j;
import me.emma.orderservice.pojo.dto.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class PaymentServiceClient {

    @Value("${paymentService.paymentUrl}")
    private String paymentUrl;

    private final RestTemplate restTemplate;

    public PaymentServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Payment getPaymentByOrderId(Long orderId) {
        String url = String.format("%s?orderId=%d", paymentUrl, orderId);
        try {
            ResponseEntity<Payment> response = restTemplate.getForEntity(url, Payment.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (RestClientException e) {
            return null;
        }
    }
}
