package gestortareas.controller;

import gestortareas.dao.CategoriaDAO;
import gestortareas.dao.TareaDao;
import gestortareas.interfacesGUI.frmAgregarNuevaTarea;
import gestortareas.model.Categoria;
import gestortareas.model.Tarea;
import gestortareas.model.Usuario;
import gestortareas.model.enums.Prioridad;
import gestortareas.service.ArchivoService;
import gestortareas.service.CategoriaService;
import gestortareas.service.TareaService;
import gestortareas.service.UsuarioService;
import gestortareas.utilidad.ResultadoOperacion;
import java.io.File;
import java.util.List;

import javax.swing.*;

/**
 *
 * @author Michael Medina
 */
public class TareaController {

    private final frmAgregarNuevaTarea vistaNuevaTarea;
    private final TareaService tareaService;
    private final ArchivoService archivoService;

    public TareaController() {
        this.vistaNuevaTarea = new frmAgregarNuevaTarea(this);
        this.tareaService = new TareaService();
        this.archivoService = new ArchivoService();
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

        List<Categoria> listCategoria = categoriaService.obtenerCategorias();

        if (!listCategoria.isEmpty()) {
            for (Categoria categoria : listCategoria) {
                model.addElement(categoria);
            }

            comboCategoria.setModel(model);
            comboCategoria.setSelectedIndex(0);
        }

    }

    public void llenarComboBoxPrioridad(JComboBox<String> jComboBoxPrioridad) {
        DefaultComboBoxModel<String> prioridades = new DefaultComboBoxModel<>();
        prioridades.addElement(Prioridad.MEDIA.name());
        prioridades.addElement(Prioridad.ALTA.name());
        prioridades.addElement(Prioridad.URGENTE.name());
        prioridades.addElement(Prioridad.BAJA.name());
        jComboBoxPrioridad.setModel(prioridades);
        jComboBoxPrioridad.setSelectedIndex(0);
    }

    public void createTarea(Tarea tarea, List<File> archivosAdjuntos) {
        ResultadoOperacion<Integer> resultado = tareaService.create(tarea);

        if (!resultado.esExitoso()) {
            System.out.println("Usuario no encontrado");
            resultado.mostrarDialogo(vistaNuevaTarea);
            return;
        }

        if (!archivosAdjuntos.isEmpty()) {
            System.out.println("ID: " + resultado.getDatos());
            archivoService.createArchivo(archivosAdjuntos, resultado.getDatos());
            JOptionPane.showMessageDialog(vistaNuevaTarea, "Archivo agregado");
        }

        resultado.mostrarDialogo(vistaNuevaTarea);

    }

    public void llenarComboxUsuarios(JComboBox<Usuario> jComboBoxUsuarios) {
        UsuarioService userService = new UsuarioService();

        List<Usuario> userList = userService.obtenerTodosUsuarios();

        DefaultComboBoxModel<Usuario> usuarios = new DefaultComboBoxModel<>();

        if (userList.isEmpty()) {
            System.out.println("Usuarios no encontrados");
        } else {

            for (Usuario user : userList) {
                usuarios.addElement(user);
            }

            jComboBoxUsuarios.setModel(usuarios);

            jComboBoxUsuarios.setSelectedItem(userList.get(0));
        }

    }
}
