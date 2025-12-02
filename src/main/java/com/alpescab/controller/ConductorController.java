package com.alpescab.controller;

import com.alpescab.model.Conductor;
import com.alpescab.service.ConductorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/conductores")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorService service;

    // RF2
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Conductor> registrarConductor(@RequestBody Conductor conductor) {

        return service.save(conductor);
    }

    // RFC2
    @GetMapping("/top-20")
    public Flux<Conductor> obtenerTop20Conductores() {

        return service.obtenerTop20();
    }

    @GetMapping("/cercanos")
    public Flux<Conductor> buscarCercanos(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5") double radio // Por defecto 5km
    ) {
        return service.encontrarCercanos(lat, lon, radio);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Conductor>> obtenerConductor(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Conductor> obtenerTodos() {
        return service.findAll();
    }
}