package com.buzachero.app;

import com.buzachero.app.domain.*;
import com.buzachero.app.repository.OrderRepository;
import com.buzachero.app.repository.StoreRepository;
import com.buzachero.app.enumeration.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Profile("dev")
public class DBService implements ApplicationRunner {
    private StoreRepository storeRepository;
    private OrderRepository orderRepository;

    @Autowired
    public DBService(StoreRepository storeRepository, OrderRepository orderRepository) {
        this.storeRepository = storeRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Store store1 = new Store("Amazon", new Address("Georgia Street, 121", "New York", "New York", "USA"));
        Store store2 = new Store("Google", new Address("Philadelphia Street, 121", "California", "California", "USA"));

        System.out.println("DELETING ALL STORES AND CREATING NEW ONES ...");
        List<Store> savedStores = new ArrayList<>();
        storeRepository
                .deleteAll()
                .thenMany(Flux.just(store1,store2))
                .flatMap(storeRepository::save)
                .doOnNext(store -> savedStores.add(store))
                .blockLast();

        System.out.println("SAVED STORES LENGTH : " + savedStores.size());
        store1.setId(savedStores.get(0).getId());
        store2.setId(savedStores.get(1).getId());

        Order order1 = new Order(
                new Address("Rua Floriano Peixoto, 1298", "Andradina", "SP", "Brasil"),
                new Date(),
                store1,
                new ArrayList<>(Arrays.asList(new OrderItem("Taco de Baseball", 249.99, 1))),
                new Payment(PaymentStatus.PENDING, "23452345-2345324", new Date())
        );

        Order order2 = new Order(
                new Address("Rua Miguel Alves, 360", "São Carlos", "SP", "Brasil"),
                new Date(),
                store1,
                new ArrayList<>(Arrays.asList(new OrderItem("Bola de futebol", 80.65, 1),
                                                new OrderItem("Cadeira", 149.99, 4))),
                new Payment(PaymentStatus.PENDING, "889959-2345324", new Date())
        );

        Order order3 = new Order(
                new Address("Rua José Raimundo, 255", "Nazaré Paulista", "SP", "Brasil"),
                new Date(),
                store2,
                new ArrayList<>(Arrays.asList(new OrderItem("Espelho", 300.00, 3),
                                                new OrderItem("Camiseta", 49.99, 2))),
                new Payment(PaymentStatus.PENDING, "564865-2233244", new Date())
        );

        System.out.println("DELETING ALL ORDERS AND CREATING NEW ONES ...");
        orderRepository.deleteAll()
                        .thenMany(Flux.just(order1, order2, order3))
                        .flatMap(orderRepository::save)
                        .then()
                        .doOnEach(System.out::println)
                        .block();
    }
}
