package gestortareas.dao.impl;

import gestortareas.DTO.TareaDetalle;
import gestortareas.dao.TareaDao;
import gestortareas.model.Categoria;
import gestortareas.model.Tarea;
import gestortareas.model.Usuario;
import gestortareas.service.CategoriaService;
import gestortareas.service.UsuarioService;
import gestortareas.utilidad.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TareaImplem implements TareaDao {

    @Override
    public int create(Tarea tarea) {
        String insert = "INSERT INTO tareas (id_usuario, id_categoria, nombre_tarea, " +
                "descripcion, fecha_creacion, fecha_limite, prioridad) " +
                "VALUES(?,?,?,?,?,?,?)";

        int idGenerado = -1;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tarea.getUsuario().getIdUsuario());
            ps.setInt(2, tarea.getCategoria().getId());
            ps.setString(3, tarea.getNombre());
            ps.setString(4, tarea.getDescripcion());

            if (tarea.getFechaCreacion() != null) {
                ps.setTimestamp(5, new Timestamp(tarea.getFechaCreacion().getTime()));
            } else {
                ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            }

            ps.setTimestamp(6, new Timestamp(tarea.getFechaLimite().getTime()));
            ps.setString(7, tarea.getPrioridad());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = (int) rs.getLong(1);
                    return idGenerado;
                }
            }
            
            return -1;
        } catch (SQLException sqlException) {
            System.out.println("Hubo un error: " + sqlException.getMessage());
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

    @Override
    public List<Tarea> getTareas(Usuario user) {
        List<Tarea> listTareas = new ArrayList<>();
        Map<Integer, Categoria> categorias = todasLasCategorias();

        String SQL = "SELECT * FROM tareas WHERE id_usuario = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, user.getIdUsuario());
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Categoria categoria = new Categoria();
                int idCategoria = rs.getInt("id_categoria");

                if (categorias.containsKey(idCategoria)){
                    categoria = categorias.get(idCategoria);
                }else{
                    System.out.println("No hay ninguna categoria con ese id");
                }

                Tarea tarea = new Tarea.TareaBuilder()
                        .setIdTarea(rs.getInt("id_tarea"))
                        .setNombre(rs.getString("nombre_tarea"))
                        .setDescripcion(rs.getString("descripcion"))
                        .setFechaCreacion(rs.getTimestamp("fecha_creacion"))
                        .setFechaLimite(rs.getTimestamp("fecha_limite"))
                        .setUsuario(user)
                        .setEstado(rs.getString("estado"))
                        .setPrioridad(rs.getString("prioridad"))
                        .setCategoria(categoria)
                        .build();
                listTareas.add(tarea);
            }
            return listTareas;
        }catch (SQLException ex){
            System.out.println("Hubo un error: "+ex.getMessage());
        }
        return List.of();
    }

    @Override
    public TareaDetalle getTareaIDdetalle(int idTarea) {
        String SQL = "select * from vista_tareas_detalle where id_tarea = ?;";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL);){
            ps.setInt(1,idTarea);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new TareaDetalle(
                        rs.getString("nombre_tarea"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fecha_limite"),
                        rs.getString("prioridad"),
                        rs.getString("estado"),
                        rs.getString("nombres").concat(" ").concat(rs.getString("apellidos")),
                        rs.getString("nombre_categoria"),
                        rs.getInt("numero_archivos")
                );
            }else{
                return null;
            }
        }catch (SQLException exception){
            System.out.println("Hubo un error en el metodo getTareaIDdetalle de la clase TareaImplem: "+exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean actualizarTarea(Tarea tarea) {
        StringBuilder sql = new StringBuilder("UPDATE tareas SET ");
        List<Object> parametros = new ArrayList<>();

        if (tarea.getUsuario() != null) {
            sql.append("id_usuario = ?, ");
            parametros.add(tarea. getUsuario().getIdUsuario());
        }

        if (tarea.getCategoria() != null) {
            sql.append("id_categoria = ?, ");
            parametros.add(tarea.getCategoria().getId());
        }

        if (tarea.getNombre() != null && !tarea.getNombre().trim().isEmpty()) {
            sql.append("nombre_tarea = ?, ");
            parametros.add(tarea.getNombre());
        }

        if (tarea.getDescripcion() != null && !tarea.getDescripcion().trim().isEmpty()) {
            sql.append("descripcion = ?, ");
            parametros.add(tarea.getDescripcion());
        }

        if (tarea.getFechaLimite() != null) {
            sql.append("fecha_limite = ?, ");
            parametros.add(new Timestamp(tarea.getFechaLimite().getTime()));
        }

        if (tarea.getEstado() != null && !tarea.getEstado().trim().isEmpty()) {
            sql.append("estado = ?, ");
            parametros.add(tarea.getEstado());
        }

        if (tarea.getPrioridad() != null && !tarea.getPrioridad().trim().isEmpty()) {
            sql.append("prioridad = ?, ");
            parametros.add(tarea.getPrioridad());
        }

        if (tarea.getRecordatorio() != 0) {
            sql.append("recordatorio_enviado = ?, ");
            parametros. add(tarea.getRecordatorio());
        }

        if (tarea.getFechaArchivada() != null) {
            sql.append("fecha_archivado = ?, ");
            parametros.add(new Timestamp(tarea.getFechaArchivada().getTime()));
        }

        if (parametros.isEmpty()) {
            System.out.println("No hay campos para actualizar");
            return false;
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id_tarea = ?; ");
        parametros.add(tarea.getIdTarea());
        parametros.forEach(System.out::println);
        System.out.println(sql);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                System.out.println("Parametro: "+parametros.get(i));
                ps.setObject(i + 1, parametros.get(i));
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public Tarea obtenerTarea(int idTarea) {
        String sql = "select * from tareas where id_tarea = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTarea);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                int idCategoria = rs.getInt("id_categoria");
                String descripcion = rs.getString("descripcion");
                Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
                Timestamp fechaLimite = rs.getTimestamp("fecha_limite");
                String prioridad = rs.getString("prioridad");
                String estado = rs.getString("estado");
                String nombreTarea = rs.getString("nombre_tarea");
                Timestamp archivada = rs.getTimestamp("fecha_archivado");

                Usuario usuario = obtenerUsuario(idUsuario);
                Categoria categoria = obtenerCategoria(idCategoria);

                return new Tarea.TareaBuilder()
                        .setIdTarea(idTarea)
                        .setUsuario(usuario)
                        .setDescripcion(descripcion)
                        .setFechaCreacion(fechaCreacion)
                        .setFechaLimite(fechaLimite)
                        .setPrioridad(prioridad)
                        .setEstado(estado)
                        .setNombre(nombreTarea)
                        .setCategoria(categoria)
                        .setFechaArchivada(archivada)
                        .build();
            }else{
                return null;
            }
        }catch (SQLException ex) {
            System.out.println("Ocurrio un error en la implementacion de obtener la tarea por su id en la TareaImpleme: "+ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean eliminarTarea(int idTarea) {
        String sql = "delete from tareas where id_tarea = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,idTarea);
            return ps.executeUpdate() > 0;

        }catch (SQLException ex) {
            System.out.println("Ocurrio un error al eliminar la tarea. Clase Tareaimpleme, Metodo Eliminar tarea");
            ex.printStackTrace();
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

    private Map<Integer, Categoria> todasLasCategorias(){
        CategoriaService categoriaService = new CategoriaService(new CategoriaDAOImpl());

        List<Categoria> categorias = categoriaService.obtenerCategorias();
        Map<Integer, Categoria> categoriaMap = new HashMap<>();

        for (Categoria categoria : categorias) {
            categoriaMap.put(categoria.getId(), categoria);
        }

        return categoriaMap;
    }

    private Usuario obtenerUsuario(int idUsuario) {
        UsuarioService usuarioService = new UsuarioService(new UsuarioDAOImpl());
        return usuarioService.obtenerPorId(idUsuario);
    }

    private Categoria obtenerCategoria(int idCategoria) {
        CategoriaService categoriaService = new CategoriaService(new CategoriaDAOImpl());
        return categoriaService.obtenerPorId(idCategoria);
    }
}
