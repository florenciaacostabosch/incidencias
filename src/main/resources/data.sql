INSERT INTO usuario (nombre, email) VALUES
    ('Ana López',   'ana.lopez@ejemplo.com'),
    ('Carlos Ruiz', 'carlos.ruiz@ejemplo.com');

INSERT INTO incidencia (titulo, descripcion, prioridad, estado, usuario_id) VALUES
    ('No funciona la VPN',      'Error de conexión desde casa',            'ALTA',  'ABIERTA',  1),
    ('Cambiar teclado',         'Teclado con teclas rotas',                'BAJA',  'EN_CURSO', 2),
    ('Alta de usuario nuevo',   'Crear cuenta para nueva incorporación',   'MEDIA', 'CERRADA',  1),
    ('Impresora sin tóner',     'La impresora de la planta 2 no imprime',  'MEDIA', 'ABIERTA',  NULL);