package gestortareas.controller;

import gestortareas.dao.CategoriaDAO;
import gestortareas.dao.TareaDao;
import gestortareas.interfacesGUI.frmAgregarNuevaTarea;
import gestortareas.model.Categoria;
import gestortareas.model.Tarea;
import gestortareas.model.Usuario;
import gestortareas.model.enums.Prioridad;
import gestortareas.service.CategoriaService;
import gestortareas.service.TareaService;
import gestortareas.service.UsuarioService;

import javax.swing.*;

/**
 *
 * @author Michael Medina
 */
public class TareaController {
    private final frmAgregarNuevaTarea vistaNuevaTarea;
    private final TareaService tareaService;

    public TareaController() {
        this.vistaNuevaTarea = new  frmAgregarNuevaTarea(this);
        this.tareaService = new TareaService();
    }

    public void abrirVistaNuevaTarea() {
        vistaNuevaTarea.setVisible(true);
        vistaNuevaTarea.setLocationRelativeTo(null);
        vistaNuevaTarea.setResizable(false);
        vistaNuevaTarea.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void mensaje(String mensaje) {
        JOptionPane.showMessageDialog(vistaNuevaTarea, mensaje);
    }

    public void llenarComboBox(JComboBox<Categoria> comboCategoria) {
        CategoriaService categoriaService = new CategoriaService();
        DefaultComboBoxModel<Categoria> model = new DefaultComboBoxModel<>();

        for (Categoria categoria: categoriaService.obtenerCategorias()){
            model.addElement(categoria);
        }

        comboCategoria.setModel(model);
    }

    public void llenarComboBoxPrioridad(JComboBox<String> jComboBoxPrioridad) {
        DefaultComboBoxModel<String> prioridades = new DefaultComboBoxModel<>();
        prioridades.addElement(Prioridad.MEDIA.name());
        prioridades.addElement(Prioridad.ALTA.name());
        prioridades.addElement(Prioridad.URGENTE.name());
        prioridades.addElement(Prioridad.BAJA.name());
        jComboBoxPrioridad.setModel(prioridades);
    }

    public int createTarea(Tarea tarea){
        return tareaService.create(tarea);
    }


    public void llenarComboxUsuarios(JComboBox<Usuario> jComboBoxUsuarios) {
        UsuarioService userService = new UsuarioService();
        DefaultComboBoxModel<Usuario> usuarios = new DefaultComboBoxModel<>();

        for (Usuario user : userService.obtenerTodosUsuarios()){
            usuarios.addElement(user);
        }

        jComboBoxUsuarios.setModel(usuarios);
    }
}
