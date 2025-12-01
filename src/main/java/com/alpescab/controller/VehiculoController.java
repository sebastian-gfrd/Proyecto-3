package com.alpescab.controller;

import com.alpescab.model.Vehiculo;
import com.alpescab.service.VehiculoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService service;

    public VehiculoController(VehiculoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Vehiculo>> crearVehiculo(@RequestBody Vehiculo vehiculo, UriComponentsBuilder uriBuilder) {
        return service.crearVehiculo(vehiculo)
                .map(creado -> {
                    URI location = uriBuilder.path("/{id}")
                            .buildAndExpand(creado.getId())
                            .toUri();
                    return ResponseEntity.created(location).body(creado);
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Vehiculo>> obtenerVehiculoPorId(@PathVariable String id) {
        return service.obtenerVehiculoPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Vehiculo> obtenerTodos() {
        return service.obtenerTodos();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Vehiculo>> actualizarVehiculo(@PathVariable String id, @RequestBody Vehiculo vehiculoDetalles) {
        return service.actualizarVehiculo(id, vehiculoDetalles)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarVehiculo(@PathVariable String id) {
        return service.eliminarVehiculo(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/conductor/{conductorId}")
    public Flux<Vehiculo> obtenerVehiculosPorConductor(@PathVariable String conductorId) {
        return service.obtenerVehiculosPorConductor(conductorId);
    }
}