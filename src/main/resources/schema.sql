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

INSERT INTO usuario (nombre, email) VALUES
    ('Ana López',   'ana.lopez@ejemplo.com'),
    ('Carlos Ruiz', 'carlos.ruiz@ejemplo.com');

INSERT INTO incidencia (titulo, descripcion, prioridad, estado, usuario_id) VALUES
    ('No funciona la VPN',      'Error de conexión desde casa',            'ALTA',  'ABIERTA',  1),
    ('Cambiar teclado',         'Teclado con teclas rotas',                'BAJA',  'EN_CURSO', 2),
    ('Alta de usuario nuevo',   'Crear cuenta para nueva incorporación',   'MEDIA', 'CERRADA',  1),
    ('Impresora sin tóner',     'La impresora de la planta 2 no imprime',  'MEDIA', 'ABIERTA',  NULL);