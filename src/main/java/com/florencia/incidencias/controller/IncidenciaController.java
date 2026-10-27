package com.florencia.incidencias.controller;

import com.florencia.incidencias.dto.CambiarEstadoRequest;
import com.florencia.incidencias.dto.CrearIncidenciaRequest;
import com.florencia.incidencias.dto.IncidenciaResponse;
import com.florencia.incidencias.model.Estado;
import com.florencia.incidencias.service.IncidenciaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaController {

    private final IncidenciaService servicio;

    public IncidenciaController(IncidenciaService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<IncidenciaResponse> crear(@Valid @RequestBody CrearIncidenciaRequest request) {
        IncidenciaResponse creada = servicio.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @GetMapping
    public List<IncidenciaResponse> listar(@RequestParam(required = false) Estado estado) {
        return servicio.listar(estado);
    }

    @PatchMapping("/{id}/estado")
    public IncidenciaResponse cambiarEstado(@PathVariable Long id,
                                            @Valid @RequestBody CambiarEstadoRequest request) {
        return servicio.cambiarEstado(id, request.estado());
    }
}