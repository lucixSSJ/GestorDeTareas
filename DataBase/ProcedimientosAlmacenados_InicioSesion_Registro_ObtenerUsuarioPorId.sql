use gestor_tareas;

-- Procedimiento para inicio de sesión
DROP PROCEDURE IF EXISTS sp_login_usuario;

DELIMITER //
CREATE PROCEDURE sp_login_usuario(
    IN p_username VARCHAR(50)
)
BEGIN
    SELECT 
        id_usuario, 
        nombres, 
        apellidos, 
        email, 
        username,
        password_hash,
        fecha_registro,
        activo,
        ultimo_login,
        notificaciones_vencimiento, 
        dias_antes_vencimiento
    FROM usuarios 
    WHERE username = p_username 
    AND activo = TRUE;
END //

DELIMITER ;

-- Procedimiento para registrar usuario
DROP PROCEDURE IF EXISTS sp_registrar_usuario;

DELIMITER //

CREATE PROCEDURE sp_registrar_usuario(
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_email VARCHAR(150),
    IN p_username VARCHAR(50),
    IN p_password_hash VARCHAR(255),
    OUT p_resultado INT,
    OUT p_mensaje VARCHAR(255)
)
BEGIN
    DECLARE usuario_existente INT DEFAULT 0;
    DECLARE email_existente INT DEFAULT 0;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_resultado = 0;
        SET p_mensaje = 'Error en el servidor';
    END;
    
    START TRANSACTION;
    
    -- Verificar si el username ya existe
    SELECT COUNT(*) INTO usuario_existente 
    FROM usuarios WHERE username = p_username;
    
    -- Verificar si el email ya existe
    SELECT COUNT(*) INTO email_existente 
    FROM usuarios WHERE email = p_email;
    
    IF usuario_existente > 0 THEN
        SET p_resultado = 0;
        SET p_mensaje = 'El nombre de usuario ya está en uso';
    ELSEIF email_existente > 0 THEN
        SET p_resultado = 0;
        SET p_mensaje = 'El correo electrónico ya está registrado';
    ELSE
        -- Insertar nuevo usuario CON EL HASH QUE ENVÍA JAVA
        INSERT INTO usuarios (nombres, apellidos, email, username, password_hash)
        VALUES (p_nombres, p_apellidos, p_email, p_username, p_password_hash);
        
        SET p_resultado = 1;
        SET p_mensaje = 'Usuario registrado exitosamente';
        
        COMMIT;
    END IF;
END //

DELIMITER ;


-- Procedimiento para obtener usuario por ID
DELIMITER //
CREATE PROCEDURE sp_obtener_usuario_por_id(IN p_id_usuario INT)
BEGIN
    SELECT id_usuario, nombres, apellidos, email, username,
           notificaciones_vencimiento, dias_antes_vencimiento,
           fecha_registro, ultimo_login
    FROM usuarios 
    WHERE id_usuario = p_id_usuario AND activo = TRUE;
END //
DELIMITER ;

-- h