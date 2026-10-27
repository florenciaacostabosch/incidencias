package com.florencia.incidencias.dto;

import com.florencia.incidencias.model.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearIncidenciaRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 100, message = "El título no puede superar los 100 caracteres")
        String titulo,

        @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
        String descripcion,

        @NotNull(message = "La prioridad es obligatoria")
        Prioridad prioridad,

        Long usuarioId
) {
}