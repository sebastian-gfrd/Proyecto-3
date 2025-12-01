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

    public VehiculoService(VehiculoRepository repo) {
        this.repo = repo;
    }

    public Mono<Vehiculo> crearVehiculo(Vehiculo vehiculo) {
        return repo.existsById(vehiculo.getId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("El vehículo con este ID ya existe."));
                    }
                    return repo.save(vehiculo);
                });
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
}