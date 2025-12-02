package com.alpescab.controller;

import com.alpescab.dto.SolicitudViajeDTO;
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

@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
@Slf4j
public class ViajesController {

    private final ViajesService viajesService;

    // RF6: Solicitar Servicio
    @PostMapping("/solicitar")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Viajes> solicitarServicio(@RequestBody SolicitudViajeDTO solicitud) {
        log.info("POST /api/viajes/solicitar - Solicitando nuevo servicio");
        return viajesService.solicitarServicio(solicitud);
    }

    // RF7: Finalizar Servicio
    @PutMapping("/{viajeId}/finalizar")
    public Mono<ResponseEntity<Viajes>> finalizarServicio(
            @PathVariable String viajeId,
            @RequestBody Viajes datosFinales) {
        log.info("PUT /api/viajes/{}/finalizar - Finalizando servicio", viajeId);
        return viajesService.finalizarViaje(viajeId, datosFinales)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // RFC1: Consultar Histórico
    @GetMapping("/historico/{pasajeroId}")
    public Flux<Viajes> consultarHistorico(@PathVariable String pasajeroId) {
        log.info("GET /api/viajes/historico/{} - Consultando histórico", pasajeroId);
        return viajesService.consultarHistorico(pasajeroId);
    }

    // Endpoints existentes
    @GetMapping
    public Flux<Viajes> obtenerTodosLosViajes() {
        return viajesService.obtenerTodosLosViajes();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Viajes>> obtenerViaje(@PathVariable String id) {
        return viajesService.obtenerViajePorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Viajes> crearViaje(@Valid @RequestBody Viajes viaje) {
        return viajesService.crearViaje(viaje);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Viajes>> actualizarViaje(
            @PathVariable String id,
            @Valid @RequestBody Viajes viaje) {
        return viajesService.actualizarViaje(id, viaje)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarViaje(@PathVariable String id) {
        return viajesService.eliminarViaje(id);
    }
}
