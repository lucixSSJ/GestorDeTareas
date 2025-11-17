package gestortareas.controller;

import gestortareas.interfacesGUI.frmInicioSesion;
import gestortareas.interfacesGUI.frmPantallaPrincipal;
import gestortareas.model.Usuario;

public class LogoutController {
    private frmPantallaPrincipal vista;
    private Usuario usuario;

    public LogoutController(frmPantallaPrincipal vista, Usuario usuario){
        this.vista = vista;
        this.usuario = usuario;
    }

    public void cerrarSesion() {
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro que desea cerrar sesión?",
                "Cerrar Sesión",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            // Cerrar ventana actual
            vista.dispose();

            // Abrir login
            abrirLogin();
        }
    }

    private void abrirLogin() {
        frmInicioSesion login = new frmInicioSesion();
        login.setVisible(true);
    }

    public Usuario getUsuario(){
        return usuario;
    }

}
