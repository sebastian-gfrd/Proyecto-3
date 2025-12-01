package com.alpescab.controller;

import com.alpescab.model.Ciudades;
import com.alpescab.service.CiudadesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadesController {

    private final CiudadesService service;

    public CiudadesController(CiudadesService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<Ciudades> create(@RequestBody Ciudades ciudad) {
        return service.save(ciudad);
    }

    @GetMapping
    public Flux<Ciudades> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Ciudades>> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Ciudades>> update(@PathVariable String id, @RequestBody Ciudades updated) {
        return service.findById(id)
                .flatMap(existing -> {
                    existing.setNombre(updated.getNombre());
                    existing.setPais(updated.getPais());
                    existing.setCiudad_id(id);
                    return service.save(existing);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.findById(id)
                .flatMap(existing ->
                        service.deleteById(id).then(Mono.just(ResponseEntity.noContent().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}