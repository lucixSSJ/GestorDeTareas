package gestortareas.controller;

import gestortareas.interfacesGUI.frmCategorias;
import gestortareas.interfacesGUI.frmPantallaPrincipal;
import gestortareas.interfacesGUI.frmTareasGeneral;
import gestortareas.model.Usuario;

import javax.swing.*;

public class NavigationController {
    private Usuario usuario;

    public NavigationController(Usuario usuario) {
        this.usuario = usuario;
    }

    public void abrirGestionTareas(JFrame vistaActual){
        vistaActual.dispose();
        new frmTareasGeneral(usuario, this).setVisible(true);
    }

    public void abrirGestionCategorias(JFrame vistaActual) {
        vistaActual.dispose();
        new frmCategorias(usuario, this).setVisible(true);
    }

    public void irATareasDesdeCategorias(JFrame vistaActual) {
        vistaActual.dispose();
        new frmTareasGeneral(usuario, this).setVisible(true);
    }

    public void irACategoriasDesdeTareas(JFrame vistaActual) {
        vistaActual.dispose();
        new frmCategorias(usuario, this).setVisible(true);
    }

    public void volverAlDashboard(JFrame vistaActual) {
        vistaActual.dispose();
        new frmPantallaPrincipal(usuario).setVisible(true);
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
