package com.alpescab.controller;

import com.alpescab.model.Tarifa;
import com.alpescab.service.TarifaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
public class TarifaController {

    private final TarifaService tarifaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Tarifa> crearTarifa(@RequestBody Tarifa tarifa) {
        return tarifaService.crearTarifa(tarifa);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Tarifa>> obtenerTarifaPorId(@PathVariable String id) {
        return tarifaService.obtenerTarifaPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Tarifa> obtenerTodasLasTarifas() {
        return tarifaService.obtenerTodasLasTarifas();
    }

    @GetMapping("/buscar")
    public Mono<ResponseEntity<Tarifa>> buscarTarifa(
            @RequestParam String ciudadId,
            @RequestParam String tipoServicio,
            @RequestParam String tipoVehiculo) {
        return tarifaService.obtenerTarifa(ciudadId, tipoServicio, tipoVehiculo)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Tarifa>> actualizarTarifa(
            @PathVariable String id,
            @RequestBody Tarifa tarifa) {
        return tarifaService.actualizarTarifa(id, tarifa)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarTarifa(@PathVariable String id) {
        return tarifaService.eliminarTarifa(id);
    }
}
