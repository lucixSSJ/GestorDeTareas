package gestortareas.service;

import gestortareas.DTO.TareaDTO;
import gestortareas.DTO.TareaDetalle;
import gestortareas.dao.CategoriaDAO;
import gestortareas.dao.TareaDao;
import gestortareas.dao.UsuarioDAO;
import gestortareas.model.Categoria;
import gestortareas.model.Tarea;
import gestortareas.model.Usuario;
import gestortareas.notificacion.GestorNotificaciones;
import gestortareas.utilidad.ResultadoOperacion;
import org.modelmapper.ModelMapper;

import javax.xml.crypto.Data;
import java.security.PublicKey;
import java.util.List;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Michael Medina
 */
public class TareaService {

    private final TareaDao tareaDao;
    private final UsuarioDAO usuarioService;
    private final CategoriaDAO categoriaService;
    private final GestorNotificaciones gestorNotificaciones;
    private final ScheduledExecutorService scheduler;

    public TareaService(TareaDao  tareaDao, UsuarioDAO usuarioDAO, CategoriaDAO categoriaDAO) {
        this.tareaDao = tareaDao;
        this.usuarioService = usuarioDAO;
        this.categoriaService = categoriaDAO;
        this.gestorNotificaciones = GestorNotificaciones.getInstance();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public ResultadoOperacion<Integer> create(Tarea tarea) {
        // Validar que el usuario existe
        if (usuarioService.obtenerPorId(tarea.getUsuario().getIdUsuario()) == null) {
            System.out.println("Usuario no encontrado");
            return ResultadoOperacion.error("Usuario no encontrado");
        }

        // Validar campos obligatorios
        if (tarea.getNombre() == null || tarea.getNombre().trim().isEmpty() ||
                tarea.getDescripcion() == null || tarea.getDescripcion().trim().isEmpty()) {
            System.out.println("Los campos del nombre y descripcion son importantes");
            return ResultadoOperacion.advertencia("Los campos del nombre y descripcion son importantes");
        }

        // Validar fecha límite
        if (tarea.getFechaLimite() == null) {
            return ResultadoOperacion.advertencia("Selecciona una fecha límite para la tarea");
        }

        // Validar que la tarea no exista
        if (tareaDao.existe(tarea.getNombre().trim())) {
            System.out.println("La tarea ya existe");
            return ResultadoOperacion.advertencia("La tarea ya existe");
        }

        // Validar categoría
        if (categoriaService.obtenerPorId(tarea.getCategoria().getId()) == null) {
            System.out.println("Categoria no encontrada");
            return ResultadoOperacion.error("Categoria no encontrada");
        }

        // Establecer fecha de creación actual
        Date fechaCreacion = new Date();
        tarea.setFechaCreacion(fechaCreacion);

        // Validar que la fecha límite sea al menos 10 minutos después de la creación
        long minutosDiferencia = calcularDiferenciaMinutos(tarea.getFechaLimite(), fechaCreacion);

        // Si la fecha límite es anterior a la fecha de creación
        if (tarea.getFechaLimite().before(fechaCreacion)) {
            System.out.println("La fecha límite no puede ser en el pasado");
            return ResultadoOperacion.advertencia("La fecha límite no puede ser en el pasado");
        }

        // Si la diferencia es menor a 10 minutos
        if (minutosDiferencia < 10) {
            System.out.println("La tarea debe tener un tiempo mayor de 10 minutos desde su creación");
            return ResultadoOperacion.advertencia("La tarea debe tener un tiempo mínimo de 10 minutos desde su creación");
        }

        // Convertir a Timestamp de SQL
        java.sql.Timestamp timestampLimite = new java.sql.Timestamp(tarea.getFechaLimite().getTime());
        tarea.setFechaLimite(timestampLimite);

        // También convertir fecha de creación a Timestamp
        java.sql.Timestamp timestampCreacion = new java.sql.Timestamp(fechaCreacion.getTime());
        tarea.setFechaCreacion(timestampCreacion);

        // Crear la tarea
        int idTarea = tareaDao.create(tarea);

        if (idTarea > 0) {
            // Obtener la tarea completa con ID
            Tarea tareaCreada = tareaDao.obtenerTarea(idTarea);

            if (tareaCreada != null) {
                // Obtener usuario con email
                gestortareas.model.Usuario usuario = usuarioService.obtenerPorId(tarea.getUsuario().getIdUsuario());

                if (usuario != null) {
                    // Notificar creación inmediatamente
                    gestorNotificaciones.notificarCreacion(tareaCreada, usuario);

                    // Programar recordatorio 10 minutos antes del vencimiento
                    programarRecordatorio(tareaCreada, usuario);

                    // Programar notificación de vencimiento
                    programarNotificacionVencimiento(tareaCreada, usuario);
                }
            }

            return ResultadoOperacion.exito("Tarea Creada Correctamente", idTarea);
        }

        return ResultadoOperacion.error("Hubo un problema al crear la tarea");
    }

    private void programarRecordatorio(Tarea tarea, gestortareas.model.Usuario usuario) {
        if (usuario != null && usuario.isNotificacionesVencimiento()) {
            Date fechaLimite = tarea.getFechaLimite();
            Date ahora = new Date();

            // Calcular minutos hasta la fecha límite
            long minutosHastaVencimiento = calcularDiferenciaMinutos(fechaLimite, ahora);

            // Solo programar recordatorio si faltan más de 10 minutos
            if (minutosHastaVencimiento > 10) {
                // Programar recordatorio 10 minutos antes
                long tiempoParaRecordatorio = (minutosHastaVencimiento - 10) * 60 * 1000;

                System.out.println("Recordatorio programado para: " + tiempoParaRecordatorio + "ms (" +
                        (minutosHastaVencimiento - 10) + " minutos)");

                scheduler.schedule(() -> {
                    try {
                        // Verificar que la tarea no esté completada
                        Tarea tareaActualizada = tareaDao.obtenerTarea(tarea.getIdTarea());
                        if (tareaActualizada != null &&
                                !"completada".equals(tareaActualizada.getEstado()) &&
                                !"vencida".equals(tareaActualizada.getEstado())) {

                            System.out.println("Enviando recordatorio para tarea: " + tareaActualizada.getNombre());
                            gestorNotificaciones.notificarRecordatorio(tareaActualizada, usuario);
                        }
                    } catch (Exception e) {
                        System.err.println("Error en recordatorio: " + e.getMessage());
                    }
                }, tiempoParaRecordatorio, TimeUnit.MILLISECONDS);
            } else {
                System.out.println("Recordatorio no programado - la tarea vence en menos de 10 minutos");
            }
        }
    }

    private void programarNotificacionVencimiento(Tarea tarea, gestortareas.model.Usuario usuario) {
        Date fechaLimite = tarea.getFechaLimite();
        Date ahora = new Date();

        long tiempoParaVencimiento = fechaLimite.getTime() - ahora.getTime();

        if (tiempoParaVencimiento > 0) {
            scheduler.schedule(() -> {
                // Verificar que la tarea no esté completada
                Tarea tareaActualizada = tareaDao.obtenerTarea(tarea.getIdTarea());
                if (tareaActualizada != null && !"completada".equals(tareaActualizada.getEstado())) {
                    // Actualizar estado a vencida
                    tareaActualizada.setEstado("vencida");
                    tareaDao.actualizarTarea(tareaActualizada);

                    // Notificar vencimiento
                    gestorNotificaciones.notificarVencimiento(tareaActualizada, usuario);
                }
            }, tiempoParaVencimiento, TimeUnit.MILLISECONDS);
        }
    }

    private boolean validarFecha(Date fechaLimite) {
        Date fechaActual = new Date();
        long diferenciaMinutos = calcularDiferenciaMinutos(fechaLimite, fechaActual);

        return diferenciaMinutos < 10;
    }

    private long calcularDiferenciaMinutos(Date fechaLimite, Date fechaCreacion) {
        long diferenciaMs = fechaLimite.getTime() - fechaCreacion.getTime();
        return diferenciaMs / (60 * 1000);
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

        // Siempre devolver una lista, aunque esté vacía
        List<TareaDTO> tareasDTO = tareas.stream()
                .map(tarea -> modelMapper.map(tarea, TareaDTO.class))
                .toList();

        // Si hay tareas, devolver éxito con los datos
        if (!tareasDTO.isEmpty()) {
            return ResultadoOperacion.exito("Tareas obtenidas correctamente", tareasDTO);
        } else {
            // Si no hay tareas, devolver éxito con lista vacía
            return ResultadoOperacion.exito("No hay tareas disponibles", tareasDTO);
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

    public ResultadoOperacion<Boolean> eliminarTarea(int Idtarea) {
        if (Idtarea == 0) {
            return ResultadoOperacion.advertencia("No hay una tarea seleccionada");
        }

        if (tareaDao.obtenerTarea(Idtarea) == null) {
            return ResultadoOperacion.error("No existe el tarea seleccionada");
        }

        boolean ok = tareaDao.eliminarTarea(Idtarea);

        if (ok) {
            return ResultadoOperacion.exito("Tarea eliminada",true);
        }else{
            return ResultadoOperacion.error("No se pudo eliminar la tarea");
        }
    }
}
