package gestortareas.dao;

import gestortareas.model.Usuario;

import java.util.List;
public interface UsuarioDAO {
    Usuario login(String username, String passwordHash);
    boolean registrarUsuario(Usuario usuario);
    Usuario obtenerPorId(int id);
    boolean actualizarUltimoLogin(int idUsuario);
    boolean existeUsername(String username);
    boolean existeEmail(String email);
    boolean actualizarContraseña(String newPassword, String email);
    List<Usuario> obtenerTodosUsuarios();
}
