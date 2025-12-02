package com.alpescab.repository;

import com.alpescab.model.Conductor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ConductorRepository extends ReactiveMongoRepository<Conductor, String> {
    Mono<Conductor> findByUsuarioId(String usuarioId);

    Mono<Conductor> findByNumeroLicencia(String numeroLicencia);
}
