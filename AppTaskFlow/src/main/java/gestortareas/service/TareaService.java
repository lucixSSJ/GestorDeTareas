package gestortareas.service;

import gestortareas.DTO.TareaDTO;
import gestortareas.DTO.TareaDetalle;
import gestortareas.dao.CategoriaDAO;
import gestortareas.dao.TareaDao;
import gestortareas.dao.UsuarioDAO;
import gestortareas.model.Categoria;
import gestortareas.model.Tarea;
import gestortareas.model.Usuario;
import gestortareas.utilidad.ResultadoOperacion;
import org.modelmapper.ModelMapper;

import java.security.PublicKey;
import java.util.List;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Michael Medina
 */
public class TareaService {

    private final TareaDao tareaDao;
    private final UsuarioDAO usuarioService;
    private final CategoriaDAO categoriaService;

    public TareaService(TareaDao  tareaDao, UsuarioDAO usuarioDAO, CategoriaDAO categoriaDAO) {
        this.tareaDao = tareaDao;
        this.usuarioService = usuarioDAO;
        this.categoriaService = categoriaDAO;
    }

    public ResultadoOperacion<Integer> create(Tarea tarea) {
        if (usuarioService.obtenerPorId(tarea.getUsuario().getIdUsuario()) == null) {
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
        
        if (tareaDao.existe(tarea.getNombre().trim())) {
            System.out.println("La tarea ya existe");
            return ResultadoOperacion.advertencia("La tarea ya existe");
        }

        if (categoriaService.obtenerPorId(tarea.getCategoria().getId()) == null) {
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
        
        int idTarea = tareaDao.create(tarea); //devuelve el id de la tarea creada

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

        return tareaDao.existe(nombreTarea);
    }

    public ResultadoOperacion<List<TareaDTO>> getTareas(Usuario user) {
        if (user == null) {
            return ResultadoOperacion.advertencia("Usuario no encontrado");
        }

        List<Tarea> tareas = tareaDao.getTareas(user);
        ModelMapper modelMapper = new ModelMapper();
        if (!tareas.isEmpty()){
            return ResultadoOperacion.exito("",tareas.stream().map(tarea -> modelMapper.map(tarea,TareaDTO.class)).toList());
        }else {
            return ResultadoOperacion.advertencia("Ocurrio un problema al obtener los tareas");
        }
    }

    public ResultadoOperacion<TareaDetalle>  getTareaDetalle(int idTarea) {
        if (idTarea == 0) {
            return ResultadoOperacion.advertencia("No hay una tarea seleccionada");
        }

        TareaDetalle tareaDetalle = tareaDao.getTareaIDdetalle(idTarea);

        if (tareaDetalle != null) {
            return ResultadoOperacion.exito("Tarea seleccionada", tareaDetalle);
        }else{
            return ResultadoOperacion.advertencia("No existe el tarea seleccionada");
        }
    }

    public ResultadoOperacion<Boolean> actualizarTarea(Tarea tarea) {
       boolean ok = tareaDao.actualizarTarea(tarea);
       if (ok) {
           return ResultadoOperacion.exito("Datos actualizados",true);
       }else{
           return ResultadoOperacion.error("Ocurrio un problema al actualizar la tarea");
       }
    }

    public ResultadoOperacion<Tarea> obtenerTarea(int idTarea) {
        if (idTarea == 0) {
            return ResultadoOperacion.advertencia("No hay una tarea seleccionada");
        }

        Tarea tareaObtenida = this.tareaDao.obtenerTarea(idTarea);

        if (tareaObtenida != null) {
            return ResultadoOperacion.exito("Tarea encontrada",tareaObtenida);
        }else {
            return ResultadoOperacion.error("No existe el tarea encontrada");
        }
    }
}
