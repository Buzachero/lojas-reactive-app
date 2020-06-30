package com.buzachero.app.service;

import com.buzachero.app.domain.Order;
import com.buzachero.app.repository.OrderRepository;
import com.buzachero.app.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Mono<Order> findById(String orderId) {
        return orderRepository.findById(orderId);
    }

    public Flux<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> registerOrder(Order order) {
        validateOrderStatus(order);

        return orderRepository.save(order);
    }

    private void validateOrderStatus(Order order) {
        // validate business requirement in order to avoid saving invalid order in database
    }

}
