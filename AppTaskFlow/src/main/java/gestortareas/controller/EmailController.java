package gestortareas.controller;

import gestortareas.interfacesGUI.frmIngresarCodigoClave;
import gestortareas.interfacesGUI.frmResetPassword;
import gestortareas.service.EmailService;
import gestortareas.service.UsuarioService;
/**
 *
 * @author Michael Medina
 */
public class EmailController {
    private frmResetPassword resetPassword;

    public EmailController(frmResetPassword resetPassword) {
        this.resetPassword = resetPassword;
    }

    public void abrirEnvioCorreo() {
        this.resetPassword.setVisible(true);
        this.resetPassword.setResizable(false);
        this.resetPassword.setLocationRelativeTo(null);
    }

    public void abrirActualizarClave(EmailService emailService,
            UsuarioService usuarioService, String correo) {

        frmIngresarCodigoClave ingresarCodigo = new frmIngresarCodigoClave(emailService, usuarioService, correo);
        ingresarCodigo.setVisible(true);
        ingresarCodigo.setResizable(true);
        ingresarCodigo.setLocationRelativeTo(null);
    }
}
