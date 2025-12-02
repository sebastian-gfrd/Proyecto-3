package com.alpescab.repository;

import com.alpescab.model.Calificacion;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CalificacionRepository extends ReactiveMongoRepository<Calificacion, String> {
    Flux<Calificacion> findByConductorId(String conductorId);
}
