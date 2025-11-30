package com.alpescab.controller;

import com.alpescab.model.Viajes;
import com.alpescab.service.ViajesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST reactivo para la gestión de viajes.
 * Expone endpoints para operaciones CRUD sobre viajes.
 */
@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
@Slf4j
public class ViajesController {

    private final ViajesService viajesService;

    /**
     * Obtiene todos los viajes.
     * 
     * @return Flux de todos los viajes
     */
    @GetMapping
    public Flux<Viajes> listarViajes() {
        log.info("GET /api/viajes - Listando todos los viajes");
        return viajesService.obtenerTodosLosViajes();
    }

    /**
     * Obtiene un viaje por su ID.
     * 
     * @param id ID del viaje
     * @return Mono del viaje encontrado
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Viajes>> obtenerViaje(@PathVariable String id) {
        log.info("GET /api/viajes/{} - Obteniendo viaje", id);
        return viajesService.obtenerViajePorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo viaje.
     * 
     * @param viaje Viaje a crear
     * @return Mono del viaje creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Viajes> crearViaje(@Valid @RequestBody Viajes viaje) {
        log.info("POST /api/viajes - Creando nuevo viaje");
        return viajesService.crearViaje(viaje);
    }

    /**
     * Actualiza un viaje existente.
     * 
     * @param id    ID del viaje a actualizar
     * @param viaje Datos actualizados del viaje
     * @return Mono del viaje actualizado
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Viajes>> actualizarViaje(
            @PathVariable String id,
            @Valid @RequestBody Viajes viaje) {
        log.info("PUT /api/viajes/{} - Actualizando viaje", id);
        return viajesService.actualizarViaje(id, viaje)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un viaje por su ID.
     * 
     * @param id ID del viaje a eliminar
     * @return Mono vacío
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarViaje(@PathVariable String id) {
        log.info("DELETE /api/viajes/{} - Eliminando viaje", id);
        return viajesService.eliminarViaje(id);
    }

    /**
     * Obtiene viajes por conductor.
     * 
     * @param conductorId ID del conductor
     * @return Flux de viajes del conductor
     */
    @GetMapping("/conductor/{conductorId}")
    public Flux<Viajes> obtenerViajesPorConductor(@PathVariable String conductorId) {
        log.info("GET /api/viajes/conductor/{} - Obteniendo viajes del conductor", conductorId);
        return viajesService.obtenerViajesPorConductor(conductorId);
    }

    /**
     * Obtiene viajes por pasajero.
     * 
     * @param pasajeroId ID del pasajero
     * @return Flux de viajes del pasajero
     */
    @GetMapping("/pasajero/{pasajeroId}")
    public Flux<Viajes> obtenerViajesPorPasajero(@PathVariable String pasajeroId) {
        log.info("GET /api/viajes/pasajero/{} - Obteniendo viajes del pasajero", pasajeroId);
        return viajesService.obtenerViajesPorPasajero(pasajeroId);
    }

    /**
     * Obtiene viajes por estado.
     * 
     * @param estado Estado del viaje
     * @return Flux de viajes con el estado especificado
     */
    @GetMapping("/estado/{estado}")
    public Flux<Viajes> obtenerViajesPorEstado(@PathVariable Viajes.EstadoViaje estado) {
        log.info("GET /api/viajes/estado/{} - Obteniendo viajes por estado", estado);
        return viajesService.obtenerViajesPorEstado(estado);
    }
}
