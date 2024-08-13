package me.emma.orderservice.serviceClient;

import lombok.extern.slf4j.Slf4j;
import me.emma.orderservice.pojo.dto.Product;
import me.emma.orderservice.pojo.entity.Orders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ProductServiceClient {

    @Value("${productInventoryService.productUrl}")
    private String productUrl;

    private final RestTemplate restTemplate;
    public ProductServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void updateProductInventory(Orders order, boolean isDeduction) {
        String url = productUrl.replace("{productId}", order.getProductId().toString())
                + "/restock" + "?quantity=" + order.getQuantity() + "&isDeduction=" + isDeduction;

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    null,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Product inventory updated successfully for productId: {}", order.getProductId());
            } else {
                log.error("Failed to update product inventory for productId: {}. Status code: {}", order.getProductId(), response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Failed to update product inventory for productId: {}", order.getProductId(), e);
        }
    }

    public Product getProductById(Long productId) {
        String url = productUrl.replace("{productId}", productId.toString());
        try {
            ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching product with ID: {}", productId, e);
            throw new RuntimeException("Product not found");
        }
    }

}
