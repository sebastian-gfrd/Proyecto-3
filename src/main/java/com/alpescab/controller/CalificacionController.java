package com.alpescab.controller;

import com.alpescab.model.Calificacion;
import com.alpescab.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @GetMapping
    public Flux<Calificacion> obtenerTodas() {
        return calificacionService.obtenerTodas();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Calificacion> registrarCalificacion(@RequestBody Calificacion calificacion) {
        return calificacionService.registrarCalificacion(calificacion);
    }
}
