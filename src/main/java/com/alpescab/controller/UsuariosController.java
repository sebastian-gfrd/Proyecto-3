package com.alpescab.controller;

import com.alpescab.model.MetodosPago;
import com.alpescab.model.Usuarios;
import com.alpescab.service.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private final UsuariosService service;

    public UsuariosController(UsuariosService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Usuarios> create(@RequestBody Usuarios usuario) {
        return service.save(usuario);
    }

    @GetMapping
    public Flux<Usuarios> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Usuarios>> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Usuarios>> update(@PathVariable String id, @RequestBody Usuarios usuario) {
        return service.findById(id)
                .flatMap(existingUser -> {
                    usuario.setUsuarioId(id);
                    // Copiar otros campos que se pueden actualizar
                    existingUser.setNombre(usuario.getNombre());
                    existingUser.setTelefono(usuario.getTelefono());
                    existingUser.setRol(usuario.getRol());
                    return service.save(existingUser);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.findById(id)
                .flatMap(existingUser ->
                        service.deleteById(id)
                                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/pago")
    public Mono<ResponseEntity<Usuarios>> addMetodoPago(@PathVariable String id, @RequestBody MetodosPago metodoPago) {
        return service.addMetodoPago(id, metodoPago)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
