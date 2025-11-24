package gestortareas.dao.impl;

import gestortareas.dao.TareaDao;
import gestortareas.model.Tarea;
import gestortareas.utilidad.db.DatabaseConnection;

import java.sql.*;

public class TareaImplem implements TareaDao {
    @Override
    public boolean create(Tarea tarea) {
        String insert ="INSERT INTO tareas (id_usuario, id_categoria, nombre_tarea, descripcion, fecha_limite, prioridad) VALUES(?,?,?,?,?,?);";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(insert);
            ){

            ps.setInt(1,tarea.getUsuario().getIdUsuario());
            ps.setInt(2,tarea.getIdCategoria());
            ps.setString(3, tarea.getNombre());
            ps.setString(4, tarea.getDescripcion());
            ps.setTimestamp(5, new Timestamp(tarea.getFechaLimite().getTime()));
            ps.setString(6, tarea.getPrioridad());

            int rs = ps.executeUpdate();
            return  rs > 0;
        }catch (SQLException sqlException){
            System.out.println("Hubo un error: "+sqlException.getMessage());
            return false;
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
}
