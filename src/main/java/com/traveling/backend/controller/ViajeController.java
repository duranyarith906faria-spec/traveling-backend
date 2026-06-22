package com.traveling.backend.controller;

import com.traveling.backend.model.Viaje;
import com.traveling.backend.service.ViajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/viajes")
@CrossOrigin(origins = "*")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @GetMapping
    public ResponseEntity<List<Viaje>> listarViajes() {
        return ResponseEntity.ok(viajeService.listarViajes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> buscarViaje(@PathVariable Long id) {
        return viajeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Viaje> crearViaje(@Valid @RequestBody Viaje viaje) {
        Viaje nuevoViaje = viajeService.crearViaje(viaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoViaje);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Viaje> editarViaje(@PathVariable Long id, @Valid @RequestBody Viaje viaje) {
        try {
            return ResponseEntity.ok(viajeService.editarViaje(id, viaje));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarViaje(@PathVariable Long id) {
        try {
            viajeService.eliminarViaje(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
