package com.traveling.backend.controller;

import com.traveling.backend.model.Actividad;
import com.traveling.backend.service.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Expone el itinerario de un viaje: sus actividades ordenadas por fecha y
 * hora (equivalente a consultaActsPorViaje3.sql, pero resuelto vía JPA).
 */
@RestController
@RequestMapping("/itinerario")
@CrossOrigin(origins = "*")
public class ItinerarioController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping("/{viajeId}")
    public ResponseEntity<List<Actividad>> obtenerItinerario(@PathVariable Long viajeId) {
        return ResponseEntity.ok(actividadService.obtenerItinerarioOrdenado(viajeId));
    }
}
