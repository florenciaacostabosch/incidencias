package com.florencia.incidencias.dto;

import com.florencia.incidencias.model.Estado;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoRequest(
        @NotNull(message = "El estado es obligatorio")
        Estado estado
) {
}