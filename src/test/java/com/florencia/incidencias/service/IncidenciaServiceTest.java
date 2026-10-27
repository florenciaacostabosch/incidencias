package com.florencia.incidencias.service;

import com.florencia.incidencias.dto.CrearIncidenciaRequest;
import com.florencia.incidencias.dto.IncidenciaResponse;
import com.florencia.incidencias.exception.IncidenciaCerradaException;
import com.florencia.incidencias.model.Estado;
import com.florencia.incidencias.model.Incidencia;
import com.florencia.incidencias.model.Prioridad;
import com.florencia.incidencias.repository.IncidenciaRepository;
import com.florencia.incidencias.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IncidenciaServiceTest {

    private IncidenciaRepository incidenciaRepository;
    private UsuarioRepository usuarioRepository;
    private IncidenciaService servicio;

    @BeforeEach
    void preparar() {
        incidenciaRepository = mock(IncidenciaRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        servicio = new IncidenciaService(incidenciaRepository, usuarioRepository);
    }

    @Test
    void noPermiteReabrirUnaIncidenciaCerrada() {
        Incidencia cerrada = new Incidencia("Titulo", "Desc", Prioridad.ALTA);
        cerrada.setEstado(Estado.CERRADA);
        when(incidenciaRepository.findById(1L)).thenReturn(Optional.of(cerrada));

        assertThrows(IncidenciaCerradaException.class,
                () -> servicio.cambiarEstado(1L, Estado.ABIERTA));
        assertEquals(Estado.CERRADA, cerrada.getEstado());
    }

    @Test
    void crearIncidenciaLaDejaEnEstadoAbierta() {
        when(incidenciaRepository.save(any(Incidencia.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        IncidenciaResponse respuesta = servicio.crear(
                new CrearIncidenciaRequest("Titulo", "Desc", Prioridad.MEDIA, null));

        assertEquals(Estado.ABIERTA, respuesta.estado());
        verify(usuarioRepository, never()).findById(any());
    }
}