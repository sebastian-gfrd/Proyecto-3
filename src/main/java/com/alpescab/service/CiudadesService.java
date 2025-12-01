package com.alpescab.service;

import com.alpescab.model.Ciudades;
import com.alpescab.repository.CiudadesRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CiudadesService {

    private final CiudadesRepository repo;

    public CiudadesService(CiudadesRepository repo) {
        this.repo = repo;
    }

    public Mono<Ciudades> save(Ciudades ciudad) {
        return repo.save(ciudad);
    }

    public Flux<Ciudades> findAll() {
        return repo.findAll();
    }

    public Mono<Ciudades> findById(String id) {
        return repo.findById(id);
    }

    public Mono<Void> deleteById(String id) {
        return repo.deleteById(id);
    }

    public Flux<Ciudades> findByNombre(String nombre) {
        return repo.findByNombre(nombre);
    }
}