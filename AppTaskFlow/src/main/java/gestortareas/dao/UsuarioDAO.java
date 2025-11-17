package gestortareas.dao;

import gestortareas.model.Usuario;

public interface UsuarioDAO {
    Usuario login(String username, String passwordHash);
    boolean registrarUsuario(Usuario usuario);
    Usuario obtenerPorId(int id);
    boolean actualizarUltimoLogin(int idUsuario);
    boolean existeUsername(String username);
    boolean existeEmail(String email);
}
