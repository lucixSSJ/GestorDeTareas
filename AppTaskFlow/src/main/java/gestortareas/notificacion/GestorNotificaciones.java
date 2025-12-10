package gestortareas.notificacion;

import gestortareas.model.Tarea;
import gestortareas.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorNotificaciones implements NotificacionSubject{
    private static GestorNotificaciones instancia; // Singleton -> una sola instancia en toda la aplicación
    private final List<NotificacionObserver> observers;

    private GestorNotificaciones() {
        this.observers = new ArrayList<>();
    }

    public static synchronized GestorNotificaciones getInstance() {
        if (instancia == null) {
            instancia = new GestorNotificaciones();
        }
        return instancia;
    }

    //metodos para gestionar observers
    @Override
    public void agregarObserver(NotificacionObserver observer) {
        observers.add(observer);
    }

    @Override
    public void eliminarObserver(NotificacionObserver observer) {
        observers.remove(observer);
    }

    //metodos para notificar a todos los observers
    @Override
    public void notificarCreacion(Tarea tarea, Usuario usuario) {
        for (NotificacionObserver observer : observers) {
            observer.notificarCreacionTarea(tarea, usuario);
        }
    }

    @Override
    public void notificarRecordatorio(Tarea tarea, Usuario usuario) {
        for (NotificacionObserver observer : observers) {
            observer.notificarRecordatorioTarea(tarea, usuario);
        }
    }

    @Override
    public void notificarVencimiento(Tarea tarea, Usuario usuario) {
        for (NotificacionObserver observer : observers) {
            observer.notificarTareaVencida(tarea, usuario);
        }
    }
}
