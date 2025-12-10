package gestortareas.notificacion;

import gestortareas.model.Tarea;
import gestortareas.model.Usuario;

public interface NotificacionSubject {
    void agregarObserver(NotificacionObserver observer);
    void eliminarObserver(NotificacionObserver observer);
    void notificarCreacion(Tarea tarea, Usuario usuario);
    void notificarRecordatorio(Tarea tarea, Usuario usuario);
    void notificarVencimiento(Tarea tarea, Usuario usuario);
}
