package com.alpescab.repository;

import com.alpescab.model.Vehiculo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface VehiculoRepository extends ReactiveMongoRepository<Vehiculo, String> {

    Mono<Vehiculo> findByPlaca(String placa);
    Flux<Vehiculo> findByConductorId(String conductorId);
}