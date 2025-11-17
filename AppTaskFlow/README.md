# 📋 TaskFlow - Sistema de Gestión de Tareas

## ✨ Funcionalidades Implementadas

### 🔐 Sistema de Autenticación
- **Login de usuarios** con validación de credenciales
- **Gestión de sesiones** de usuario
- **Usuario administrador por defecto**:
  - Usuario: `admin`
  - Contraseña: `admin123`
- **Seguridad**: Contraseñas encriptadas con SHA-256 + salt

### 📝 Gestión de Tareas (CRUD Completo)
- ✅ Crear nuevas tareas
- 👁️ Visualizar tareas existentes
- ✏️ Modificar tareas
- 🗑️ Eliminar tareas
- 📊 Categorización de tareas
- 📤 Exportar tareas

### 🗄️ Base de Datos Híbrida
- **Modo con Base de Datos**: MySQL (recomendado)
- **Modo Sin Base de Datos**: Almacenamiento en memoria (fallback automático)
- **Auto-detección**: La aplicación detecta automáticamente si MySQL está disponible

## 🚀 Instalación y Ejecución

### Opción 1: NetBeans IDE (Recomendado)
1. **Instalar NetBeans IDE** (si no lo tienes)
2. **Abrir el proyecto**:
   - Archivo → Abrir Proyecto
   - Seleccionar la carpeta `AppTaskFlow`
3. **Ejecutar**:
   - Click derecho en el proyecto → Ejecutar
   - O presionar `F6`

### Opción 2: Maven + Línea de Comandos
1. **Instalar Maven** (si no lo tienes)
2. **Compilar**:
   ```bash
   cd AppTaskFlow
   mvn clean compile
   ```
3. **Ejecutar**:
   ```bash
   mvn exec:java -Dexec.mainClass="appTaskFlow.Main"
   ```

### Opción 3: Ejecutable JAR
1. **Compilar con Maven**:
   ```bash
   mvn clean package
   ```
2. **Ejecutar**:
   ```bash
   java -jar target/AppTaskFlow-1.0-SNAPSHOT.jar
   ```

## 🗄️ Configuración de Base de Datos (Opcional)

### MySQL Setup
1. **Instalar MySQL** (si no lo tienes)
2. **Crear base de datos**:
   ```sql
   CREATE DATABASE gestor_tareas;
   USE gestor_tareas;
   
   CREATE TABLE usuarios (
       id_usuario INT PRIMARY KEY AUTO_INCREMENT,
       nombres VARCHAR(100) NOT NULL,
       apellidos VARCHAR(100) NOT NULL,
       email VARCHAR(150) UNIQUE NOT NULL,
       username VARCHAR(50) UNIQUE NOT NULL,
       password_hash VARCHAR(255) NOT NULL,
       fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
       ultimo_login DATETIME
   );
   
   CREATE TABLE tareas (
       id_tarea INT PRIMARY KEY AUTO_INCREMENT,
       titulo VARCHAR(200) NOT NULL,
       descripcion TEXT,
       fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
       fecha_vencimiento DATETIME,
       completada BOOLEAN DEFAULT FALSE,
       prioridad ENUM('ALTA', 'MEDIA', 'BAJA') DEFAULT 'MEDIA',
       categoria VARCHAR(50),
       id_usuario INT,
       FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
   );
   ```

3. **Configurar conexión** (si es necesario):
   - Archivo: `src/main/java/utilidad/DatabaseConnection.java`
   - Modificar URL, usuario y contraseña según tu configuración

### Funcionamiento Sin MySQL
- ✅ **La aplicación funciona perfectamente sin MySQL**
- 💾 **Datos en memoria**: Se mantienen durante la ejecución
- 🔄 **Persistencia**: Los datos se pierden al cerrar la aplicación
- ⚡ **Ventaja**: No requiere instalación ni configuración adicional

## 👥 Usuarios por Defecto

### Administrador del Sistema
- **Usuario**: `admin`
- **Contraseña**: `admin123`
- **Funciones**: Acceso completo al sistema

*Nota: Se recomienda cambiar la contraseña del administrador en producción*

## 🛠️ Arquitectura Técnica

### Tecnologías Utilizadas
- ☕ **Java 24** - Lenguaje de programación
- 🖥️ **Java Swing** - Interfaz gráfica
- 🗄️ **MySQL** - Base de datos (opcional)
- 🔒 **SHA-256** - Encriptación de contraseñas
- 🏗️ **Patrón DAO** - Acceso a datos
- 💼 **Patrón Service** - Lógica de negocio

### Estructura del Proyecto
```
src/main/java/
├── appTaskFlow/          # Clase principal
├── dao/                  # Acceso a datos
│   └── impl/            # Implementaciones DAO
├── domain/              # Modelos de dominio
├── interfacesGUI/       # Interfaces gráficas
├── service/             # Lógica de negocio
└── utilidad/            # Utilidades (DB, encriptación)
```

## 🔧 Resolución de Problemas

### ❌ Error: "No se encontró el driver de MySQL"
- **Solución**: La aplicación automáticamente funciona sin base de datos
- **Opcional**: Instalar MySQL para persistencia de datos

### ❌ Error: "ClassNotFoundException: AbsoluteLayout"
- **Solución**: Usar NetBeans IDE o compilar con Maven correctamente
- **Causa**: Dependencias de NetBeans no disponibles

### ❌ No aparece la ventana de login
- **Verificar**: Java está instalado correctamente
- **Revisar**: Consola para mensajes de error
- **Solución**: Usar NetBeans IDE para mejor compatibilidad

## 📈 Próximas Funcionalidades

- 📝 **Formulario de registro** de nuevos usuarios
- 👥 **Gestión de usuarios** (admin)
- 🎨 **Temas personalizables**
- 📊 **Reportes y estadísticas**
- 🔔 **Notificaciones de tareas**
- 📱 **Interfaz responsive**

## 👨‍💻 Desarrolladores

- **Luciano** - Funcionalidad de tareas y arquitectura base
- **Fabian** - Sistema de autenticación y login
- **Equipo TaskFlow** - Integración y testing

---

## 🚀 ¡Uso Rápido!

1. **Abrir NetBeans IDE**
2. **Abrir proyecto** (`AppTaskFlow`)
3. **Ejecutar** (F6)
4. **Login**: usuario `admin`, contraseña `admin123`
5. **¡Gestionar tareas!** 📋✨

---

**¿Problemas?** Consulta la sección de resolución de problemas arriba o contacta al equipo de desarrollo.