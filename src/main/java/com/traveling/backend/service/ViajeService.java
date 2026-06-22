package com.traveling.backend.service;

import com.traveling.backend.model.Viaje;
import com.traveling.backend.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;

    public List<Viaje> listarViajes() {
        return viajeRepository.findAll();
    }

    public Optional<Viaje> buscarPorId(Long id) {
        return viajeRepository.findById(id);
    }

    public Viaje crearViaje(Viaje viaje) {
        validarViaje(viaje);
        return viajeRepository.save(viaje);
    }

    public Viaje editarViaje(Long id, Viaje datosActualizados) {
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));

        validarViaje(datosActualizados);

        viaje.setDestino(datosActualizados.getDestino());
        viaje.setPrecio(datosActualizados.getPrecio());
        viaje.setFechaInicio(datosActualizados.getFechaInicio());
        viaje.setFechaFin(datosActualizados.getFechaFin());
        return viajeRepository.save(viaje);
    }

    public void eliminarViaje(Long id) {
        if (!viajeRepository.existsById(id)) {
            throw new RuntimeException("Viaje no encontrado con id: " + id);
        }
        viajeRepository.deleteById(id);
    }

    private void validarViaje(Viaje viaje) {

        if (viaje.getFechaInicio() == null || viaje.getFechaFin() == null) {
            throw new RuntimeException("Las fechas de inicio y fin son obligatorias");
        }

        if (viaje.getFechaFin().isBefore(viaje.getFechaInicio())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        if (viaje.getFechaInicio().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de inicio no puede ser en el pasado");
        }

        if (viaje.getPrecio() == null || viaje.getPrecio() < 0) {
            throw new RuntimeException("El precio no puede ser negativo");
        }
    }
}
