package gestortareas.notificacion;

import gestortareas.model.Tarea;
import gestortareas.model.Usuario;
import gestortareas.service.EmailNotificacionService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailNotificacionObserver implements NotificacionObserver{
    private static final Logger LOGGER = Logger.getLogger(EmailNotificacionObserver.class.getName());
    private final EmailNotificacionService emailService;

    public EmailNotificacionObserver(EmailNotificacionService emailService) {
        this.emailService = emailService;
    }

    //logica para el envio del email de creacion
    @Override
    public void notificarCreacionTarea(Tarea tarea, Usuario usuario) {
        if (usuario != null && usuario.getEmail() != null) {
            try {
                String asunto = "Nueva Tarea Asignada: " + tarea.getNombre();
                String mensaje = String.format("""
                    Hola %s %s,
                    
                    Se te ha asignado una nueva tarea:
                    
                    📋 Tarea: %s
                    📝 Descripción: %s
                    📅 Fecha Límite: %s
                    ⚡ Prioridad: %s
                    📁 Categoría: %s
                    
                    Puedes ver los detalles en tu panel de tareas.
                    
                    Saludos,
                    Sistema de Gestión de Tareas
                    """,
                        usuario.getNombres(),
                        usuario.getApellidos(),
                        tarea.getNombre(),
                        tarea.getDescripcion(),
                        tarea.getFechaLimite().toString(),
                        tarea.getPrioridad(),
                        tarea.getCategoria() != null ? tarea.getCategoria().getNombre() : "Sin categoría"
                );

                emailService.enviarNotificacion(usuario.getEmail(), asunto, mensaje, "creacion", usuario.getIdUsuario(), tarea.getIdTarea());

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al enviar notificación de creación de tarea", e);
            }
        }
    }

    //logica para email de recordatorio
    @Override
    public void notificarRecordatorioTarea(Tarea tarea, Usuario usuario) {
        if (usuario != null && usuario.getEmail() != null && usuario.isNotificacionesVencimiento()) {
            try {
                String asunto = "⏰ Recordatorio: Tu tarea está por vencer";
                String mensaje = String.format("""
                    Hola %s %s,
                    
                    Esta es una notificación de recordatorio para tu tarea:
                    
                    📋 Tarea: %s
                    📝 Descripción: %s
                    ⏰ Fecha Límite: %s
                    ⚡ Prioridad: %s
                    
                    Te quedan 10 minutos para completarla.
                    
                    Saludos,
                    Sistema de Gestión de Tareas
                    """,
                        usuario.getNombres(),
                        usuario.getApellidos(),
                        tarea.getNombre(),
                        tarea.getDescripcion(),
                        tarea.getFechaLimite().toString(),
                        tarea.getPrioridad()
                );

                emailService.enviarNotificacion(usuario.getEmail(), asunto, mensaje, "recordatorio", usuario.getIdUsuario(), tarea.getIdTarea());

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al enviar notificación de recordatorio", e);
            }
        }
    }

    //logica de email de vencimiento
    @Override
    public void notificarTareaVencida(Tarea tarea, Usuario usuario) {
        if (usuario != null && usuario.getEmail() != null) {
            try {
                String asunto = "⚠️ Tarea Vencida: " + tarea.getNombre();
                String mensaje = String.format("""
                    Hola %s %s,
                    
                    Tu tarea ha vencido:
                    
                    📋 Tarea: %s
                    📝 Descripción: %s
                    🕒 Fecha Límite: %s
                    ⚡ Prioridad: %s
                    ❌ Estado: VENCIDA
                    
                    Por favor, actualiza el estado de la tarea.
                    
                    Saludos,
                    Sistema de Gestión de Tareas
                    """,
                        usuario.getNombres(),
                        usuario.getApellidos(),
                        tarea.getNombre(),
                        tarea.getDescripcion(),
                        tarea.getFechaLimite().toString(),
                        tarea.getPrioridad()
                );

                emailService.enviarNotificacion(usuario.getEmail(), asunto, mensaje, "vencimiento", usuario.getIdUsuario(), tarea.getIdTarea());

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al enviar notificación de tarea vencida", e);
            }
        }
    }
}
