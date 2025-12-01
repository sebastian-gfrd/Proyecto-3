package com.alpescab.repository;

import com.alpescab.model.Usuarios;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UsuariosRepository extends ReactiveMongoRepository<Usuarios, String> {
    Mono<Usuarios> findByCorreo(String correo);
    Mono<Boolean> existsByCorreo(String correo);
}
