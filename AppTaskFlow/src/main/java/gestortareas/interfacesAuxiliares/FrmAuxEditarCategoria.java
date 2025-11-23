/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gestortareas.interfacesAuxiliares;

import gestortareas.controller.CategoriaController;
import gestortareas.interfacesGUI.frmCategorias;
import gestortareas.model.Categoria;
import gestortareas.model.Usuario;

import javax.swing.*;

/**
 *
 * @author Luciano
 */
public class FrmAuxEditarCategoria extends javax.swing.JFrame {
    private frmCategorias parentForm;
    private Usuario usuario;
    private CategoriaController categoriaController;
    private Categoria categoriaAEditar;
    private String nombreOriginal;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmAuxEditarCategoria.class.getName());

    /**
     * Creates new form FrmAuxEditarCategoria
     */
    public FrmAuxEditarCategoria() {
        initComponents();
    }

    public FrmAuxEditarCategoria(frmCategorias parent, Usuario usuario,
                                 CategoriaController controller, Categoria categoria) {
        this.parentForm = parent;
        this.usuario = usuario;
        this.categoriaController = controller;
        this.categoriaAEditar = categoria;
        this.nombreOriginal = categoria.getNombre();

        initComponents();
        configurarInterfaz();
    }

    private void configurarInterfaz() {
        setTitle("Editar Categoría - " + nombreOriginal);
        setLocationRelativeTo(parentForm);

        // Mostrar el nombre actual de la categoría
        jTextFieldNombreCategoria.setText(nombreOriginal);
        jTextFieldNombreCategoria.requestFocus();
        jTextFieldNombreCategoria.selectAll(); // Seleccionar todo el texto para fácil edición

        configurarEventos();
    }

    private void guardarCambios() {
        String nuevoNombre = jTextFieldNombreCategoria.getText().trim();

        // Validaciones básicas
        if (nuevoNombre.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El nombre de la categoría no puede estar vacío",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            jTextFieldNombreCategoria.requestFocus();
            return;
        }

        if (nuevoNombre.length() > 100) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El nombre de la categoría no puede tener más de 100 caracteres",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            jTextFieldNombreCategoria.requestFocus();
            jTextFieldNombreCategoria.selectAll();
            return;
        }

        // Verificar si el nombre cambió
        if (nuevoNombre.equals(nombreOriginal)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El nombre de la categoría no ha cambiado",
                    "Sin Cambios",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            cerrarFormulario();
            return;
        }

        // Verificar duplicados
        if (categoriaController.verificarCategoriaExistente(nuevoNombre, categoriaAEditar.getId())) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Ya existe otra categoría con el nombre: '" + nuevoNombre + "'",
                    "Categoría Existente",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            jTextFieldNombreCategoria.requestFocus();
            jTextFieldNombreCategoria.selectAll();
            return;
        }

        // Confirmar modificación
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea modificar la categoría?\n\n" +
                        "Nombre original: " + nombreOriginal + "\n" +
                        "Nuevo nombre: " + nuevoNombre,
                "Confirmar Modificación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            // Usar el controlador para actualizar la categoría
            categoriaController.actualizarCategoria(categoriaAEditar.getId(), nuevoNombre);
            cerrarFormulario();
        }
    }

    private void cancelarEdicion() {
        // Preguntar si realmente quiere cancelar si hay cambios
        String nombreActual = jTextFieldNombreCategoria.getText().trim();

        if (!nombreActual.equals(nombreOriginal)) {
            int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this,
                    "Tiene cambios sin guardar. ¿Está seguro que desea cancelar?",
                    "Confirmar Cancelación",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE);

            if (confirmacion != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }
        }

        cerrarFormulario();
    }

    private void cerrarFormulario(){
        this.dispose();
    }

    private void configurarEventos() {
        this.getRootPane().setDefaultButton(jButtonGuardarCambios);

        jTextFieldNombreCategoria.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarEstadoBotonGuardar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarEstadoBotonGuardar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarEstadoBotonGuardar();
            }
        });

        actualizarEstadoBotonGuardar();
    }

    private void actualizarEstadoBotonGuardar() {
        String textoActual = jTextFieldNombreCategoria.getText().trim();
        boolean hayCambios = !textoActual.equals(nombreOriginal) && !textoActual.isEmpty();
        jButtonGuardarCambios.setEnabled(hayCambios);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldNombreCategoria = new javax.swing.JTextField();
        jButtonCancelar = new javax.swing.JButton();
        jButtonGuardarCambios = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(207, 207, 227));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(102, 102, 102))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(32, 32, 32));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/categoria.png"))); // NOI18N
        jLabel13.setText("Editar Categoria");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel13)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel22.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/check.png"))); // NOI18N
        jLabel22.setText("Nombre de categoria");

        jTextFieldNombreCategoria.setFont(new java.awt.Font("Segoe UI Semilight", 0, 13)); // NOI18N
        jTextFieldNombreCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNombreCategoriaActionPerformed(evt);
            }
        });

        jButtonCancelar.setBackground(new java.awt.Color(0, 80, 202));
        jButtonCancelar.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButtonCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nada.png"))); // NOI18N
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(51, 102, 255))); // NOI18N
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButtonGuardarCambios.setBackground(new java.awt.Color(73, 167, 73));
        jButtonGuardarCambios.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButtonGuardarCambios.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarCambios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/registrar.png"))); // NOI18N
        jButtonGuardarCambios.setText("Guadar");
        jButtonGuardarCambios.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(68, 164, 61))); // NOI18N
        jButtonGuardarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarCambiosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonGuardarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jTextFieldNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(jLabel22)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonGuardarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(154, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 401, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 473, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //jTextFiel nuevo nombre de categoria editar
    private void jTextFieldNombreCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNombreCategoriaActionPerformed
        guardarCambios();
    }//GEN-LAST:event_jTextFieldNombreCategoriaActionPerformed

    //jButton para cancelar
    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        cancelarEdicion();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    //jButton guardar cambios
    private void jButtonGuardarCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarCambiosActionPerformed
        guardarCambios();
    }//GEN-LAST:event_jButtonGuardarCambiosActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonGuardarCambios;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextFieldNombreCategoria;
    // End of variables declaration//GEN-END:variables
}
