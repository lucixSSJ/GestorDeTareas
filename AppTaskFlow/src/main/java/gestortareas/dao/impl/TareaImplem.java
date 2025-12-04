package gestortareas.dao.impl;

import gestortareas.dao.TareaDao;
import gestortareas.model.Tarea;
import gestortareas.utilidad.db.DatabaseConnection;

import java.sql.*;

public class TareaImplem implements TareaDao {
    @Override
    public int create(Tarea tarea) {
        String insert ="INSERT INTO tareas (id_usuario, id_categoria, nombre_tarea, descripcion, fecha_limite, prioridad) VALUES(?,?,?,?,?,?);";
        int idGenerado = -1;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS);
            ){

            ps.setInt(1,tarea.getUsuario().getIdUsuario());
            ps.setInt(2,tarea.getIdCategoria());
            ps.setString(3, tarea.getNombre());
            ps.setString(4, tarea.getDescripcion());
            ps.setTimestamp(5, new Timestamp(tarea.getFechaLimite().getTime()));
            ps.setString(6, tarea.getPrioridad());

            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = (int) rs.getLong(1);
                    return idGenerado;
                }
            }
            
            return -1;
        }catch (SQLException sqlException){
            System.out.println("Hubo un error: "+sqlException.getMessage());
            return -1;
        }
    }

    @Override
    public boolean existe(String nombreTarea) {
        String SQL = "select * from tareas where nombre_tarea = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL);){
            ps.setString(1, nombreTarea);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return false;
    }

    public boolean actualizarTarea(Tarea tarea){
        String sql = "UPDATE tareas set id_usuario = ?, id_categoria = ?, nombre_tarea = ?, descripcion = ?, fecha_limite = ?, estado = ?, prioridad = ?, recordatorio_enviado = ?, fecha_archivada = ? WHERE id_tarea = ?";
        
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
            
            if(tarea.getUsuario() != null){
                ps.setInt(1, tarea.getUsuario().getIdUsuario());
            }
            
            if (tarea.getIdCategoria() != 0) {
                ps.setInt(2, tarea.getIdCategoria());
            }
            
            if((tarea.getNombre() != null || !tarea.getNombre().trim().isEmpty()) && mismaIdNombreTarea(tarea.getIdTarea(), tarea.getNombre())){
                ps.setString(3, tarea.getNombre());
            }
            
            if(tarea.getDescripcion() != null || tarea.getDescripcion().trim().isEmpty()){
                ps.setString(4, tarea.getDescripcion());
            }
            
            if (tarea.getFechaLimite() != null) {
                ps.setTimestamp(5, new Timestamp(tarea.getFechaLimite().getTime()));
            }
            
            if (!tarea.getEstado().trim().isEmpty() || tarea.getEstado() != null) {
                ps.setString(6, tarea.getEstado());
            }
            
            if (!tarea.getPrioridad().trim().isEmpty() || tarea.getPrioridad()!= null) {
                ps.setString(7, tarea.getPrioridad());
            }
            
            if (tarea.getRecordatorio() != 0) {
                ps.setInt(8, tarea.getRecordatorio());
            }
            
            if (tarea.getFechaArchivada() != null) {
                ps.setTimestamp(9, new Timestamp(tarea.getFechaArchivada().getTime()));
            }
            
            if (tarea.getIdTarea() != 0) {
                ps.setInt(10, tarea.getIdTarea());
            }
            
            return ps.executeUpdate() > 0;
        }catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return false;
    }

    private boolean mismaIdNombreTarea(int id,String nombre) {
         String SQL = "select * from tareas where nombre_tarea = ? AND id_tarea = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL);){
            
            ps.setString(1, nombre);
            ps.setInt(2,id);
            
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return false;
    }
}
