package com.alpescab.service;

import com.alpescab.model.Conductor;
import com.alpescab.repository.ConductorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public class ConductorService {

    private final ConductorRepository repository;
    private final ReactiveMongoTemplate mongoTemplate;

    public Flux<Conductor> obtenerTodosLosConductores() {
        return repository.findAll();
    }

    public Mono<Conductor> registrarConductor(Conductor conductor) {
        if (conductor.getUsuarioId() == null || conductor.getCiudadId() == null) {
            return Mono.error(new IllegalArgumentException("Usuario ID y Ciudad ID son obligatorios"));
        }
        conductor.setCalificacionAvg(0.0);
        conductor.setEstado("DISPONIBLE");
        return repository.save(conductor);
    }

    public Mono<Conductor> actualizarEstado(String conductorId, String nuevoEstado) {
        return repository.findById(conductorId)
                .flatMap(conductor -> {
                    conductor.setEstado(nuevoEstado);
                    return repository.save(conductor);
                });
    }

    public Mono<Conductor> obtenerConductorPorId(String id) {
        return repository.findById(id);
    }

    // RFC2: Top 20 Conductores con más viajes
    // Esta consulta agrega sobre la colección 'Viajes'
    public Flux<TopConductorResult> obtenerTop20Conductores() {
        Aggregation aggregation = newAggregation(
                group("conductor_id").count().as("totalViajes"),
                sort(Sort.Direction.DESC, "totalViajes"),
                limit(20));

        return mongoTemplate.aggregate(aggregation, "Viajes", TopConductorResult.class);
    }

    // Clase auxiliar para mapear el resultado de la agregación
    @lombok.Data
    public static class TopConductorResult {
        private String id; // conductor_id
        private Long totalViajes;
    }
}
