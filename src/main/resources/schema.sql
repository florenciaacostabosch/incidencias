CREATE TABLE usuario (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL,
    email   VARCHAR(150) NOT NULL,
    CONSTRAINT uq_usuario_email UNIQUE (email)
);

CREATE TABLE incidencia (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo         VARCHAR(100) NOT NULL,
    descripcion    VARCHAR(1000),
    prioridad      VARCHAR(20)  NOT NULL,
    estado         VARCHAR(20)  NOT NULL DEFAULT 'ABIERTA',
    fecha_creacion TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id     BIGINT,
    CONSTRAINT ck_incidencia_prioridad CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA')),
    CONSTRAINT ck_incidencia_estado    CHECK (estado IN ('ABIERTA', 'EN_CURSO', 'CERRADA')),
    CONSTRAINT fk_incidencia_usuario   FOREIGN KEY (usuario_id)
        REFERENCES usuario (id) ON DELETE SET NULL
);

CREATE INDEX idx_incidencia_estado     ON incidencia (estado);
CREATE INDEX idx_incidencia_usuario_id ON incidencia (usuario_id);