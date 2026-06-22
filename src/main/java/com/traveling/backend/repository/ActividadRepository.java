package com.traveling.backend.repository;

import com.traveling.backend.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findByViajeId(Long viajeId);

    List<Actividad> findByViajeIdOrderByFechaAscHoraAsc(Long viajeId);

    List<Actividad> findByViajeIdAndFechaAndHora(Long viajeId, LocalDate fecha, LocalTime hora);
}
