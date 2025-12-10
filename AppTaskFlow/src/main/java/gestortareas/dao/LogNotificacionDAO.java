package gestortareas.dao;

import gestortareas.model.LogNotificacion;

import java.util.List;

public interface LogNotificacionDAO {
    void guardarLog(LogNotificacion log);
    List<LogNotificacion> obtenerLogsPorUsuario(int idUsuario);
    List<LogNotificacion> obtenerLogsPorTarea(int idTarea);
}
