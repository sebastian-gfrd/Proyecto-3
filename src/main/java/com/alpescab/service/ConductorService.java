package com.alpescab.service;

import com.alpescab.model.Conductor;
import com.alpescab.model.EstadoConductor;
import com.alpescab.repository.ConductoresRepository;
import com.alpescab.repository.UsuariosRepository;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ConductorService {

    private final ConductoresRepository ConductoresRepository;
    private final UsuariosRepository usuariosRepository;

    // Se inyecta el constructor de las interfaces de condcutor y usuario
    public ConductorService(ConductoresRepository ConductoresRepository, UsuariosRepository usuariosRepository) {
        this.ConductoresRepository = ConductoresRepository;
        this.usuariosRepository = usuariosRepository;
    }

    // RF2 registrar un conductor verificando que primero exista un usuario
    public Mono<Conductor> save(Conductor conductor) {

        return usuariosRepository.findById(String.valueOf(conductor.getUsuarioId())) // verifica que el usuario al que
                                                                                     // se asociará el conductor debe
                                                                                     // existir

                // Si el usuario no existe, se detiene el proceso con un error
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("No se puede crear el conductor, el usuario con ID "
                                + conductor.getUsuarioId() + " no existe.")))

                // En caso que el usuario sea valido se continúa con la validación siguiente
                .flatMap(usuarioEncontrado ->

                // Aqui se valida que no exista otro conductor con el mismo id asociado o el
                // mismo condcutor con otro id de usuario
                ConductoresRepository.findByUsuarioId(conductor.getUsuarioId())
                        .flatMap(existente -> Mono.error(
                                new IllegalArgumentException("Este usuario ya tiene un perfil de conductor.")))

                        // Si no existe un registro previo, se permite continuar con el registro
                        .switchIfEmpty(Mono.just(conductor))
                        .cast(Conductor.class))

                // Asigna los paramteros iniciales de cualquier conductor nuevo
                .flatMap(conductorValidado -> {
                    conductorValidado.setTotalViajes(0);
                    conductorValidado.setCalificacionAvg(5.0);
                    conductorValidado.setEstado(EstadoConductor.ACTIVO);

                    return ConductoresRepository.save(conductorValidado);
                });
    }

    // RFC2
    public Flux<Conductor> obtenerTop20() {
        return ConductoresRepository.findTop20ByOrderByTotalViajesDesc();
    }

    // RF6
    public Flux<Conductor> encontrarCercanos(double latitud, double longitud, double radioKm) {
        Point origen = new Point(longitud, latitud);
        Distance distancia = new Distance(radioKm, Metrics.KILOMETERS);

        return ConductoresRepository.findByUbicacionActualNearAndEstado(origen, distancia, EstadoConductor.ACTIVO);
    }

    // Búsqueda por ID (uso general)
    public Mono<Conductor> findById(String id) {
        return ConductoresRepository.findById(id);
    }

    public Flux<Conductor> findAll() {
        return ConductoresRepository.findAll();
    }
}