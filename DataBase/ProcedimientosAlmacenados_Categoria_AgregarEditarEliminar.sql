use gestor_tareas;
-- drop
-- Eliminar columna color de la tabla categorias
ALTER TABLE categorias DROP COLUMN color; -- color es innecesario


-- Procedimiento para obtener categorías de un usuario
DROP PROCEDURE IF EXISTS sp_obtener_categorias_usuario;

DELIMITER //
CREATE PROCEDURE sp_obtener_categorias_usuario(IN p_id_usuario INT)
BEGIN
    SELECT id_categoria, nombre_categoria, fecha_creacion
    FROM categorias 
    WHERE id_usuario = p_id_usuario
    ORDER BY nombre_categoria;
END //
DELIMITER ;



-- Procedimiento para crear categoría
DROP PROCEDURE IF EXISTS sp_crear_categoria;

DELIMITER //
CREATE PROCEDURE sp_crear_categoria(
    IN p_id_usuario INT,
    IN p_nombre_categoria VARCHAR(100),
    OUT p_resultado INT,
    OUT p_mensaje VARCHAR(255)
)
BEGIN
    DECLARE categoria_existente INT DEFAULT 0;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_resultado = 0;
        SET p_mensaje = 'Error en el servidor';
    END;
    
    START TRANSACTION;
    
    -- Verificar si la categoría ya existe para este usuario
    SELECT COUNT(*) INTO categoria_existente 
    FROM categorias 
    WHERE id_usuario = p_id_usuario AND nombre_categoria = p_nombre_categoria;
    
    IF categoria_existente > 0 THEN
        SET p_resultado = 0;
        SET p_mensaje = 'Ya existe una categoría con ese nombre';
    ELSE
        -- Insertar nueva categoría
        INSERT INTO categorias (id_usuario, nombre_categoria)
        VALUES (p_id_usuario, p_nombre_categoria);
        
        SET p_resultado = 1;
        SET p_mensaje = 'Categoría creada exitosamente';
        
        COMMIT;
    END IF;
END //
DELIMITER ;




-- Procedimiento para actualizar categoría
DROP PROCEDURE IF EXISTS sp_actualizar_categoria;

DELIMITER //
CREATE PROCEDURE sp_actualizar_categoria(
    IN p_id_categoria INT,
    IN p_nombre_categoria VARCHAR(100),
    OUT p_resultado INT,
    OUT p_mensaje VARCHAR(255)
)
BEGIN
    DECLARE categoria_existente INT DEFAULT 0;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_resultado = 0;
        SET p_mensaje = 'Error en el servidor';
    END;
    
    START TRANSACTION;
    
    -- Verificar si ya existe otra categoría con el mismo nombre (mismo usuario)
    SELECT COUNT(*) INTO categoria_existente 
    FROM categorias c1
    INNER JOIN categorias c2 ON c1.id_usuario = c2.id_usuario
    WHERE c1.id_categoria = p_id_categoria 
    AND c2.nombre_categoria = p_nombre_categoria 
    AND c2.id_categoria != p_id_categoria;
    
    IF categoria_existente > 0 THEN
        SET p_resultado = 0;
        SET p_mensaje = 'Ya existe otra categoría con ese nombre';
    ELSE
        -- Actualizar categoría
        UPDATE categorias 
        SET nombre_categoria = p_nombre_categoria
        WHERE id_categoria = p_id_categoria;
        
        SET p_resultado = 1;
        SET p_mensaje = 'Categoría actualizada exitosamente';
        
        COMMIT;
    END IF;
END //
DELIMITER ;






-- Procedimiento para eliminar categoría
DROP PROCEDURE IF EXISTS sp_eliminar_categoria;

DELIMITER //
CREATE PROCEDURE sp_eliminar_categoria(
    IN p_id_categoria INT,
    OUT p_resultado INT,
    OUT p_mensaje VARCHAR(255)
)
BEGIN
    DECLARE tareas_asociadas INT DEFAULT 0;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_resultado = 0;
        SET p_mensaje = 'Error en el servidor';
    END;
    
    START TRANSACTION;
    
    -- Verificar si hay tareas asociadas a esta categoría
    SELECT COUNT(*) INTO tareas_asociadas 
    FROM tareas 
    WHERE id_categoria = p_id_categoria;
    
    IF tareas_asociadas > 0 THEN
        SET p_resultado = 0;
        SET p_mensaje = 'No se puede eliminar la categoría porque tiene tareas asociadas';
    ELSE
        -- Eliminar categoría
        DELETE FROM categorias WHERE id_categoria = p_id_categoria;
        
        SET p_resultado = 1;
        SET p_mensaje = 'Categoría eliminada exitosamente';
        
        COMMIT;
    END IF;
END //
DELIMITER ;



-- procedimiento para obtener categorías con conteo de tareas
DROP PROCEDURE IF EXISTS sp_obtener_categorias_con_conteo;

DELIMITER //
CREATE PROCEDURE sp_obtener_categorias_con_conteo(IN p_id_usuario INT)
BEGIN
    SELECT 
        c.id_categoria,
        c.id_usuario,
        c.nombre_categoria,
        c.fecha_creacion,
        COUNT(t.id_tarea) as numero_tareas
    FROM categorias c
    LEFT JOIN tareas t ON c.id_categoria = t.id_categoria
    WHERE c.id_usuario = p_id_usuario
    GROUP BY c.id_categoria, c.id_usuario, c.nombre_categoria, c.fecha_creacion
    ORDER BY c.nombre_categoria;
END //
DELIMITER ;