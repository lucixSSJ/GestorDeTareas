package gestortareas.controller;

import gestortareas.interfacesGUI.frmCategorias;
import gestortareas.interfacesGUI.frmPantallaPrincipal;
import gestortareas.interfacesGUI.frmTareasGeneral;
import gestortareas.model.Usuario;

public class DashboardController {
    private frmPantallaPrincipal vistaDashboard;
    private Usuario usuario;
    private NavigationController navigationController;

    public DashboardController(frmPantallaPrincipal vista, Usuario usuario) {
        this.vistaDashboard = vista;
        this.usuario = usuario;
        this.navigationController = new NavigationController(usuario);
    }

    public void abrirGestionTareas() {
        navigationController.abrirGestionTareas(vistaDashboard);
    }

    public void abrirGestionCategorias() {
        navigationController.abrirGestionCategorias(vistaDashboard);
    }
}
