package com.alpescab.service;

import com.alpescab.dto.SolicitudViajeDTO;
import com.alpescab.model.Conductor;
import com.alpescab.model.EstadoConductor;
import com.alpescab.model.Viajes;
import com.alpescab.repository.ConductoresRepository;
import com.alpescab.repository.ViajesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ViajesService {

    private final ViajesRepository viajesRepository;
    private final ConductoresRepository conductoresRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    // RF6: Solicitar un Servicio
    @Transactional
    public Mono<Viajes> solicitarServicio(SolicitudViajeDTO solicitud) {
        log.info("Solicitando servicio para pasajero: {}", solicitud.getPasajeroId());

        // 1. Buscar conductor disponible en la ciudad
        return conductoresRepository
                .findByUbicacionActualNearAndEstado(new Point(solicitud.getLongitud(), solicitud.getLatitud()),
                        new Distance(10), EstadoConductor.ACTIVO)
                .next() // Tomar el primero disponible (simplificación de "cerca")
                .switchIfEmpty(Mono.error(new RuntimeException("No hay conductores disponibles")))
                .flatMap(conductor -> {
                    // 2. Calcular tarifa (simplificado, asumiendo tarifa base + km)
                    // Nota: TarifasRepository usa Integer para ciudadId, pero Solicitud usa String.
                    // Esto es un problema de tipos. Asumiremos conversión o ajuste.
                    // Por ahora, usamos un valor por defecto si no se encuentra tarifa exacta.
                    double tarifaEstimada = solicitud.getDistanciaKm() * 1.5; // Dummy logic

                    // 3. Crear Viaje y Actualizar Conductor (Transaccional)
                    Viajes nuevoViaje = new Viajes();
                    nuevoViaje.setPasajeroId(solicitud.getPasajeroId());
                    nuevoViaje.setConductorId(conductor.getConductor_id());
                    nuevoViaje.setCiudadId(solicitud.getCiudadId());
                    nuevoViaje.setOrigenUbicacion(solicitud.getOrigenUbicacion());
                    nuevoViaje.setDestinoUbicacion(solicitud.getDestinoUbicacion());
                    nuevoViaje.setTipoServicio(solicitud.getTipoServicio());
                    nuevoViaje.setDistanciaKm(solicitud.getDistanciaKm());
                    nuevoViaje.setTarifa(tarifaEstimada);
                    nuevoViaje.setFechaInicio(LocalDateTime.now());
                    nuevoViaje.setEstado("ASIGNADO");

                    conductor.setEstado(EstadoConductor.OCUPADO);

                    return conductoresRepository.save(conductor)
                            .then(viajesRepository.save(nuevoViaje));
                });
    }

    // RF7: Finalizar Viaje
    @Transactional
    public Mono<Viajes> finalizarViaje(String viajeId, Viajes datosFinales) {
        return viajesRepository.findById(viajeId)
                .flatMap(viaje -> {
                    viaje.setEstado("COMPLETADO");
                    viaje.setFechaFin(LocalDateTime.now());
                    if (datosFinales.getDistanciaKm() != null)
                        viaje.setDistanciaKm(datosFinales.getDistanciaKm());
                    if (datosFinales.getTarifa() != null)
                        viaje.setTarifa(datosFinales.getTarifa());

                    return conductoresRepository.findById(viaje.getConductorId())
                            .flatMap(conductor -> {
                                conductor.setEstado(EstadoConductor.ACTIVO);
                                return conductoresRepository.save(conductor);
                            })
                            .then(viajesRepository.save(viaje));
                });
    }

    // RFC1: Consultar Histórico
    public Flux<Viajes> consultarHistorico(String pasajeroId) {
        return viajesRepository.findByPasajeroId(pasajeroId);
    }

    // Métodos existentes (mantener compatibilidad si es necesario)
    public Flux<Viajes> obtenerTodosLosViajes() {
        return viajesRepository.findAll();
    }

    public Mono<Viajes> obtenerViajePorId(String id) {
        return viajesRepository.findById(id);
    }

    public Mono<Viajes> crearViaje(Viajes viaje) {
        return viajesRepository.save(viaje);
    }

    public Mono<Viajes> actualizarViaje(String id, Viajes viaje) {
        viaje.setId(id);
        return viajesRepository.save(viaje);
    }

    public Mono<Void> eliminarViaje(String id) {
        return viajesRepository.deleteById(id);
    }

    public Flux<Viajes> obtenerViajesPorConductor(String conductorId) {
        return viajesRepository.findByConductorId(conductorId);
    }

    public Flux<Viajes> obtenerViajesPorEstado(String estado) {
        return viajesRepository.findByEstado(estado);
    }

    // RFC3: Estadísticas de uso de servicios por ciudad y rango de fechas
    public Flux<com.alpescab.dto.EstadisticaServicioDTO> obtenerEstadisticasServicio(
            String ciudadId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        log.info("Obteniendo estadísticas para ciudad: {}, desde: {} hasta: {}",
                ciudadId, fechaInicio, fechaFin);

        // Primero obtenemos el total de servicios para calcular porcentajes
        return viajesRepository.findByCiudadIdAndFechaInicioBetween(ciudadId, fechaInicio, fechaFin)
                .collectList()
                .flatMapMany(viajes -> {
                    long totalServicios = viajes.size();

                    if (totalServicios == 0) {
                        return Flux.empty();
                    }

                    // Agrupar por tipo de servicio y contar
                    java.util.Map<String, Long> serviciosPorTipo = viajes.stream()
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Viajes::getTipoServicio,
                                    java.util.stream.Collectors.counting()));

                    // Convertir a DTOs con porcentajes y ordenar por cantidad descendente
                    return Flux.fromIterable(serviciosPorTipo.entrySet())
                            .map(entry -> {
                                String tipoServicio = entry.getKey();
                                Long cantidad = entry.getValue();
                                Double porcentaje = (cantidad * 100.0) / totalServicios;
                                return new com.alpescab.dto.EstadisticaServicioDTO(
                                        tipoServicio, cantidad, porcentaje);
                            })
                            .sort((a, b) -> Long.compare(b.getCantidadServicios(), a.getCantidadServicios()));
                });
    }
}
