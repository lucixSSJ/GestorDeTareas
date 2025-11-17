package gestortareas.service;

import gestortareas.dao.IEmailDAO;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author Michael Medina
 */
public class EmailService implements IEmailDAO{
    private final String username = "houlejhean81@gmail.com";
    private final String appPassword = "pwlh mthd utsd nptz";
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int CODE_LENGTH = 8;
    private final SimpleEmail email = new SimpleEmail();
    private Map<String,String> token = new HashMap<>();
    private Map<String, LocalDateTime> tiempoExpiracion = new HashMap<>();
    
    @Override
    public void enviarCorreo(String email) throws EmailException{
            String token = generarToken();
            this.token.put(email, token);
            this.tiempoExpiracion.put(email, LocalDateTime.now().plusMinutes(5));
            
            this.email.setHostName("smtp.gmail.com");
            this.email.setSmtpPort(587);
            this.email.setAuthenticator(new DefaultAuthenticator(username, appPassword));
            this.email.setSSLOnConnect(true);
            this.email.setFrom(username);
            this.email.setSubject("Codigo para restablecer la contraseña");
            this.email.setMsg("Usa este código para restablecer tu contraseña: "+ token);
            this.email.addTo(email);
            this.email.send();
    }
    
    public boolean verificarCodigo(String email, String codigo) {
        System.out.println("email: "+email);
        System.out.println("codigo: "+codigo);
        System.out.println(this.token.get(email));
        System.out.println("hash: "+this.token.isEmpty());
        if (email.isEmpty() && codigo.isEmpty()) {
            System.out.println("Campos vacíos");
            return false;
        }
        
        String codigoAlmacenado = this.token.get(email);
        LocalDateTime expiracion = this.tiempoExpiracion.get(email);
        
        if (codigoAlmacenado == null && expiracion == null) {
            System.out.println("Campos nulos");
            return false;
        }
        
        return codigoAlmacenado.equals(codigo) && expiracion.isAfter(LocalDateTime.now());
    }
    
    private String generarToken(){
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
