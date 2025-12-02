// ...existing code...
package com.alpescab.service;

import com.alpescab.model.Vehiculo;
import com.alpescab.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VehiculoService {

    private final VehiculoRepository repo;
    private final org.springframework.data.mongodb.core.ReactiveMongoTemplate mongoTemplate;

    public VehiculoService(VehiculoRepository repo,
            org.springframework.data.mongodb.core.ReactiveMongoTemplate mongoTemplate) {
        this.repo = repo;
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<Vehiculo> crearVehiculo(Vehiculo vehiculo) {
        // MongoDB generará automáticamente el ID si es null
        return repo.save(vehiculo);
    }

    public Mono<Vehiculo> obtenerVehiculoPorId(String id) {
        return repo.findById(id);
    }

    public Flux<Vehiculo> obtenerTodos() {
        return repo.findAll();
    }

    public Mono<Vehiculo> actualizarVehiculo(String id, Vehiculo vehiculoDetalles) {
        return repo.findById(id)
                .flatMap(existing -> {
                    existing.setCiudadId(vehiculoDetalles.getCiudadId());
                    existing.setConductorId(vehiculoDetalles.getConductorId());
                    existing.setPlaca(vehiculoDetalles.getPlaca());
                    existing.setModelo(vehiculoDetalles.getModelo());
                    existing.setTipoVehiculo(vehiculoDetalles.getTipoVehiculo());
                    existing.setCapacidad(vehiculoDetalles.getCapacidad());
                    existing.setAnio(vehiculoDetalles.getAnio());
                    existing.setId(id);
                    return repo.save(existing);
                });
    }

    public Mono<Void> eliminarVehiculo(String id) {
        return repo.findById(id)
                .flatMap(existing -> repo.deleteById(id));
    }

    public Flux<Vehiculo> obtenerVehiculosPorConductor(String conductorId) {
        return repo.findByConductorId(conductorId);
    }

    // RF4: Registrar Disponibilidad
    public Mono<Vehiculo> registrarDisponibilidad(String vehiculoId,
            com.alpescab.model.Disponibilidad nuevaDisponibilidad) {
        return repo.findById(vehiculoId)
                .flatMap(vehiculo -> {
                    // Validar superposición
                    boolean overlap = vehiculo.getDisponibilidad().stream()
                            .anyMatch(d -> d.getDiaSemana().equalsIgnoreCase(nuevaDisponibilidad.getDiaSemana()) &&
                                    isOverlapping(d.getHoraInicio(), d.getHoraFin(),
                                            nuevaDisponibilidad.getHoraInicio(), nuevaDisponibilidad.getHoraFin()));

                    if (overlap) {
                        return Mono.error(new IllegalArgumentException(
                                "El horario se superpone con una disponibilidad existente."));
                    }

                    // Actualización atómica con $push
                    return mongoTemplate.updateFirst(
                            org.springframework.data.mongodb.core.query.Query.query(
                                    org.springframework.data.mongodb.core.query.Criteria.where("_id").is(vehiculoId)),
                            new org.springframework.data.mongodb.core.query.Update().push("disponibilidad",
                                    nuevaDisponibilidad),
                            Vehiculo.class).flatMap(result -> repo.findById(vehiculoId));
                });
    }

    // RF5: Modificar Disponibilidad
    public Mono<Vehiculo> modificarDisponibilidad(String vehiculoId, int index,
            com.alpescab.model.Disponibilidad modificacion) {
        return repo.findById(vehiculoId)
                .flatMap(vehiculo -> {
                    if (index < 0 || index >= vehiculo.getDisponibilidad().size()) {
                        return Mono.error(new IllegalArgumentException("Índice de disponibilidad inválido."));
                    }

                    // Validar superposición (excluyendo el índice actual)
                    boolean overlap = false;
                    for (int i = 0; i < vehiculo.getDisponibilidad().size(); i++) {
                        if (i == index)
                            continue;
                        com.alpescab.model.Disponibilidad d = vehiculo.getDisponibilidad().get(i);
                        if (d.getDiaSemana().equalsIgnoreCase(modificacion.getDiaSemana()) &&
                                isOverlapping(d.getHoraInicio(), d.getHoraFin(), modificacion.getHoraInicio(),
                                        modificacion.getHoraFin())) {
                            overlap = true;
                            break;
                        }
                    }

                    if (overlap) {
                        return Mono.error(new IllegalArgumentException(
                                "El horario modificado se superpone con otra disponibilidad existente."));
                    }

                    // Actualización atómica con $set en posición específica
                    return mongoTemplate.updateFirst(
                            org.springframework.data.mongodb.core.query.Query.query(
                                    org.springframework.data.mongodb.core.query.Criteria.where("_id").is(vehiculoId)),
                            new org.springframework.data.mongodb.core.query.Update().set("disponibilidad." + index,
                                    modificacion),
                            Vehiculo.class).flatMap(result -> repo.findById(vehiculoId));
                });
    }

    private boolean isOverlapping(String start1, String end1, String start2, String end2) {
        return start1.compareTo(end2) < 0 && start2.compareTo(end1) < 0;
    }
}