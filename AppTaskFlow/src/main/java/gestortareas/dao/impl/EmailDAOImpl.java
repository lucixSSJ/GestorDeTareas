package gestortareas.dao.impl;

import gestortareas.dao.IEmailDAO;
import gestortareas.service.EmailService;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Michael Medina
 */
public class EmailDAOImpl implements IEmailDAO {

    private final UsuarioDAOImpl usuario;
    private final EmailService emailService;

     public EmailDAOImpl(UsuarioDAOImpl usuario, EmailService emailService) {
        this.usuario = usuario;
        this.emailService = emailService;
    }

    @Override
    public void enviarCorreo(String email) throws EmailException {

        if (usuario.existeEmail(email)) {
            this.emailService.enviarCorreo(email);
        } else {
            System.out.println("El correo ingresado no existe");
        }

    }
}
