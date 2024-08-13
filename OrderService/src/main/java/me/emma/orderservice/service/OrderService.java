package me.emma.orderservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.emma.orderservice.pojo.Status;
import me.emma.orderservice.pojo.dto.OrderDTO;
import me.emma.orderservice.pojo.dto.Product;
import me.emma.orderservice.pojo.entity.Orders;
import me.emma.orderservice.repository.OrderRepository;
import me.emma.orderservice.serviceClient.ProductServiceClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;

    public List<Orders> getOrders() {
        return orderRepository.findAll();
    }

    public boolean addOrder(OrderDTO orderDTO) {
        Product product = productServiceClient.getProductById(orderDTO.getProductId());
        log.info("product: {}", product);
        if (product == null) {
            return false;
        }
        log.info("stock of product: " + product.getStock());
        if (product.getStock() < orderDTO.getQuantity()) {
            return false;
        }
        Orders order = new Orders();
        BeanUtils.copyProperties(orderDTO, order);
        BigDecimal total = product.getPrice().multiply(new BigDecimal(orderDTO.getQuantity()));
        order.setTotalPrice(total);
        order.setStatus(Status.PENDING);
        orderRepository.save(order);
        productServiceClient.updateProductInventory(order, true);
        return true;
    }

    public Optional<List<Orders>> getOrderByUserId(Long userId) {
        return Optional.ofNullable(orderRepository.findByUserId(userId));
    }

    public boolean updateOrderStatus(Long orderId, Status status) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    public Orders getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}
