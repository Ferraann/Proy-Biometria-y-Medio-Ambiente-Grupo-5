-- =======================================================
-- TABLA: credenciales
-- =======================================================
CREATE TABLE credenciales (
    id SERIAL PRIMARY KEY,
    credencial TEXT NOT NULL,
    permisos TEXT
);

-- =======================================================
-- TABLA: usuario
-- =======================================================
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150),
    gmail VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    credencial_id INT REFERENCES credenciales(id)
        ON UPDATE CASCADE,
    biometria BYTEA
);

-- =======================================================
-- TABLA: sensor
-- =======================================================
CREATE TABLE sensor (
    id SERIAL PRIMARY KEY,
    mac VARCHAR(50) UNIQUE NOT NULL,
    problema BOOLEAN DEFAULT FALSE
);

-- =======================================================
-- TABLA: tipo_medicion
-- =======================================================
CREATE TABLE tipo_medicion (
    id SERIAL PRIMARY KEY,
    medida VARCHAR(100) NOT NULL,
    unidad VARCHAR(50) NOT NULL,
    txt TEXT
);

-- =======================================================
-- TABLA: medicion
-- =======================================================
CREATE TABLE medicion (
    id SERIAL PRIMARY KEY,
    tipo_medicion_id INT NOT NULL REFERENCES tipo_medicion(id)
        ON UPDATE CASCADE,
    valor NUMERIC(10,2) NOT NULL,
    hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    localizacion VARCHAR(255),
    sensor_id INT NOT NULL REFERENCES sensor(id)
        ON UPDATE CASCADE
);

-- 🔍 Índice para acelerar consultas por sensor + hora (últimas mediciones)
CREATE INDEX idx_medicion_sensor_hora ON medicion(sensor_id, hora DESC);

-- =======================================================
-- TABLA: usuario_sensor
-- =======================================================
CREATE TABLE usuario_sensor (
    id_relacion SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuario(id)
        ON UPDATE CASCADE,
    sensor_id INT NOT NULL REFERENCES sensor(id)
        ON UPDATE CASCADE,
    actual BOOLEAN DEFAULT TRUE,
    inicio_relacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fin_relacion TIMESTAMP
);

-- 🔍 Índice útil para saber rápidamente qué sensores están activos por usuario
CREATE INDEX idx_usuario_sensor_actual ON usuario_sensor(usuario_id, actual);

-- =======================================================
-- TABLA: incidencias
-- =======================================================
CREATE TABLE incidencias (
    id SERIAL PRIMARY KEY,
    id_tecnico INT REFERENCES usuario(id)
        ON UPDATE CASCADE,
    id_user INT REFERENCES usuario(id)
        ON UPDATE CASCADE,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_finalizacion TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE
);

-- 🔍 Índice para filtrar incidencias activas rápidamente
CREATE INDEX idx_incidencias_activa ON incidencias(activa);

-- 🔍 Índice para consultas por técnico o usuario
CREATE INDEX idx_incidencias_usuarios ON incidencias(id_tecnico, id_user);

-- =======================================================
-- TABLA: fotos_incidencia
-- =======================================================
CREATE TABLE fotos_incidencia (
    id SERIAL PRIMARY KEY,
    incidencia_id INT NOT NULL REFERENCES incidencias(id)
        ON UPDATE CASCADE,
    foto BYTEA NOT NULL
);
