package com.buzachero.app.service;

import com.buzachero.app.domain.Store;
import com.buzachero.app.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    public Mono<Store> findById(String storeId) {
        return storeRepository.findById(storeId);
    }

    public Flux<Store> findAllStores() {
        return storeRepository.findAll();
    }

    public Flux<Store> findByName(String storeName) {
        return storeRepository.findByName(storeName);
    }

    public Mono<Store> createStore(Store store) {
        return storeRepository.insert(store);
    }

    public Mono<Store> updateStore(String storeId, Store store) {
        System.out.println("UPDATING STORE ID = " + store.getId());
        //storeRepository.
        return storeRepository
                .findById(storeId)
                .flatMap(existingStore -> {
                    existingStore.setName(store.getName());
                    if(store.getAddress() != null) {
                        existingStore.setAddress(store.getAddress());
                    }
                    return storeRepository.save(existingStore);
                });
    }

    public Mono<Void> deleteStore(String storeId) {
        return storeRepository
                .deleteById(storeId);
    }

}
