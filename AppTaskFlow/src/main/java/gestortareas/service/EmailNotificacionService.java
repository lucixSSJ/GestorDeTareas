package gestortareas.service;

import gestortareas.dao.LogNotificacionDAO;
import gestortareas.model.LogNotificacion;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.time.LocalDateTime;

public class EmailNotificacionService {
    private final String username = "houlejhean81@gmail.com";
    private final String appPassword = "pwlh mthd utsd nptz";
    private final LogNotificacionDAO logNotificacionDAO;

    public EmailNotificacionService(LogNotificacionDAO logNotificacionDAO) {
        this.logNotificacionDAO = logNotificacionDAO;
    }

    public void enviarNotificacion(String emailDestino, String asunto, String mensaje,
                                   String tipoNotificacion, int idUsuario, Integer idTarea) {
        SimpleEmail email = new SimpleEmail();
        boolean enviadoCorrectamente = false;
        String error = null;

        try {
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator(username, appPassword));
            email.setSSLOnConnect(true);
            email.setFrom(username);
            email.setSubject(asunto);
            email.setMsg(mensaje);
            email.addTo(emailDestino);
            email.send();

            enviadoCorrectamente = true;
            System.out.println("Notificación enviada a: " + emailDestino);

        } catch (EmailException e) {
            error = e.getMessage();
            System.err.println("Error al enviar notificación: " + e.getMessage());
        } finally {
            // Registrar en logs
            LogNotificacion log = new LogNotificacion();
            log.setIdUsuario(idUsuario);
            log.setIdTarea(idTarea);
            log.setTipoNotificacion(tipoNotificacion);
            log.setEmailDestino(emailDestino);
            log.setAsunto(asunto);
            log.setMensaje(mensaje);
            log.setFechaEnvio(LocalDateTime.now());
            log.setEnviadoCorrectamente(enviadoCorrectamente);
            log.setErrorEnvio(error);

            logNotificacionDAO.guardarLog(log);
        }
    }
}
