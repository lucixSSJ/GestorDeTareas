package gestortareas.config;

import gestortareas.dao.impl.LogNotificacionDAOImpl;
import gestortareas.notificacion.EmailNotificacionObserver;
import gestortareas.notificacion.GestorNotificaciones;
import gestortareas.service.EmailNotificacionService;

public class NotificacionConfig {
    public static void inicializarSistemaNotificaciones() {
        // Crear servicios
        LogNotificacionDAOImpl logDAO = new LogNotificacionDAOImpl();
        EmailNotificacionService emailService = new EmailNotificacionService(logDAO);

        //2 creacion de un Observer de Email
        EmailNotificacionObserver emailObserver = new EmailNotificacionObserver(emailService);

        //1 creación del subject GESTOR
        GestorNotificaciones gestor = GestorNotificaciones.getInstance();
        //3 Suscribir el Observer al Subject
        gestor.agregarObserver(emailObserver);

        System.out.println("Sistema de notificaciones inicializado");
    }

}
