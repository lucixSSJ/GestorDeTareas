package gestortareas.service;

import gestortareas.dao.UsuarioDAO;
import gestortareas.dao.impl.UsuarioDAOImpl;
import gestortareas.model.Usuario;
import gestortareas.utilidad.passwordhasher.PasswordHasher;

public class UsuarioService {
    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public Usuario iniciarSesion(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        return usuarioDAO.login(username, password);
    }

    public boolean registrarUsuario(String nombres, String apellidos, String email,
                                    String username, String password) {
        // Validaciones
        if (nombres == null || nombres.trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres son obligatorios");
        }

        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (password.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres");
        }

        // Verificar si el username ya existe
        if (usuarioDAO.existeUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Verificar si el email ya existe
        if (usuarioDAO.existeEmail(email)) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        // Hashear password y crear usuario
        String passwordHash = PasswordHasher.hashPassword(password);
        Usuario usuario = new Usuario(nombres, apellidos, email, username, passwordHash);

        return usuarioDAO.registrarUsuario(usuario);
    }

    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioDAO.obtenerPorId(id);
    }
}
