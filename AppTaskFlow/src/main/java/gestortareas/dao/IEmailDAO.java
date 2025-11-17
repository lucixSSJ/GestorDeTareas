package gestortareas.dao;

import org.apache.commons.mail.EmailException;

/**
 *
 * @author Michael Medina
 */
public interface IEmailDAO {
    void enviarCorreo(String email) throws EmailException;
}
