package com.alpescab.service;

import com.alpescab.model.Viajes;
import com.alpescab.repository.ViajesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Servicio para la gestión de viajes.
 * Implementa la lógica de negocio para operaciones CRUD reactivas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ViajesService {

    private final ViajesRepository viajesRepository;

    /**
     * Obtiene todos los viajes.
     * 
     * @return Flux de todos los viajes
     */
    public Flux<Viajes> obtenerTodosLosViajes() {
        log.info("Obteniendo todos los viajes");
        return viajesRepository.findAll()
                .doOnNext(viaje -> log.debug("Viaje encontrado: {}", viaje.getId()))
                .doOnComplete(() -> log.info("Listado de viajes completado"));
    }

    /**
     * Obtiene un viaje por su ID.
     * 
     * @param id ID del viaje
     * @return Mono del viaje encontrado
     */
    public Mono<Viajes> obtenerViajePorId(String id) {
        log.info("Buscando viaje con ID: {}", id);
        return viajesRepository.findById(id)
                .doOnNext(viaje -> log.debug("Viaje encontrado: {}", viaje))
                .doOnError(error -> log.error("Error al buscar viaje: {}", error.getMessage()));
    }

    /**
     * Crea un nuevo viaje.
     * 
     * @param viaje Viaje a crear
     * @return Mono del viaje creado
     */
    public Mono<Viajes> crearViaje(Viajes viaje) {
        log.info("Creando nuevo viaje");
        return viajesRepository.save(viaje)
                .doOnNext(v -> log.info("Viaje creado con ID: {}", v.getId()))
                .doOnError(error -> log.error("Error al crear viaje: {}", error.getMessage()));
    }

    /**
     * Actualiza un viaje existente.
     * 
     * @param id    ID del viaje a actualizar
     * @param viaje Datos actualizados del viaje
     * @return Mono del viaje actualizado
     */
    public Mono<Viajes> actualizarViaje(String id, Viajes viaje) {
        log.info("Actualizando viaje con ID: {}", id);
        return viajesRepository.findById(id)
                .flatMap(viajeExistente -> {
                    viaje.setId(id);
                    return viajesRepository.save(viaje);
                })
                .doOnNext(v -> log.info("Viaje actualizado: {}", v.getId()))
                .doOnError(error -> log.error("Error al actualizar viaje: {}", error.getMessage()));
    }

    /**
     * Elimina un viaje por su ID.
     * 
     * @param id ID del viaje a eliminar
     * @return Mono vacío
     */
    public Mono<Void> eliminarViaje(String id) {
        log.info("Eliminando viaje con ID: {}", id);
        return viajesRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Viaje eliminado: {}", id))
                .doOnError(error -> log.error("Error al eliminar viaje: {}", error.getMessage()));
    }

    /**
     * Obtiene viajes por conductor.
     * 
     * @param conductorId ID del conductor
     * @return Flux de viajes del conductor
     */
    public Flux<Viajes> obtenerViajesPorConductor(String conductorId) {
        log.info("Obteniendo viajes del conductor: {}", conductorId);
        return viajesRepository.findByConductorId(conductorId);
    }

    /**
     * Obtiene viajes por pasajero.
     * 
     * @param pasajeroId ID del pasajero
     * @return Flux de viajes del pasajero
     */
    public Flux<Viajes> obtenerViajesPorPasajero(String pasajeroId) {
        log.info("Obteniendo viajes del pasajero: {}", pasajeroId);
        return viajesRepository.findByPasajeroId(pasajeroId);
    }

    /**
     * Obtiene viajes por estado.
     * 
     * @param estado Estado del viaje
     * @return Flux de viajes con el estado especificado
     */
    public Flux<Viajes> obtenerViajesPorEstado(Viajes.EstadoViaje estado) {
        log.info("Obteniendo viajes con estado: {}", estado);
        return viajesRepository.findByEstado(estado);
    }
}
