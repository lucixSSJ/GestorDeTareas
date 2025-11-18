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
    private String mensaje;
    public EmailDAOImpl(UsuarioDAOImpl usuario, EmailService emailService) {
        this.usuario = usuario;
        this.emailService = emailService;
    }

    @Override
    public void enviarCorreo(String email) throws EmailException {
        boolean existe = usuario.existeEmail(email);
        
        System.out.println("Existe el correo: "+existe);
        
        if (!existe) {
            System.out.println("El correo no existe");
            this.mensaje = "El correo no existe";
        }else{
            this.mensaje = " ";
            System.out.println("El correo existe");
            System.out.println("Mensaje: "+this.mensaje);
            
            this.emailService.enviarCorreo(email);
        }
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    
    
}
