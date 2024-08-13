package me.emma.orderservice.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.emma.orderservice.pojo.Status;
import me.emma.orderservice.pojo.entity.Orders;
import me.emma.orderservice.repository.OrderRepository;
import me.emma.orderservice.service.OrderService;
import me.emma.orderservice.serviceClient.ProductServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OrderTrackerScheduler {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductServiceClient productServiceClient;

    @Scheduled(cron = "0 */2 * ? * *")
    public void updateOrder() {
        log.info("check if order is paid {}");
        List<Orders> orders = orderService.getOrders();
        for (Orders order : orders) {
            if (order.getStatus() == Status.PENDING) {
                order.setStatus(Status.CANCELLED);
                orderRepository.save(order);
                productServiceClient.updateProductInventory(order, false);
                log.info("order updated {}", order);
            }
        }

    }
}
