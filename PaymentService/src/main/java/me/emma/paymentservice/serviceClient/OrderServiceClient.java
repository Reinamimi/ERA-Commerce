package me.emma.paymentservice.serviceClient;

import lombok.extern.slf4j.Slf4j;
import me.emma.paymentservice.pojo.dto.Orders;
import me.emma.paymentservice.pojo.dto.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class OrderServiceClient {
    private final RestTemplate restTemplate;
    public OrderServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    @Value("${orderService.orderUrl}")
    private String orderUrl;

    public void updateOrderStatus(Long orderId, Status status) {
        String url = String.format("%s/%s?status=%s", orderUrl, orderId, status);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    null,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Successfully updated order status");
            } else {
                log.error("Failed to update order status");
            }
        } catch (Exception e) {
            log.error("Failed to update order status", e);
        }
    }

    public Orders getOrderByOrderId(Long orderId) {
        String url = String.format("%s/%s", orderUrl, orderId);
        try {
            ResponseEntity<Orders> response = restTemplate.getForEntity(url, Orders.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to retrieve order status", e);
            return null;
        }
    }
}
