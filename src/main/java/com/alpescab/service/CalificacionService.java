package com.alpescab.service;

import com.alpescab.model.Calificacion;
import com.alpescab.model.Conductor;
import com.alpescab.repository.CalificacionRepository;
import com.alpescab.repository.ConductoresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final ConductoresRepository conductoresRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    public Flux<Calificacion> obtenerTodas() {
        return calificacionRepository.findAll();
    }

    public Mono<Calificacion> registrarCalificacion(Calificacion calificacion) {
        log.info("Registrando calificación para conductor: {}", calificacion.getConductorId());

        // Validación de campos requeridos
        if (calificacion.getConductorId() == null || calificacion.getConductorId().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El conductorId es requerido"));
        }
        if (calificacion.getUsuarioId() == null || calificacion.getUsuarioId().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El usuarioId es requerido"));
        }
        if (calificacion.getPuntuacion() == null) {
            return Mono.error(new IllegalArgumentException("La puntuación es requerida"));
        }
        if (calificacion.getPuntuacion() < 0 || calificacion.getPuntuacion() > 5) {
            return Mono.error(new IllegalArgumentException("La puntuación debe estar entre 0 y 5"));
        }

        calificacion.setFechaCreada(LocalDateTime.now());

        return calificacionRepository.save(calificacion)
                .flatMap(savedCalificacion -> {
                    log.info("Calificación guardada. Actualizando promedio...");
                    return actualizarPromedioConductor(savedCalificacion.getConductorId())
                            .thenReturn(savedCalificacion);
                })
                .doOnError(e -> log.error("Error al registrar calificación", e));
    }

    private Mono<Void> actualizarPromedioConductor(String conductorId) {
        return calificacionRepository.findByConductorId(conductorId)
                .collectList()
                .flatMap(calificaciones -> {
                    if (calificaciones.isEmpty()) {
                        log.info("No hay calificaciones para conductor {}", conductorId);
                        return Mono.empty();
                    }

                    double promedio = calificaciones.stream()
                            .filter(c -> c.getPuntuacion() != null)
                            .mapToDouble(Calificacion::getPuntuacion)
                            .average()
                            .orElse(0.0);

                    log.info("Nuevo promedio calculado para conductor {}: {}", conductorId, promedio);

                    // Actualización atómica del promedio en la colección de Conductores
                    Query query = Query.query(Criteria.where("_id").is(conductorId));
                    Update update = Update.update("calificacion_avg", promedio);

                    return mongoTemplate.updateFirst(query, update, Conductor.class)
                            .doOnSuccess(
                                    r -> log.info("Promedio actualizado en DB. Modificados: {}", r.getModifiedCount()))
                            .then();
                });
    }
}
