package com.alpescab.repository;

import com.alpescab.model.Ciudades;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CiudadesRepository extends ReactiveMongoRepository<Ciudades, String> {
    Flux<Ciudades> findByNombre(String nombre);
}