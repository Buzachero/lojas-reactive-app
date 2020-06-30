package com.buzachero.app.controller;

import com.buzachero.app.domain.Address;
import com.buzachero.app.domain.Store;
import com.buzachero.app.repository.StoreRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import static org.junit.Assert.*;

// MAKE SURE TO RUN THIS TEST WHEN REAL MONGO DB SERVER IS DOWN
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StoreControllerTest {
    private static final String BASE_STORE_URL = "/store";
    private static final MediaType EXPECTED_MEDIA_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private StoreRepository storeRepository;

    private static Flux<Store> stores = Flux.just(
        new Store("Loja1", new Address("Rua das Américas, 75", "São Paulo", "SP", "Brasil")),
        new Store("Loja2", new Address("Rua das Filipinas, 759", "Belo Horizonte", "MG", "Brasil")),
        new Store("Loja3", new Address("Rua Mato Grosso, 759", "Cuiabá", "MT", "Brasil"))
    );

    @Before
    public void setUpDatabase() {
        System.out.println("SETTING UP THE DATABASE FOR STORE CONTROLLER TESTING ...");
        storeRepository
                .deleteAll()
                .thenMany(stores)
                .flatMap(storeRepository::save)
                .doOnNext(System.out::println)
                .blockLast();
    }

    @Test
    public void testGetAllStores() {
        ResponseEntity<Store[]> responseEntity = template.getForEntity(BASE_STORE_URL, Store[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(EXPECTED_MEDIA_TYPE, responseEntity.getHeaders().getContentType());

        Store[] responseStores = responseEntity.getBody();
        assertNotNull(responseStores);
        assertTrue(responseStores.length == 3);
    }

    @Test
    public void testGetStoreByName() {
        ResponseEntity<Store[]> responseEntity = template.getForEntity(BASE_STORE_URL + "/name?name=Loja1", Store[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(EXPECTED_MEDIA_TYPE, responseEntity.getHeaders().getContentType());

        Store[] responseStores = responseEntity.getBody();
        assertNotNull(responseStores);
        assertTrue(responseStores.length == 1);
        assertEquals("Loja1", responseStores[0].getName());
    }

    @Test
    public void testCreateStore() {
        Store newStore =
                new Store("Loja4",
                        new Address("Rua Independência, 2587", "Rio de Janeiro", "RJ", "Brasil")
                );
        ResponseEntity<Store> responseEntity = template.postForEntity(BASE_STORE_URL, newStore, Store.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(EXPECTED_MEDIA_TYPE, responseEntity.getHeaders().getContentType());

        Store responseStore = responseEntity.getBody();
        assertNotNull(responseStore);
        assertEquals("Loja4", responseStore.getName());
        assertEquals("Rua Independência, 2587", responseStore.getAddress().getLocation());
        assertEquals("Rio de Janeiro", responseStore.getAddress().getCity());
        assertEquals("RJ", responseStore.getAddress().getState());
        assertEquals("Brasil", responseStore.getAddress().getCountry());
    }

    // WE SHOULD ADD ANOTHER TEST TO TEST THE updateStore AND deleteStore API

}
