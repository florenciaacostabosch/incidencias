package com.florencia.incidencias.exception;

public class IncidenciaCerradaException extends RuntimeException {
    public IncidenciaCerradaException(Long id) {
        super("La incidencia " + id + " está cerrada y no puede reabrirse");
    }
}