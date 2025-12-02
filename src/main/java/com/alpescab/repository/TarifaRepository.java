package com.alpescab.repository;

import com.alpescab.model.Tarifa;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface TarifaRepository extends ReactiveMongoRepository<Tarifa, String> {
    Mono<Tarifa> findByCiudadIdAndTipoServicioAndTipoVehiculo(String ciudadId, String tipoServicio,
            String tipoVehiculo);
}
