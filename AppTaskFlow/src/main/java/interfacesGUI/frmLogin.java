/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfacesGUI;

import service.UsuarioService;
import javax.swing.JOptionPane;
import utilidad.WindowStateManager;

/**
 * Interfaz de inicio de sesión
 * @author Luciano
 */
public class frmLogin extends javax.swing.JFrame {

    private UsuarioService usuarioService;

    /**
     * Creates new form frmLogin
     */
    public frmLogin() {
        initComponents();
        usuarioService = new UsuarioService();
        configurarVentana();
    }
    
    private void configurarVentana() {
        WindowStateManager.getInstance().configureWindow(this);
        this.setTitle("TaskFlow - Inicio de Sesión");
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        
        // Centrar ventana
        this.setLocationRelativeTo(null);
        
        // Configurar eventos Enter
        configurarEventosEnter();
        
        // Configurar campos editables
        txtUsuario.setEditable(true);
        txtContraseña.setEditable(true);
        
        // Configurar placeholders
        configurarPlaceholders();
    }
    
    private void configurarPlaceholders() {
        // Agregar listeners para manejar placeholders
        txtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if ("Usuario".equals(txtUsuario.getText())) {
                    txtUsuario.setText("");
                    txtUsuario.setForeground(java.awt.Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtUsuario.getText().trim().isEmpty()) {
                    txtUsuario.setText("Usuario");
                    txtUsuario.setForeground(new java.awt.Color(102, 102, 102));
                }
            }
        });
        
        txtContraseña.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if ("Contraseña".equals(txtContraseña.getText())) {
                    txtContraseña.setText("");
                    txtContraseña.setForeground(java.awt.Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtContraseña.getText().trim().isEmpty()) {
                    txtContraseña.setText("Contraseña");
                    txtContraseña.setForeground(new java.awt.Color(102, 102, 102));
                }
            }
        });
    }
    
    private void configurarEventosEnter() {
        // Enter en campo usuario pasa a contraseña
        txtUsuario.addActionListener(e -> txtContraseña.requestFocus());
        
        // Enter en contraseña ejecuta login
        txtContraseña.addActionListener(e -> realizarLogin());
    }
    
    private void realizarLogin() {
        String username = txtUsuario.getText().trim();
        String password = txtContraseña.getText().trim();
        
        // Limpiar placeholders
        if ("Usuario".equals(username)) {
            username = "";
        }
        if ("Contraseña".equals(password)) {
            password = "";
        }
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese su usuario", 
                                        "Campo requerido", JOptionPane.WARNING_MESSAGE);
            txtUsuario.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese su contraseña", 
                                        "Campo requerido", JOptionPane.WARNING_MESSAGE);
            txtContraseña.requestFocus();
            return;
        }
        
        ingresar.setEnabled(false);
        ingresar.setText("Validando...");
        
        try {
            boolean loginExitoso = usuarioService.login(username, password);
            
            if (loginExitoso) {
                JOptionPane.showMessageDialog(this, 
                    "¡Bienvenido " + UsuarioService.getUsuarioLogueado().getNombreCompleto() + "!", 
                    "Login exitoso", JOptionPane.INFORMATION_MESSAGE);
                
                // Abrir pantalla principal
                abrirPantallaPrincipal();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Usuario o contraseña incorrectos", 
                    "Error de autenticación", JOptionPane.ERROR_MESSAGE);
                limpiarCampos();
                txtUsuario.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al intentar iniciar sesión: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            ingresar.setEnabled(true);
            ingresar.setText("Ingresar");
        }
    }
    
    private void abrirPantallaPrincipal() {
        this.setVisible(false);
        
        // Crear y mostrar pantalla principal
        java.awt.EventQueue.invokeLater(() -> {
            new frmPantallaPrincipal().setVisible(true);
        });
        
        this.dispose();
    }
    
    private void limpiarCampos() {
        txtContraseña.setText("");
        txtContraseña.requestFocus();
    }
    
    private void abrirRegistro() {
        // TODO: Crear frmRegistro
        JOptionPane.showMessageDialog(this, "Funcionalidad de registro próximamente", 
                                    "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
        // frmRegistro registro = new frmRegistro();
        // registro.setVisible(true);
        // this.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelizquierdo2 = new javax.swing.JPanel();
        txtUsuario = new javax.swing.JTextField();
        txtContraseña = new javax.swing.JTextField();
        cn2 = new javax.swing.JLabel();
        cn1 = new javax.swing.JLabel();
        textoregistrarse = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ingresar = new javax.swing.JButton();
        btnRegistro = new javax.swing.JButton();
        contenedor = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelizquierdo2.setBackground(new java.awt.Color(41, 43, 66));
        panelizquierdo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtUsuario.setEditable(false);
        txtUsuario.setBackground(new java.awt.Color(190, 228, 215));
        txtUsuario.setForeground(new java.awt.Color(102, 102, 102));
        txtUsuario.setText("Usuario");
        txtUsuario.setBorder(null);
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        panelizquierdo2.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, 50, 30));

        txtContraseña.setEditable(false);
        txtContraseña.setBackground(new java.awt.Color(190, 228, 215));
        txtContraseña.setForeground(new java.awt.Color(102, 102, 102));
        txtContraseña.setText("Contraseña");
        txtContraseña.setBorder(null);
        txtContraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraseñaActionPerformed(evt);
            }
        });
        panelizquierdo2.add(txtContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 220, 70, 30));

        cn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/contenedor2.png"))); // NOI18N
        panelizquierdo2.add(cn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 140, 240, 50));

        cn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/contenedor2.png"))); // NOI18N
        panelizquierdo2.add(cn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 210, -1, 50));

        textoregistrarse.setForeground(new java.awt.Color(51, 51, 51));
        textoregistrarse.setText("¿No tienes cuenta aun?");
        panelizquierdo2.add(textoregistrarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 340, -1, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo.png"))); // NOI18N
        panelizquierdo2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 270, 90));

        jLabel3.setBackground(new java.awt.Color(0, 102, 255));
        jLabel3.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 255));
        jLabel3.setText("Bienvenido");
        panelizquierdo2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, -1, -1));

        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Inicie sesión con su correo electrónico");
        panelizquierdo2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 100, -1, -1));

        ingresar.setBackground(new java.awt.Color(0, 102, 255));
        ingresar.setFont(new java.awt.Font("Berlin Sans FB", 0, 16)); // NOI18N
        ingresar.setForeground(new java.awt.Color(255, 255, 255));
        ingresar.setText("Ingresar");
        ingresar.setAlignmentY(0.05F);
        ingresar.setBorderPainted(false);
        ingresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingresarActionPerformed(evt);
            }
        });
        panelizquierdo2.add(ingresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 290, 100, 30));

        btnRegistro.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistro.setForeground(new java.awt.Color(0, 153, 255));
        btnRegistro.setText("Registrate");
        btnRegistro.setBorderPainted(false);
        btnRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroActionPerformed(evt);
            }
        });
        panelizquierdo2.add(btnRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 360, 100, 20));

        contenedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/contenedor.png"))); // NOI18N
        contenedor.setText("jLabel2");
        panelizquierdo2.add(contenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 330, 410));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 5, Short.MAX_VALUE)
                .addComponent(panelizquierdo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 5, Short.MAX_VALUE)
                .addComponent(panelizquierdo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // Limpiar placeholder y mover a contraseña
        if ("Usuario".equals(txtUsuario.getText())) {
            txtUsuario.setText("");
        }
        txtContraseña.requestFocus();
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void txtContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraseñaActionPerformed
        // Limpiar placeholder y ejecutar login
        if ("Contraseña".equals(txtContraseña.getText())) {
            txtContraseña.setText("");
        }
        realizarLogin();
    }//GEN-LAST:event_txtContraseñaActionPerformed

    private void ingresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingresarActionPerformed
        realizarLogin();
    }//GEN-LAST:event_ingresarActionPerformed

    private void btnRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroActionPerformed
        abrirRegistro();
    }//GEN-LAST:event_btnRegistroActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegistro;
    private javax.swing.JLabel cn1;
    private javax.swing.JLabel cn2;
    private javax.swing.JLabel contenedor;
    private javax.swing.JButton ingresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel panelizquierdo2;
    private javax.swing.JLabel textoregistrarse;
    private javax.swing.JTextField txtContraseña;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
