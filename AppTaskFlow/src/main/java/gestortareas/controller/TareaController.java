package gestortareas.controller;

import gestortareas.DTO.TareaDTO;
import gestortareas.DTO.TareaDetalle;
import gestortareas.dao.impl.ArchivoImpl;
import gestortareas.dao.impl.CategoriaDAOImpl;
import gestortareas.dao.impl.TareaImplem;
import gestortareas.dao.impl.UsuarioDAOImpl;
import gestortareas.interfacesGUI.DetalleTarea;
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
import javax.swing.table.DefaultTableModel;

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
        this.tareaService = new TareaService(new TareaImplem(),new UsuarioDAOImpl(),new CategoriaDAOImpl());
        this.archivoService = new ArchivoService(new ArchivoImpl());
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
        CategoriaService categoriaService = new CategoriaService(new CategoriaDAOImpl());
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
        UsuarioService userService = new UsuarioService(new UsuarioDAOImpl());

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


    public void obetenerTodasLasTareas(Usuario user, JTable tableTareas) {
        DefaultTableModel model = new DefaultTableModel(null, new String[]{"ID", "Nombre", "Descripcion", "Fecha Limite", "Prioridad", "Categoria"});
        ResultadoOperacion<List<TareaDTO>> tareas = this.tareaService.getTareas(user);

        if (!tareas.esExitoso()) {
            tareas.mostrarDialogo(vistaNuevaTarea);
        }

        List<TareaDTO> tareasDTO = tareas.getDatos();
        for (TareaDTO tareaDTO : tareasDTO) {
            System.out.println(tareaDTO.getCategoria().getNombre());
            Object[] fila = new Object[6];
            fila[0] = tareaDTO.getIdTarea();
            fila[1] = tareaDTO.getNombre();
            fila[2] = tareaDTO.getDescripcion();
            fila[3] = tareaDTO.getFechaLimite();
            fila[4] = tareaDTO.getPrioridad().toUpperCase();
            fila[5] = tareaDTO.getCategoria().getNombre().toUpperCase();
            model.addRow(fila);
        }
        tableTareas.setModel(model);
        tableTareas.removeColumn(tableTareas.getColumnModel().getColumn(0));
    }

    public void getTareaIDDetalle(int idTarea){
        if (idTarea == 0){
            mensaje("Debe seleccionar una tarea");
        }

        ResultadoOperacion<TareaDetalle> resultadoOperacion = tareaService.getTareaDetalle(idTarea);

        if (!resultadoOperacion.esExitoso()) {
            resultadoOperacion.mostrarDialogo(vistaNuevaTarea);
        }

        TareaDetalle tareaDetalle = resultadoOperacion.getDatos();
        DetalleTarea detalle = new DetalleTarea(vistaNuevaTarea,true,tareaDetalle);
        detalle.setLocationRelativeTo(null);
        detalle.setVisible(true);
        /*
        String detalleTarea = String.format("""
                Nombre de la Persona Asignada: %s
                Titulo de la Tarea: %s
                Descripcion: %s
                Fecha Limite: %s
                Prioridad: %s
                Categoria: %s
                Estado: %s
                Archivos Adjuntos: %d
                """,tareaDetalle.getNombresCompletos(),tareaDetalle.getNombreTarea(),tareaDetalle.getDescripcion(),
                tareaDetalle.getFechaLimite(),tareaDetalle.getPrioridad(),tareaDetalle.getCategoria(),tareaDetalle.getEstado(),
                tareaDetalle.getNumeroArchivosAdjuntos()
        );
        JOptionPane.showMessageDialog(vistaNuevaTarea, detalleTarea);

         */
    }

    public Tarea obtenerTarea(int idTarea){
        ResultadoOperacion<Tarea> resultadoOperacion = this.tareaService.obtenerTarea(idTarea);

        if (!resultadoOperacion.esExitoso()) {
            resultadoOperacion.mostrarDialogo(vistaNuevaTarea);
        }

        return  resultadoOperacion.getDatos();
    }

}
