package com.alpescab.service;

import com.alpescab.model.MetodosPago;
import com.alpescab.model.Usuarios;
import com.alpescab.repository.UsuariosRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UsuariosService {

    private final UsuariosRepository repository;

    public UsuariosService(UsuariosRepository repository) {
        this.repository = repository;
    }

    public Mono<Usuarios> save(Usuarios usuario) {
        return repository.existsByCorreo(usuario.getCorreo())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("El correo electrónico ya está registrado."));
                    }
                    // Asignar fecha de creación si es un usuario nuevo
                    if (usuario.getUsuario_id() == null) {
                        usuario.setFecha_creado(new Date());
                    }
                    return repository.save(usuario);
                });
    }

    public Flux<Usuarios> findAll() {
        return repository.findAll();
    }

    public Mono<Usuarios> findById(String id) {
        return repository.findById(id);
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    public Mono<Usuarios> addMetodoPago(String  usuarioId, MetodosPago nuevoMetodo) {
        return repository.findById(usuarioId)
                .flatMap(usuario -> {
                    if (usuario.getMetodosPago() == null) {
                        usuario.setMetodosPago(new ArrayList<>());
                    }
                    usuario.getMetodosPago().add(nuevoMetodo);
                    return repository.save(usuario);
                });
    }
}
