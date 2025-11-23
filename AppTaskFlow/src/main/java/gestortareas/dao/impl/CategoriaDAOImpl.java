package gestortareas.dao.impl;

import gestortareas.dao.CategoriaDAO;
import gestortareas.model.Categoria;
import gestortareas.utilidad.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {
    @Override
    public boolean existeCategoriaUsuario(int usuarioId, String nombreCategoria) {
        String sql = "SELECT COUNT(*) FROM categorias WHERE id_usuario = ? AND LOWER(nombre_categoria) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, nombreCategoria);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de categoría: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Categoria> obtenerPorUsuario(int usuarioId) {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "CALL sp_obtener_categorias_con_conteo(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, usuarioId);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                categorias.add(mapearCategoriaConConteo(rs, usuarioId));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
            e.printStackTrace();
        }
        return categorias;
    }

    private Categoria mapearCategoriaConConteo(ResultSet rs, int usuarioId) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id_categoria"));
        categoria.setUsuarioId(usuarioId);
        categoria.setNombre(rs.getString("nombre_categoria"));
        categoria.setNumeroTareas(rs.getInt("numero_tareas"));

        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            categoria.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }

        return categoria;
    }

    @Override
    public boolean crear(Categoria categoria) {
        String sql = "CALL sp_crear_categoria(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, categoria.getUsuarioId());
            cstmt.setString(2, categoria.getNombre());

            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.registerOutParameter(4, Types.VARCHAR);

            cstmt.execute();

            int resultado = cstmt.getInt(3);
            String mensaje = cstmt.getString(4);

            System.out.println("Crear categoría: " + mensaje);
            return resultado == 1;

        } catch (SQLException e) {
            System.err.println("Error al crear categoría: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Categoria categoria) {
        String sql = "CALL sp_actualizar_categoria(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, categoria.getId());
            cstmt.setString(2, categoria.getNombre());

            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.registerOutParameter(4, Types.VARCHAR);

            cstmt.execute();

            int resultado = cstmt.getInt(3);
            String mensaje = cstmt.getString(4);

            System.out.println("Actualizar categoría: " + mensaje);
            return resultado == 1;

        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int categoriaId) {
        String sql = "CALL sp_eliminar_categoria(?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, categoriaId);

            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            cstmt.execute();

            int resultado = cstmt.getInt(2);
            String mensaje = cstmt.getString(3);

            System.out.println("Eliminar categoría: " + mensaje);
            return resultado == 1;

        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Categoria obtenerPorId(int categoriaId) {
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoriaId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapearCategoria(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categoría por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean existeNombreCategoria(int usuarioId, String nombreCategoria, int excludeCategoriaId) {
        String sql = "SELECT COUNT(*) FROM categorias WHERE id_usuario = ? AND nombre_categoria = ? AND id_categoria != ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, nombreCategoria);
            pstmt.setInt(3, excludeCategoriaId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar nombre categoría: " + e.getMessage());
        }
        return false;
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id_categoria"));
        categoria.setUsuarioId(rs.getInt("id_usuario"));
        categoria.setNombre(rs.getString("nombre_categoria"));

        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            categoria.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }

        return categoria;
    }

}
