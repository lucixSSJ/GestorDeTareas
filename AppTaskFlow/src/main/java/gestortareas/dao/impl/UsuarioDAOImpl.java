package gestortareas.dao.impl;

import gestortareas.dao.UsuarioDAO;
import gestortareas.model.Usuario;
import gestortareas.service.UsuarioService;
import gestortareas.utilidad.db.DatabaseConnection;
import gestortareas.utilidad.passwordhasher.PasswordHasher;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAOImpl implements UsuarioDAO {
    @Override
    public Usuario login(String username, String password) {
        String sql = "CALL sp_login_usuario(?)";

        try (Connection conn = getValidConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, username);
            ResultSet rs = cstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String email = rs.getString("email");

                String computedHash = PasswordHasher.hashPassword(password);

                boolean passwordCorrecta = computedHash.equals(storedHash);

                if (passwordCorrecta) {
                    Usuario usuario = mapearUsuario(rs);
                    actualizarUltimoLogin(usuario.getIdUsuario());
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean registrarUsuario(Usuario usuario) {
        String sql = "CALL sp_registrar_usuario(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getValidConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, usuario.getNombres());
            cstmt.setString(2, usuario.getApellidos());
            cstmt.setString(3, usuario.getEmail());
            cstmt.setString(4, usuario.getUsername());
            cstmt.setString(5, usuario.getPasswordHash());

            cstmt.registerOutParameter(6, Types.INTEGER);
            cstmt.registerOutParameter(7, Types.VARCHAR);

            cstmt.execute();

            int resultado = cstmt.getInt(6);
            String mensaje = cstmt.getString(7);

            return resultado == 1;

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            // Intentar reconexión
            DatabaseConnection.reconnect();
            return false;
        }
    }

    @Override
    public Usuario obtenerPorId(int id) {
        String sql = "CALL sp_obtener_usuario_por_id(?)";

        try (Connection conn = getValidConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, id);
            ResultSet rs = cstmt.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
            DatabaseConnection.reconnect();
        }
        return null;
    }

    @Override
    public boolean actualizarUltimoLogin(int idUsuario) {
        String sql = "UPDATE usuarios SET ultimo_login = NOW() WHERE id_usuario = ?";

        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar último login: " + e.getMessage());
            DatabaseConnection.reconnect();
            return false;
        }
    }

    @Override
    public boolean existeUsername(String username) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ? AND activo = TRUE";

        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar username: " + e.getMessage());
            DatabaseConnection.reconnect();
        }
        return false;
    }

    @Override
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ? AND activo = TRUE";

        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
            DatabaseConnection.reconnect();
        }
        return false;
    }

    // Método auxiliar para obtener una conexión válida
    private Connection getValidConnection() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            DatabaseConnection.reconnect();
            conn = DatabaseConnection.getConnection();
        }
        return conn;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        // Columnas del procedimiento actualizado
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setEmail(rs.getString("email"));
        usuario.setUsername(rs.getString("username"));
        //usuario.setPasswordHash(rs.getString("password_hash"));
        usuario.setNotificacionesVencimiento(rs.getBoolean("notificaciones_vencimiento"));
        usuario.setDiasAntesVencimiento(rs.getInt("dias_antes_vencimiento"));
        //usuario.setActivo(rs.getBoolean("activo"));

        // Fechas
        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            usuario.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }

        Timestamp ultimoLogin = rs.getTimestamp("ultimo_login");
        if (ultimoLogin != null) {
            usuario.setUltimoLogin(ultimoLogin.toLocalDateTime());
        }

        return usuario;
    }

    @Override
    public boolean actualizarContraseña(String newPassword, String email) {
        String sql = "UPDATE usuarios SET password_hash = ? WHERE email = ?";
        try(Connection conn = getValidConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            
            return stmt.executeUpdate() > 0;
        }catch(SQLException ex){
            System.out.println("Error al actualizar la contraseña: "+ex.getMessage());
            DatabaseConnection.reconnect();
            return false;
        }
    }

    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql =  "SELECT id_usuario, nombres, apellidos FROM usuarios";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario user = new Usuario();
                user.setIdUsuario(rs.getInt("id_usuario"));
                user.setNombres(rs.getString("nombres"));
                user.setApellidos(rs.getString("apellidos"));
                usuarios.add(user);
            }
            return usuarios;
        }catch (SQLException ex){
            System.out.println("Error al obtener todos usuarios: "+ex.getMessage());
        }
        return usuarios;
    }
}
