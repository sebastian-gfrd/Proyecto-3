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
import java.util.Map;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
// ...existing code...

@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
@Slf4j
public class ViajesController {

    private final ViajesService viajesService;

    // RF6: Solicitar Servicio
    @PostMapping("/solicitar")
    public Mono<ResponseEntity<Object>> solicitarServicio(@Valid @RequestBody SolicitudViajeDTO solicitud) {
        log.info("POST /api/viajes/solicitar - Solicitando nuevo servicio");

        // Validaciones básicas usando campos reales de SolicitudViajeDTO
        if (solicitud == null) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "Solicitud vacía")));
        }

        if (solicitud.getPasajeroId() == null || solicitud.getPasajeroId().isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "pasajeroId es obligatorio")));
        }

        if (solicitud.getCiudadId() == null || solicitud.getCiudadId().isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "ciudadId es obligatorio")));
        }

        // Validar coordenadas de origen (latitud/longitud) para evitar NPE por desboxing
        if (solicitud.getLatitud() == null || solicitud.getLongitud() == null) {
            return Mono.just(ResponseEntity.badRequest()
                    .body(Map.of("error", "Coordenadas de origen (latitud/longitud) son obligatorias")));
        }

        return viajesService.solicitarServicio(solicitud)
                .map(viaje -> ResponseEntity.status(HttpStatus.CREATED).body((Object) viaje))
                .onErrorResume(e -> {
                    log.error("Error al procesar solicitud de viaje", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Error interno", "message", e.getMessage())));
                });
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

    // RFC3: Estadísticas de uso de servicios
    @GetMapping("/estadisticas")
    public Flux<com.alpescab.dto.EstadisticaServicioDTO> obtenerEstadisticas(
            @RequestParam String ciudadId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        log.info("GET /api/viajes/estadisticas - Ciudad: {}, Desde: {}, Hasta: {}",
                ciudadId, fechaInicio, fechaFin);

        java.time.LocalDateTime inicio = java.time.LocalDateTime.parse(fechaInicio);
        java.time.LocalDateTime fin = java.time.LocalDateTime.parse(fechaFin);

        return viajesService.obtenerEstadisticasServicio(ciudadId, inicio, fin);
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
