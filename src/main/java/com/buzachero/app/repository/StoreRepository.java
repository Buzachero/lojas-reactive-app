package com.buzachero.app.repository;

import com.buzachero.app.domain.Store;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StoreRepository extends ReactiveMongoRepository<Store, String> {

    public Flux<Store> findByName(String name);
}
