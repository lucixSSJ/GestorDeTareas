package gestortareas.service;

import gestortareas.dao.impl.TareaImplem;
import gestortareas.model.Tarea;
import gestortareas.utilidad.ResultadoOperacion;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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

    public ResultadoOperacion<Integer> create(Tarea tarea) {
        if (usuarioService.obtenerUsuarioPorId(tarea.getUsuario().getIdUsuario()) == null) {
            System.out.println("Usuario no encontrado");
            return ResultadoOperacion.error("Usuario no encontrado");
        }

        if (tarea.getNombre().trim().isEmpty() || tarea.getDescripcion().trim().isEmpty()) {
            System.out.println("Los campos del nombre y descripcion son importantes");
            return ResultadoOperacion.advertencia("Los campos del nombre y descripcion son importantes");
        }
        
        if (tarea.getFechaLimite() == null) {
            return ResultadoOperacion.advertencia("Selecciona una fecha límite para la tarea");
        }
        
        if (tareaImplem.existe(tarea.getNombre().trim())) {
            System.out.println("La tarea ya existe");
            return ResultadoOperacion.advertencia("La tarea ya existe");
        }

        if (categoriaService.obtenerCategoriaPorId(tarea.getIdCategoria()) == null) {
            System.out.println("Categoria no encontrada");
            return ResultadoOperacion.error("Categoria no encontrada");
        }
        
        if (validarFecha(tarea.getFechaLimite())) {
            System.out.println("La fecha limite no puede ser la misma que la fecha actual");
            return ResultadoOperacion.advertencia("La fecha es igual a la actual");
        }else{
            long fecha = tarea.getFechaLimite().getTime();
            java.sql.Date fechaSQL = new java.sql.Date(fecha);
            tarea.setFechaLimite(fechaSQL);
        }
        
        int idTarea = tareaImplem.create(tarea); //devuelve el id de la tarea creada

        if (idTarea > 0) {
            return ResultadoOperacion.exito("Tarea Creada Correctamente", idTarea);
        }

        return ResultadoOperacion.error("Hubo un problema al crear la tarea");
    }

    private boolean validarFecha(Date fecha) {
        LocalDate fechaLimite = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaHoy = LocalDate.now();

        return fechaLimite.isEqual(fechaHoy);

    }

    public boolean existe(String nombreTarea) {
        if (nombreTarea.trim().isEmpty()) {
            System.out.println("El nombre del tarea no puede ser vacio");
            return false;
        }

        return tareaImplem.existe(nombreTarea);
    }
}
