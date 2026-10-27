package com.florencia.incidencias.dto;

import com.florencia.incidencias.model.Estado;
import com.florencia.incidencias.model.Incidencia;
import com.florencia.incidencias.model.Prioridad;

import java.time.LocalDateTime;

public record IncidenciaResponse(
        Long id,
        String titulo,
        String descripcion,
        Prioridad prioridad,
        Estado estado,
        LocalDateTime fechaCreacion,
        Long usuarioId,
        String usuarioNombre
) {
    public static IncidenciaResponse desde(Incidencia i) {
        return new IncidenciaResponse(
                i.getId(),
                i.getTitulo(),
                i.getDescripcion(),
                i.getPrioridad(),
                i.getEstado(),
                i.getFechaCreacion(),
                i.getUsuarioAsignado() != null ? i.getUsuarioAsignado().getId() : null,
                i.getUsuarioAsignado() != null ? i.getUsuarioAsignado().getNombre() : null
        );
    }
}