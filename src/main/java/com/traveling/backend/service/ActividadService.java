package com.traveling.backend.service;

import com.traveling.backend.model.Actividad;
import com.traveling.backend.model.Viaje;
import com.traveling.backend.repository.ActividadRepository;
import com.traveling.backend.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ViajeRepository viajeRepository;

    public Actividad guardarActividad(Actividad actividad) {

        Viaje viaje = validarActividad(actividad, null);
        actividad.setViaje(viaje);

        return actividadRepository.save(actividad);
    }

    public Actividad actualizarActividad(Long id, Actividad actividadActualizada) {

        Actividad actividadExistente = actividadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        Viaje viaje = validarActividad(actividadActualizada, id);

        actividadExistente.setNombre(actividadActualizada.getNombre());
        actividadExistente.setFecha(actividadActualizada.getFecha());
        actividadExistente.setHora(actividadActualizada.getHora());
        actividadExistente.setCosto(actividadActualizada.getCosto());
        actividadExistente.setViaje(viaje);

        return actividadRepository.save(actividadExistente);
    }

    /**
     * Valida la actividad y devuelve el Viaje real (cargado desde la BD) al
     * que debe quedar asociada. idActual es el id de la actividad que se está
     * editando (null si es una actividad nueva), para no comparar contra sí misma
     * al validar el solapamiento de horario.
     */
    private Viaje validarActividad(Actividad actividad, Long idActual) {

        if (actividad.getNombre() == null || actividad.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la actividad es obligatorio");
        }

        if (actividad.getFecha() == null) {
            throw new RuntimeException("La fecha de la actividad es obligatoria");
        }

        if (actividad.getHora() == null) {
            throw new RuntimeException("La hora de la actividad es obligatoria");
        }

        if (actividad.getCosto() == null || actividad.getCosto() < 0) {
            throw new RuntimeException("El costo no puede ser negativo");
        }

        if (actividad.getViaje() == null || actividad.getViaje().getId() == null) {
            throw new RuntimeException("Debe asociar la actividad a un viaje");
        }

        Viaje viaje = viajeRepository.findById(actividad.getViaje().getId())
                .orElseThrow(() -> new RuntimeException("El viaje asociado no existe"));

        // La fecha de la actividad debe estar dentro del rango del viaje
        if (actividad.getFecha().isBefore(viaje.getFechaInicio()) ||
                actividad.getFecha().isAfter(viaje.getFechaFin())) {
            throw new RuntimeException(
                    "La fecha de la actividad debe estar dentro del rango del viaje (" +
                            viaje.getFechaInicio() + " - " + viaje.getFechaFin() + ")");
        }

        // Dos actividades del mismo viaje no pueden empezar en la misma fecha y hora
        List<Actividad> choques = actividadRepository.findByViajeIdAndFechaAndHora(
                viaje.getId(), actividad.getFecha(), actividad.getHora());

        boolean haySolapamiento = choques.stream()
                .anyMatch(a -> idActual == null || !a.getId().equals(idActual));

        if (haySolapamiento) {
            throw new RuntimeException(
                    "Ya existe una actividad programada para ese mismo día y hora en este viaje");
        }

        return viaje;
    }

    public List<Actividad> obtenerActividadesPorViaje(Long viajeId) {
        return actividadRepository.findByViajeId(viajeId);
    }

    public List<Actividad> obtenerItinerarioOrdenado(Long viajeId) {
        return actividadRepository.findByViajeIdOrderByFechaAscHoraAsc(viajeId);
    }

    public Double calcularCostoTotal(Long viajeId) {

        List<Actividad> actividades = actividadRepository.findByViajeId(viajeId);

        return actividades.stream()
                .mapToDouble(Actividad::getCosto)
                .sum();
    }
}
