package com.florencia.incidencias.repository;

import com.florencia.incidencias.model.Estado;
import com.florencia.incidencias.model.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    List<Incidencia> findByEstado(Estado estado);
}