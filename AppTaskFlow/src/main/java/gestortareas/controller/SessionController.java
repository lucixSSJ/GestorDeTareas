package gestortareas.controller;

import gestortareas.interfacesGUI.frmInicioSesion;
import gestortareas.model.Usuario;

import javax.swing.*;

public class SessionController {
    private Usuario usuario;

    public SessionController(Usuario usuario) {
        this.usuario = usuario;
    }

    public void cerrarSesion(JFrame vistaActual){
        int confirmacion = JOptionPane.showConfirmDialog(
                vistaActual, "¿Está seguro que desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION){
            vistaActual.dispose();
            abrirLogin();
        }
    }

    private void abrirLogin() {
        frmInicioSesion login = new frmInicioSesion();
        login.setVisible(true);
        System.out.println("Sesión cerrada para: " + usuario.getNombres());
    }
}
