package gestortareas.controller;

import gestortareas.dao.impl.EmailDAOImpl;
import gestortareas.dao.impl.UsuarioDAOImpl;
import gestortareas.service.EmailService;
import gestortareas.service.UsuarioService;
import java.util.Scanner;


/**
 *
 * @author Michael
 */
public class TestEmail {
    public static void main(String[] args) {
        /*
        Scanner sc = new Scanner(System.in);
        String correo = "micgael25ma@gmail.com";
        String token;
        
        EmailService emailService = new EmailService();
        UsuarioDAOImpl usuario = new UsuarioDAOImpl();
        
        EmailDAOImpl emailDAO = new EmailDAOImpl(usuario, emailService);
        EmailController email = new EmailController(emailDAO);
        
        UsuarioService usuaroService = new UsuarioService();
        
        email.EnviarCorreo(correo); //se envia el correo
 
        System.out.print("Ingresa el codigo de verificacion: ");
        token = sc.nextLine();
        
        System.out.print("Ingresa tu nueva contraseña: ");
        String clave = sc.nextLine();
        
        if (emailService.verificarCodigo(correo, token)) {
            usuaroService.resetearPassoword(clave, correo);
            System.out.println("Contraseña actualizada");
        }else{
            System.out.println("Algo salio mal");
        }
        */
        
    }
}
