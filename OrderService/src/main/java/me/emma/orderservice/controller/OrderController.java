package me.emma.orderservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.emma.orderservice.pojo.Status;
import me.emma.orderservice.pojo.dto.OrderDTO;
import me.emma.orderservice.pojo.entity.Orders;
import me.emma.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping()
@AllArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Orders>> getOrders() {
        log.info("Get orders");
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
        log.info("Get order by ID {}", id);
        Orders order = orderService.getOrderById(id);
        if (order == null) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addOrder(@RequestBody OrderDTO orderDTO) {
        log.info("Add order: {}", orderDTO);
        boolean flag = orderService.addOrder(orderDTO);
        log.info("Order added: {}", flag);
        if (flag) {
            return new ResponseEntity<>("Order created", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed to create a order", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/orders/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Object> getOrderByUserId(@PathVariable Long userId) {
        log.info("Get orders by userId: {}", userId);
        Optional<List<Orders>> orders = orderService.getOrderByUserId(userId);
        if (!orders.isPresent()) {
            return new ResponseEntity<>("Order not found", HttpStatus.OK);
        }
        return new ResponseEntity<>(orders.get(), HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestParam Status status) {
        log.info("Update order status: {}", orderId);
        boolean flag = orderService.updateOrderStatus(orderId, status);
        if (flag) {
            return new ResponseEntity<>("Order status updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
    }

}
