-- Base de datos para Diseño de patrones

-- 1. Crear la base de datos
CREATE DATABASE db_gestortareas;

-- 2. ejecutar lo siguiente
use db_gestortareas;

-- 3. finalmente ejecutar la creacion de las tablas
-- Table: Archivo
CREATE TABLE Archivo (
    archivo_id int auto_increment NOT NULL,
    nombre_archivo varchar(100) NOT NULL,
    ruta varchar(255) NOT NULL,
    tipo_archivo varchar(100)  NOT NULL,
    fecha_subida timestamp default current_timestamp not NULL,
    id_tarea int  NOT NULL,
    CONSTRAINT Archivo_pk PRIMARY KEY (archivo_id)
);

-- Table: Notificacion
CREATE TABLE Notificacion (
    notificacion_id int auto_increment NOT NULL,
    tipo enum('EMAIL','SMS','ALARMA') default 'EMAIL' NULL,
    asunto varchar(255)  NULL,
    mensaje text  NULL,
    fecha_creacion timestamp  NOT NULL,
    fecha_envio date NOT NULL,
    sonido varchar(255) NULL,
    id_tarea int  NOT NULL,
    id_usuario int  NOT NULL,
    CONSTRAINT Notificacion_pk PRIMARY KEY (notificacion_id)
);

-- Table: Tarea
CREATE TABLE Tarea (
    tarea_id int auto_increment NOT NULL,
    titulo varchar(100) not NULL,
    descripcion text NULL,
    estado enum('PENDIENTE','TERMINADA','ARCHIVADA') DEFAULT 'PENDIENTE' NULL,
    CONSTRAINT Tarea_pk PRIMARY KEY (tarea_id)
);

-- Table: Usuario
CREATE TABLE Usuario (
    usuario_id int auto_increment NOT NULL,
    nombres varchar(50) not NULL,
    apellidos varchar(50) not NULL,
    correo varchar(60) not NULL unique,
    username varchar(20)  NOT NULL unique,
    clave varchar(255) not NULL,
    telefono char(10) not NULL,
    rol_id int  NOT NULL,
    CONSTRAINT Usuario_pk PRIMARY KEY (usuario_id)
);

-- Table: rol
CREATE TABLE rol (
    id_rol int auto_increment NOT NULL,
    nombre_rol varchar(15)  NOT NULL,
    CONSTRAINT rol_pk PRIMARY KEY (id_rol)
);

-- Table: usuario_tarea
CREATE TABLE usuario_tarea (
    id_usuTarea int auto_increment  NOT NULL,
    usuario_id int  NOT NULL,
    tarea_id int  NOT NULL,
    fecha_asignacion timestamp  NOT NULL,
    fecha_limite date  NOT NULL,
    CONSTRAINT usuario_tarea_pk PRIMARY KEY (id_usuTarea)
);

-- foreign keys
-- Reference: Archivo_Tarea (table: Archivo)
ALTER TABLE Archivo ADD CONSTRAINT Archivo_Tarea FOREIGN KEY Archivo_Tarea (id_tarea)
    REFERENCES Tarea (tarea_id);

-- Reference: Notificacion_Usuario (table: Notificacion)
ALTER TABLE Notificacion ADD CONSTRAINT Notificacion_Usuario FOREIGN KEY Notificacion_Usuario (id_usuario)
    REFERENCES Usuario (usuario_id);

-- Reference: Table_4_Tarea (table: Notificacion)
ALTER TABLE Notificacion ADD CONSTRAINT Table_4_Tarea FOREIGN KEY Table_4_Tarea (id_tarea)
    REFERENCES Tarea (tarea_id);

-- Reference: Usuario_rol (table: Usuario)
ALTER TABLE Usuario ADD CONSTRAINT Usuario_rol FOREIGN KEY Usuario_rol (rol_id)
    REFERENCES rol (id_rol);

-- Reference: usuario_tarea_Tarea (table: usuario_tarea)
ALTER TABLE usuario_tarea ADD CONSTRAINT usuario_tarea_Tarea FOREIGN KEY usuario_tarea_Tarea (tarea_id)
    REFERENCES Tarea (tarea_id);

-- Reference: usuario_tarea_Usuario (table: usuario_tarea)
ALTER TABLE usuario_tarea ADD CONSTRAINT usuario_tarea_Usuario FOREIGN KEY usuario_tarea_Usuario (usuario_id)
    REFERENCES Usuario (usuario_id);

-- End of file.

