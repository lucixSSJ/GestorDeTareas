package gestortareas.controller;

import gestortareas.interfacesGUI.frmInicioSesion;
import gestortareas.interfacesGUI.frmPantallaPrincipal;
import gestortareas.interfacesGUI.frmRegistrarse;
import gestortareas.model.Usuario;
import gestortareas.service.UsuarioService;

import javax.swing.*;

public class LoginController {
    private final UsuarioService usuarioService;
    private frmInicioSesion frmInicioSesion;
    private frmRegistrarse frmRegistrarse;
    private frmPantallaPrincipal frmPantallaPrincipal;
    private Usuario usuarioLogueado;

    public LoginController(frmInicioSesion frmInicioSesion) {
        this.usuarioService = new UsuarioService();
        this.frmInicioSesion = frmInicioSesion;
    }

    public void setFrmRegistrarse(frmRegistrarse frmRegistrarse) {
        this.frmRegistrarse = frmRegistrarse;
    }

    public void iniciarSesion(String username, String password) {
        try {
            // Validaciones básicas
            if (username == null || username.trim().isEmpty()) {
                mostrarError("Por favor ingrese su nombre de usuario");
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                mostrarError("Por favor ingrese su contraseña");
                return;
            }

            // Intentar login
            usuarioLogueado = usuarioService.iniciarSesion(username, password);

            if (usuarioLogueado != null) {
                loginExitoso();
            } else {
                mostrarError("Usuario o contraseña incorrectos");
                limpiarCamposLogin();
            }

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void registrarUsuario(String nombres, String apellidos, String email,
                                 String username, String password, String confirmarPassword) {
        try {
            // Validaciones
            if (!password.equals(confirmarPassword)) {
                mostrarError("Las contraseñas no coinciden");
                return;
            }

            // Registrar usuario
            boolean registrado = usuarioService.registrarUsuario(nombres, apellidos, email, username, password);

            if (registrado) {
                mostrarExito("Usuario registrado exitosamente. Ahora puede iniciar sesión.");
                volverALogin();
            } else {
                mostrarError("Error al registrar el usuario");
            }

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void abrirRegistro() {
        if (frmInicioSesion != null) {
            frmInicioSesion.setVisible(false);
        }

        frmRegistrarse = new frmRegistrarse(this);
        frmRegistrarse.setVisible(true);
    }

    public void volverALogin() {
        if (frmRegistrarse != null) {
            frmRegistrarse.dispose();
            frmRegistrarse = null;
        }

        if (frmInicioSesion != null) {
            limpiarCamposLogin();
            frmInicioSesion.setVisible(true);
            frmInicioSesion.limpiarCampos();
        }
    }

    private void loginExitoso() {
        // Cerrar ventanas de login/registro
        if (frmInicioSesion != null) {
            frmInicioSesion.dispose();
        }
        if (frmRegistrarse != null) {
            frmRegistrarse.dispose();
        }

        // Abrir pantalla principal con el usuario logueado
        frmPantallaPrincipal = new frmPantallaPrincipal(usuarioLogueado);
        frmPantallaPrincipal.setVisible(true);

        System.out.println("Login exitoso: " + usuarioLogueado.getNombres());
    }

    private void limpiarCamposLogin() {
        if (frmInicioSesion != null) {
            frmInicioSesion.limpiarCampos();
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(getVentanaActiva(), mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(getVentanaActiva(), mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private java.awt.Component getVentanaActiva() {
        if (frmRegistrarse != null && frmRegistrarse.isVisible()) {
            return frmRegistrarse;
        }
        return frmInicioSesion;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}
