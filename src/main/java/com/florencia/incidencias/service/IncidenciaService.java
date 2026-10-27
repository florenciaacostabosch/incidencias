package com.florencia.incidencias.service;

import com.florencia.incidencias.dto.CrearIncidenciaRequest;
import com.florencia.incidencias.dto.IncidenciaResponse;
import com.florencia.incidencias.exception.IncidenciaCerradaException;
import com.florencia.incidencias.exception.IncidenciaNoEncontradaException;
import com.florencia.incidencias.exception.UsuarioNoEncontradoException;
import com.florencia.incidencias.model.Estado;
import com.florencia.incidencias.model.Incidencia;
import com.florencia.incidencias.model.Usuario;
import com.florencia.incidencias.repository.IncidenciaRepository;
import com.florencia.incidencias.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public IncidenciaService(IncidenciaRepository incidenciaRepository,
                             UsuarioRepository usuarioRepository) {
        this.incidenciaRepository = incidenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public IncidenciaResponse crear(CrearIncidenciaRequest request) {
        Incidencia incidencia = new Incidencia(
                request.titulo(), request.descripcion(), request.prioridad());

        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(request.usuarioId())
                    .orElseThrow(() -> new UsuarioNoEncontradoException(request.usuarioId()));
            incidencia.setUsuarioAsignado(usuario);
        }
        return IncidenciaResponse.desde(incidenciaRepository.save(incidencia));
    }

    @Transactional(readOnly = true)
    public List<IncidenciaResponse> listar(Estado estado) {
        List<Incidencia> incidencias = estado == null
                ? incidenciaRepository.findAll()
                : incidenciaRepository.findByEstado(estado);
        return incidencias.stream().map(IncidenciaResponse::desde).toList();
    }

    @Transactional
    public IncidenciaResponse cambiarEstado(Long id, Estado nuevoEstado) {
        Incidencia incidencia = incidenciaRepository.findById(id)
                .orElseThrow(() -> new IncidenciaNoEncontradaException(id));

        if (incidencia.getEstado() == Estado.CERRADA && nuevoEstado != Estado.CERRADA) {
            throw new IncidenciaCerradaException(id);
        }
        incidencia.setEstado(nuevoEstado);
        return IncidenciaResponse.desde(incidencia);
    }
}