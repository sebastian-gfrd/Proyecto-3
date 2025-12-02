package com.alpescab.service;

import com.alpescab.model.Tarifa;
import com.alpescab.repository.TarifaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    // RF6 Support: Obtener tarifa aplicable
    public Mono<Tarifa> obtenerTarifa(String ciudadId, String tipoServicio, String tipoVehiculo) {
        return tarifaRepository.findByCiudadIdAndTipoServicioAndTipoVehiculo(ciudadId, tipoServicio, tipoVehiculo)
                .switchIfEmpty(Mono.error(new RuntimeException(
                        "No se encontró tarifa para ciudad: " + ciudadId +
                                ", servicio: " + tipoServicio +
                                ", vehículo: " + tipoVehiculo)));
    }

    public Mono<Tarifa> crearTarifa(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public Mono<Tarifa> obtenerTarifaPorId(String id) {
        return tarifaRepository.findById(id);
    }

    public Flux<Tarifa> obtenerTodasLasTarifas() {
        return tarifaRepository.findAll();
    }

    public Mono<Tarifa> actualizarTarifa(String id, Tarifa tarifa) {
        tarifa.setTarifaId(id);
        return tarifaRepository.save(tarifa);
    }

    public Mono<Void> eliminarTarifa(String id) {
        return tarifaRepository.deleteById(id);
    }
}
