package gestortareas.service;

import gestortareas.dao.TareaDao;
import gestortareas.dao.impl.TareaImplem;
import gestortareas.model.Tarea;

/**
 *
 * @author Michael Medina
 */
public class TareaService {
    private final TareaImplem tareaImplem;
    private final UsuarioService usuarioService;
    private final CategoriaService categoriaService;

    public TareaService() {
        this.tareaImplem = new TareaImplem();
        this.usuarioService = new UsuarioService();
        this.categoriaService = new CategoriaService();
    }

    public int create(Tarea tarea) {
        if (usuarioService.obtenerUsuarioPorId(tarea.getUsuario().getIdUsuario()) == null) {
            System.out.println("Usuario no encontrado");
            return 1;
        }

        if (tarea.getNombre().trim().isEmpty() || tarea.getDescripcion().trim().isEmpty()) {
            System.out.println("Los campos del nombre y descripcion son importantes");
            return 2;
        }

        if (tareaImplem.existe(tarea.getNombre().trim())){
            System.out.println("La tarea ya existe");
            return  3;
        }

        if (categoriaService.obtenerCategoriaPorId(tarea.getIdCategoria()) == null){
            System.out.println("Categoria no encontrada");
            return 4;
        }

        boolean ok = tareaImplem.create(tarea);
        if (!ok) {
            System.out.println("Error al crear el tarea");
            return 5;
        }else {
            System.out.println("Tarea creada exitosamente");
            return 6;
        }
    }


    public boolean existe(String nombreTarea) {
        if (nombreTarea.trim().isEmpty()) {
            System.out.println("El nombre del tarea no puede ser vacio");
            return false;
        }

        return tareaImplem.existe(nombreTarea);
    }
}
