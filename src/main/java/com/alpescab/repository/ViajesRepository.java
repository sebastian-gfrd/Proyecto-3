package com.alpescab.repository;

import com.alpescab.model.Viajes;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Repositorio reactivo para la entidad Viajes.
 * Extiende ReactiveMongoRepository para operaciones CRUD reactivas.
 */
@Repository
public interface ViajesRepository extends ReactiveMongoRepository<Viajes, String> {

    /**
     * Busca todos los viajes de un conductor específico.
     * 
     * @param conductorId ID del conductor
     * @return Flux de viajes del conductor
     */
    Flux<Viajes> findByConductorId(String conductorId);

    /**
     * Busca todos los viajes de un pasajero específico.
     * 
     * @param pasajeroId ID del pasajero
     * @return Flux de viajes del pasajero
     */
    Flux<Viajes> findByPasajeroId(String pasajeroId);

    /**
     * Busca todos los viajes por estado.
     * 
     * @param estado Estado del viaje
     * @return Flux de viajes con el estado especificado
     */
    Flux<Viajes> findByEstado(Viajes.EstadoViaje estado);
}
