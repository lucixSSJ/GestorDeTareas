package gestortareas.controller;

import gestortareas.dao.impl.CategoriaDAOImpl;
import gestortareas.dao.impl.TareaImplem;
import gestortareas.dao.impl.UsuarioDAOImpl;
import gestortareas.interfacesGUI.frmModificarTarea;
import gestortareas.model.Categoria;
import gestortareas.model.Tarea;
import gestortareas.model.Usuario;
import gestortareas.model.enums.EstadoTarea;
import gestortareas.model.enums.Prioridad;
import gestortareas.service.CategoriaService;
import gestortareas.service.TareaService;
import gestortareas.service.UsuarioService;

import javax.swing.*;
import java.util.List;

public class ActualizarController {
    private frmModificarTarea vistaModificarTarea;
    private TareaService tareaService;
    private final Tarea tarea;

    public ActualizarController(Tarea tarea) {
        this.tareaService = new TareaService(new TareaImplem(), new UsuarioDAOImpl(), new CategoriaDAOImpl());
        this.tarea = tarea;
    }

    public void abrirVistaNuevaTarea() {
        this.vistaModificarTarea = new frmModificarTarea(tarea);
        vistaModificarTarea.setVisible(true);
        vistaModificarTarea.setLocationRelativeTo(null);
        vistaModificarTarea.setResizable(false);
        vistaModificarTarea.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void llenarComboBoxCategoria(JComboBox<Categoria> comboCategoria) {
        CategoriaService categoriaService = new CategoriaService(new CategoriaDAOImpl());
        DefaultComboBoxModel<Categoria> model = new DefaultComboBoxModel<>();
        System.out.println("Se va selecionar la categoria: "+tarea.getCategoria());
        List<Categoria> listCategoria = categoriaService.obtenerCategorias();

        if (!listCategoria.isEmpty()) {
            for (Categoria categoria : listCategoria) {
                model.addElement(categoria);
            }

            comboCategoria.setModel(model);
            Categoria categoria = listCategoria.stream()
                    .filter(cate -> cate.getId()==tarea.getCategoria().getId()).findFirst().orElse(null);

            if (categoria != null) {
                comboCategoria.setSelectedItem(categoria);
            }else{
                System.out.println("Categoria no encontrada: "+tarea.getCategoria());
                comboCategoria.setSelectedIndex(0);
            }
        }
    }

    public void llenarComboBoxPrioridad(JComboBox<String> jComboBoxPrioridad) {
        DefaultComboBoxModel<String> prioridades = new DefaultComboBoxModel<>();
        System.out.println("Se va a selecionar la prioridad: "+this.tarea.getPrioridad());
        prioridades.addElement(Prioridad.MEDIA.name());
        prioridades.addElement(Prioridad.ALTA.name());
        prioridades.addElement(Prioridad.URGENTE.name());
        prioridades.addElement(Prioridad.BAJA.name());
        jComboBoxPrioridad.setModel(prioridades);

        Prioridad prioridadEnum = Prioridad.valueOf(this.tarea.getPrioridad().toUpperCase().trim());
        jComboBoxPrioridad.setSelectedItem(prioridadEnum.name());
    }

    public void llenarEstadoTarea(JComboBox<String> jComboBoxEstadoTarea) {
        DefaultComboBoxModel<String> estados = new DefaultComboBoxModel<>();
        estados.addElement(EstadoTarea.PENDIENTE.name());
        estados.addElement(EstadoTarea.EN_PROGRESO.name());
        estados.addElement(EstadoTarea.COMPLETADA.name());
        estados.addElement(EstadoTarea.ARCHIVADA.name());
        estados.addElement(EstadoTarea.VENCIDA.name());

        jComboBoxEstadoTarea.setModel(estados);
        EstadoTarea estadoTarea = EstadoTarea.valueOf(this.tarea.getEstado().toUpperCase().trim());
        jComboBoxEstadoTarea.setSelectedItem(estadoTarea.name());
    }

    public void llenarComboxUsuarios(JComboBox<Usuario> jComboBoxUsuarios) {
        UsuarioService userService = new UsuarioService(new UsuarioDAOImpl());

        List<Usuario> userList = userService.obtenerTodosUsuarios();

        DefaultComboBoxModel<Usuario> usuarios = new DefaultComboBoxModel<>();

        System.out.println("Se va a seleccionar el usuario: "+this.tarea.getUsuario());

        if (userList.isEmpty()) {
            System.out.println("Usuarios no encontrados");
        } else {

            for (Usuario user : userList) {
                usuarios.addElement(user);
            }

            jComboBoxUsuarios.setModel(usuarios);
            Usuario user = userList.stream().
                    filter(u -> u.getIdUsuario() == tarea.getUsuario().getIdUsuario()).findFirst().orElse(null);
            if (user != null) {
                jComboBoxUsuarios.setSelectedItem(user);
            }else {
                System.out.println("Usuario no encontrado: "+tarea.getUsuario());
            }
        }
    }

    public void actualizarTarea(Tarea tarea) {
        boolean ok = this.tareaService.actualizarTarea(tarea).getDatos();
        if (ok) {
            mensaje("Datos actualizados");
        }else {
            mensaje("Ocurrio un error al actualizar");
        }
    }

    public void mensaje(String mensaje) {
        JOptionPane.showMessageDialog(vistaModificarTarea, mensaje);
    }


}
