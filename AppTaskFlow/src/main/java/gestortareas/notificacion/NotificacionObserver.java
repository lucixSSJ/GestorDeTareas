package gestortareas.notificacion;

import gestortareas.model.Tarea;
import gestortareas.model.Usuario;

public interface NotificacionObserver {
    void notificarCreacionTarea(Tarea tarea, Usuario usuario);
    void notificarRecordatorioTarea(Tarea tarea, Usuario usuario);
    void notificarTareaVencida(Tarea tarea, Usuario usuario);
}
