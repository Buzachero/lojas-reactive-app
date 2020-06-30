package com.buzachero.app.controller;

import com.buzachero.app.domain.*;
import com.buzachero.app.enumeration.PaymentStatus;
import com.buzachero.app.repository.OrderRepository;
import com.buzachero.app.repository.StoreRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

// MAKE SURE TO RUN THIS TEST WHEN REAL MONGO DB SERVER IS DOWN
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {
    private static final String BASE_ORDER_URL = "/order";
    private static final MediaType EXPECTED_MEDIA_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Autowired
    private TestRestTemplate template;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderRepository orderRepository;

    private static Flux<Store> stores = Flux.just(
            new Store("Loja1", new Address("Rua das Américas, 75", "São Paulo", "SP", "Brasil")),
            new Store("Loja2", new Address("Rua das Filipinas, 759", "Belo Horizonte", "MG", "Brasil")),
            new Store("Loja3", new Address("Rua Mato Grosso, 759", "Cuiabá", "MT", "Brasil"))
    );

    private static Order order1 = new Order(
            new Address("Rua Floriano Peixoto, 1298", "Andradina", "SP", "Brasil"),
            new Date(),
            null,
            new ArrayList<>(),
            new Payment(PaymentStatus.PENDING, "23452345-2345324", new Date())
    );

    private static Order order2 = new Order(
            new Address("Rua Miguel Alves, 360", "São Carlos", "SP", "Brasil"),
            new Date(),
            null,
            new ArrayList<>(Arrays.asList(new OrderItem("Bola de futebol", 80.65, 1))),
            new Payment(PaymentStatus.PENDING, "889959-2345324", new Date())
    );

    private static Order order3 = new Order(
            new Address("Rua José Raimundo, 255", "Nazaré Paulista", "SP", "Brasil"),
            new Date(),
            null,
            new ArrayList<>(Arrays.asList(new OrderItem("Espelho", 20.00, 3))),
            new Payment(PaymentStatus.PENDING, "564865-2233244", new Date())
    );

    private static Flux<Order> orders = Flux.just(order1, order2, order3);

    @Before
    public void setUpDatabase() {
        System.out.println("SETTING UP THE DATABASE FOR ORDER CONTROLLER TESTING ...");

        storeRepository
                .deleteAll()
                .thenMany(stores)
                .flatMap(storeRepository::save)
                .doOnNext(System.out::println)
                .blockLast();

        orderRepository
                .deleteAll()
                .thenMany(orders)
                .flatMap(orderRepository::save)
                .doOnEach(System.out::println)
                .blockLast();
    }

    @Test
    public void testGetAllOrders() {
        ResponseEntity<Order[]> responseEntity = template.getForEntity(BASE_ORDER_URL, Order[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(EXPECTED_MEDIA_TYPE, responseEntity.getHeaders().getContentType());

        Order[] responseOrders = responseEntity.getBody();
        assertNotNull(responseOrders);
        assertTrue(responseOrders.length == 3);
    }

    @Test
    public void testCreateOrder() {
        Order newOrder = new Order(
                new Address("Avenida Brasilia, 500", "Araçatuba", "SP", "Brasil"),
                new Date(),
                new Store("Loja500", new Address("Rua Holanda, 75", "São Paulo", "SP", "Brasil")),
                new ArrayList<>(Arrays.asList(new OrderItem("Notebook", 2000.00, 1))),
                new Payment(PaymentStatus.PENDING, "564865-2233244", new Date())
        );
        ResponseEntity<Order> responseEntity = template.postForEntity(BASE_ORDER_URL, newOrder, Order.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(EXPECTED_MEDIA_TYPE, responseEntity.getHeaders().getContentType());

        Order responseOrder = responseEntity.getBody();
        assertNotNull(responseOrder);

        // validate order item of the order
        List<OrderItem> orderItemList = responseOrder.getOrderItemList();
        assertNotNull(orderItemList);
        assertTrue(orderItemList.size() == 1);
        assertEquals("Notebook", orderItemList.get(0).getDescription());
        assertTrue(orderItemList.get(0).getUnitPrice() == 2000.00);
        assertTrue(orderItemList.get(0).getQuantity() == 1);

        //validate address of the order
        Address address = responseOrder.getAddress();
        assertNotNull(address);
        assertEquals("Avenida Brasilia, 500", address.getLocation());
        assertEquals("Araçatuba", address.getCity());
        assertEquals("SP", address.getState());
        assertEquals("Brasil", address.getCountry());
    }

    // WE SHOULD ADD OTHER TEST TO TEST THE OTHER APIs OF ORDER CONTROLLER

}
