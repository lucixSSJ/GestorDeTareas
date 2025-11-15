-- drop database gestor_tareas;

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS gestor_tareas;
USE gestor_tareas;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    ultimo_login DATETIME NULL,
    notificaciones_vencimiento BOOLEAN DEFAULT TRUE,
    dias_antes_vencimiento INT DEFAULT 1
);

-- Tabla de categorías de tareas
CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre_categoria VARCHAR(100) NOT NULL,
    color VARCHAR(7) DEFAULT '#007bff',
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    UNIQUE KEY unique_categoria_usuario (id_usuario, nombre_categoria)
);

-- Tabla principal de tareas
CREATE TABLE tareas (
    id_tarea INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_categoria INT NULL,
    nombre_tarea VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_limite DATETIME NOT NULL,
    fecha_completada DATETIME NULL,
    estado ENUM('pendiente', 'en_progreso', 'completada', 'archivada', 'vencida') DEFAULT 'pendiente',
    prioridad ENUM('baja', 'media', 'alta', 'urgente') DEFAULT 'media',
    recordatorio_enviado BOOLEAN DEFAULT FALSE,
    fecha_archivado DATETIME NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE SET NULL,
    INDEX idx_usuario_estado (id_usuario, estado),
    INDEX idx_fecha_limite (fecha_limite),
    INDEX idx_estado (estado)
);

-- Tabla para archivos adjuntos
CREATE TABLE archivos_adjuntos (
    id_archivo INT AUTO_INCREMENT PRIMARY KEY,
    id_tarea INT NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    tipo_archivo VARCHAR(100) NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    tamaño BIGINT NOT NULL,
    fecha_subida DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_tarea) REFERENCES tareas(id_tarea) ON DELETE CASCADE,
    INDEX idx_tarea (id_tarea)
);

-- Tabla para imágenes
CREATE TABLE imagenes_tareas (
    id_imagen INT AUTO_INCREMENT PRIMARY KEY,
    id_tarea INT NOT NULL,
    nombre_imagen VARCHAR(255) NOT NULL,
    tipo_imagen VARCHAR(100) NOT NULL,
    ruta_imagen VARCHAR(500) NOT NULL,
    tamaño BIGINT NOT NULL,
    es_principal BOOLEAN DEFAULT FALSE,
    fecha_subida DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_tarea) REFERENCES tareas(id_tarea) ON DELETE CASCADE,
    INDEX idx_tarea (id_tarea)
);

-- Tabla para logs de notificaciones
CREATE TABLE logs_notificaciones (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_tarea INT NULL,
    tipo_notificacion ENUM('vencimiento', 'recordatorio') NOT NULL,
    email_destino VARCHAR(150) NOT NULL,
    asunto VARCHAR(255) NOT NULL,
    mensaje TEXT,
    fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    enviado_correctamente BOOLEAN DEFAULT FALSE,
    error_envio TEXT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_tarea) REFERENCES tareas(id_tarea) ON DELETE SET NULL,
    INDEX idx_usuario_fecha (id_usuario, fecha_envio),
    INDEX idx_tipo_fecha (tipo_notificacion, fecha_envio)
);

-- Tabla para exportaciones
CREATE TABLE exportaciones (
    id_exportacion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_tarea INT NULL,
    tipo_exportacion ENUM('ics', 'pdf') NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    fecha_exportacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    parametros_exportacion TEXT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_tarea) REFERENCES tareas(id_tarea) ON DELETE SET NULL,
    INDEX idx_usuario_tipo (id_usuario, tipo_exportacion)
);

-- Tabla para sesiones de usuarios
CREATE TABLE sesiones_usuarios (
    id_sesion VARCHAR(128) PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_ultima_actividad DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip_conexion VARCHAR(45),
    user_agent TEXT,
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    INDEX idx_usuario_activa (id_usuario, activa),
    INDEX idx_fecha_actividad (fecha_ultima_actividad)
);

-- Insertar datos de ejemplo
INSERT INTO usuarios (nombres, apellidos, email, username, password_hash, notificaciones_vencimiento, dias_antes_vencimiento) VALUES 
('Luciano', 'Bances', 'luciano@gmail.com', 'luciano', 'luciano', TRUE, 1),
('María', 'Gómez', 'maria.gomez@email.com', 'mariagomez', 'prueba', TRUE, 2);

INSERT INTO categorias (id_usuario, nombre_categoria, color) VALUES 
(1, 'Trabajo', '#007bff'),
(1, 'Personal', '#28a745'),
(2, 'Estudio', '#dc3545');

INSERT INTO tareas (id_usuario, id_categoria, nombre_tarea, descripcion, fecha_limite, prioridad) VALUES 
(1, 1, 'Reunión de equipo', 'Preparar presentación para la reunión semanal', DATE_ADD(NOW(), INTERVAL 2 DAY), 'alta'),
(1, 2, 'Compras supermercado', 'Comprar alimentos para la semana', DATE_ADD(NOW(), INTERVAL 1 DAY), 'media'),
(2, 3, 'Estudiar para examen', 'Repasar capítulos 5 al 8', DATE_ADD(NOW(), INTERVAL 5 DAY), 'alta');

-- Crear vistas útiles
CREATE VIEW vista_tareas_usuario AS
SELECT 
    t.*,
    u.nombres,
    u.apellidos,
    u.email,
    u.notificaciones_vencimiento,
    u.dias_antes_vencimiento,
    c.nombre_categoria,
    c.color,
    DATEDIFF(t.fecha_limite, NOW()) as dias_restantes,
    CASE 
        WHEN t.fecha_limite < NOW() AND t.estado != 'completada' THEN 'vencida'
        WHEN DATEDIFF(t.fecha_limite, NOW()) <= u.dias_antes_vencimiento AND t.estado = 'pendiente' THEN 'por_vencer'
        ELSE 'normal'
    END as estado_vencimiento
FROM tareas t
JOIN usuarios u ON t.id_usuario = u.id_usuario
LEFT JOIN categorias c ON t.id_categoria = c.id_categoria;

CREATE VIEW vista_tareas_pendientes AS
SELECT * FROM vista_tareas_usuario 
WHERE estado IN ('pendiente', 'en_progreso') 
ORDER BY fecha_limite ASC;

CREATE VIEW vista_tareas_vencidas AS
SELECT * FROM vista_tareas_usuario 
WHERE fecha_limite < NOW() AND estado NOT IN ('completada', 'archivada');

CREATE VIEW vista_tareas_por_vencer AS
SELECT * FROM vista_tareas_usuario 
WHERE estado IN ('pendiente', 'en_progreso')
AND fecha_limite BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL dias_antes_vencimiento DAY);

-- Crear procedimientos almacenados útiles
DELIMITER //

CREATE PROCEDURE sp_actualizar_estados_vencidos()
BEGIN
    UPDATE tareas 
    SET estado = 'vencida' 
    WHERE fecha_limite < NOW() 
    AND estado IN ('pendiente', 'en_progreso');
END //

CREATE PROCEDURE sp_obtener_tareas_para_recordatorio()
BEGIN
    -- Tareas que están por vencer según la configuración de cada usuario
    SELECT vt.* 
    FROM vista_tareas_por_vencer vt
    WHERE vt.notificaciones_vencimiento = TRUE
    AND vt.recordatorio_enviado = FALSE;
END //

DELIMITER ;

-- Crear triggers
DELIMITER //

CREATE TRIGGER before_tarea_insert
BEFORE INSERT ON tareas
FOR EACH ROW
BEGIN
    IF NEW.fecha_limite < NOW() THEN
        SET NEW.estado = 'vencida';
    END IF;
END //

DELIMITER ;