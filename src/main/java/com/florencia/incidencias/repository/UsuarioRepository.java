package com.florencia.incidencias.repository;

import com.florencia.incidencias.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}