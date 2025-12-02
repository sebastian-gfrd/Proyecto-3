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

    @GetMapping()
    public Flux<Conductor> obtenerTodosLosConductores() {
        return service.obtenerTodosLosConductores();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Conductor> registrarConductor(@RequestBody Conductor conductor) {
        return service.registrarConductor(conductor);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Conductor>> obtenerConductor(@PathVariable String id) {
        return service.obtenerConductorPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/top-20-servicios")
    public Flux<ConductorService.TopConductorResult> obtenerTop20Conductores() {
        return service.obtenerTop20Conductores();
    }
}
