package com.traveling.backend.controller;

import com.traveling.backend.model.Actividad;
import com.traveling.backend.service.ActividadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actividades")
@CrossOrigin(origins = "*")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @PostMapping
    public ResponseEntity<Actividad> crearActividad(
            @Valid @RequestBody Actividad actividad) {

        return ResponseEntity.ok(
                actividadService.guardarActividad(actividad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actividad> actualizarActividad(
            @PathVariable Long id,
            @Valid @RequestBody Actividad actividad) {

        return ResponseEntity.ok(
                actividadService.actualizarActividad(id, actividad));
    }

    @GetMapping("/viaje/{viajeId}")
    public ResponseEntity<List<Actividad>> obtenerActividadesPorViaje(
            @PathVariable Long viajeId) {

        return ResponseEntity.ok(
                actividadService.obtenerActividadesPorViaje(viajeId));
    }

    // TRAV-50 - Obtener costo total del viaje
    @GetMapping("/viaje/{viajeId}/costo-total")
    public ResponseEntity<Double> obtenerCostoTotal(
            @PathVariable Long viajeId) {

        return ResponseEntity.ok(
                actividadService.calcularCostoTotal(viajeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable Long id) {
        actividadService.eliminarActividad(id); 
        return ResponseEntity.noContent().build();
    }
}