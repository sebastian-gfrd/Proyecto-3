package com.alpescab.repository;

import com.alpescab.model.Conductor;
import com.alpescab.model.EstadoConductor;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ConductoresRepository extends ReactiveMongoRepository<Conductor, String> {

    // RF6 Esta sentencia busca al condcutor dado la distancia mas cercana, el punto
    // y el estado activo del conductor
    Flux<Conductor> findByUbicacionActualNearAndEstado(Point ubicacion, Distance distancia, EstadoConductor estado);

    // RFC2
    // Busca los conductores ordenados por totalViajes descendente y limita a 20.
    // Esto evita tener que usar Aggregations complejos manuales.
    Flux<Conductor> findTop20ByOrderByTotalViajesDesc();

    Mono<Conductor> findByUsuarioId(String usuarioId); // bucar el usuario por id

    Mono<Conductor> findByNumeroLicencia(String numeroLicencia); // Buscar por licencia
}