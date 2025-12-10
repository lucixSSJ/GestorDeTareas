package gestortareas.dao.impl;

import gestortareas.dao.LogNotificacionDAO;
import gestortareas.model.LogNotificacion;
import gestortareas.utilidad.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogNotificacionDAOImpl implements LogNotificacionDAO {
    @Override
    public void guardarLog(LogNotificacion log) {
        String sql = "INSERT INTO logs_notificaciones (id_usuario, id_tarea, tipo_notificacion, " +
                "email_destino, asunto, mensaje, fecha_envio, enviado_correctamente, error_envio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, log.getIdUsuario());
            if (log.getIdTarea() != null) {
                ps.setInt(2, log.getIdTarea());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, log.getTipoNotificacion());
            ps.setString(4, log.getEmailDestino());
            ps.setString(5, log.getAsunto());
            ps.setString(6, log.getMensaje());
            ps.setTimestamp(7, Timestamp.valueOf(log.getFechaEnvio()));
            ps.setBoolean(8, log.isEnviadoCorrectamente());
            ps.setString(9, log.getErrorEnvio());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al guardar log de notificación: " + e.getMessage());
        }
    }

    @Override
    public List<LogNotificacion> obtenerLogsPorUsuario(int idUsuario) {
        // Implementación similar
        return new ArrayList<>();
    }

    @Override
    public List<LogNotificacion> obtenerLogsPorTarea(int idTarea) {
        // Implementación similar
        return new ArrayList<>();
    }
}
