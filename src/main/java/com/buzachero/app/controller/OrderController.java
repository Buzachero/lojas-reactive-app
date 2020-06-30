package com.buzachero.app.controller;

import com.buzachero.app.domain.Order;
import com.buzachero.app.domain.Payment;
import com.buzachero.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public Mono<Order> findById(@PathVariable("id") String id) {
        return orderService.findById(id);
    }

    @GetMapping()
    public Flux<Order> findAll() {
        return orderService.findAllOrders();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> createOrder(@Valid @RequestBody Order order) {
        return orderService.registerOrder(order);
    }

    @PostMapping("/{id}/payment")
    public void registerPayment(@PathVariable String id, @RequestBody Payment payment) {

    }


}
