package com.buzachero.app.controller;

import com.buzachero.app.domain.Store;
import com.buzachero.app.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Store>> findById(@PathVariable("id") String id) {

        return storeService
                .findById(id)
                .map(storeFound -> new ResponseEntity<>(storeFound, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // AFTER STORE UPDATE, WE SHOULD UPDATE THE STORE INFO IN ALL ORDERS THAT CONTAIN THIS STORE IN IT
    }

    @GetMapping()
    public Flux<Store> findAll() {
        return storeService.findAllStores();
    }

    @GetMapping("/name")
    public Flux<Store> findByName(@RequestParam(value="name") String name) {
        return storeService.findByName(name);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Store>> updateStore(@PathVariable("id") String id, @RequestBody Store store) {
        return storeService
                .updateStore(id, store)
                .map(updatedStore -> new ResponseEntity<Store>(HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<Store>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Store> createStore(@Valid @RequestBody Store store) {
        return storeService.createStore(store);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteStore(@PathVariable("id") String id) {
        return storeService
                .deleteStore(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));

        // IF DELETE STORE IS ALLOWED IN THIS APPLICATION, WE SHOULD DELETE ALL THE ORDERS AS WELL
    }


}
