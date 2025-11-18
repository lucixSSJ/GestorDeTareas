package gestortareas.controller;

import gestortareas.dao.impl.EmailDAOImpl;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Michael Medina
 */
public class EmailController {
    private final EmailDAOImpl emailEnviar;

    public EmailController(EmailDAOImpl emailService) {
        this.emailEnviar = emailService;
    }

    public void EnviarCorreo(String email) {

        try {
            this.emailEnviar.enviarCorreo(email);
        } catch (EmailException ex) {
            System.out.println("Error al enviar el correo: " + ex.getMessage());
        }
    }
}
