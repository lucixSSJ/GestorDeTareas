/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gestortareas.interfacesGUI;

import gestortareas.controller.CategoriaController;
import gestortareas.controller.NavigationController;
import gestortareas.controller.SessionController;
import gestortareas.interfacesAuxiliares.FrmAuxEditarCategoria;
import gestortareas.interfacesAuxiliares.FrmAuxNuevaCategoria;
import gestortareas.model.Categoria;
import gestortareas.model.Usuario;

import java.util.List;

/**
 *
 * @author SERT
 */
public class frmCategorias extends javax.swing.JFrame {
    private Usuario usuario;
    private NavigationController navigationController;
    private SessionController sessionController;
    private CategoriaController categoriaController;
    private Categoria categoriaSeleccionada;

    private javax.swing.table.DefaultTableModel modeloTabla;

    /**
     * Creates new form Login
     */
    public frmCategorias() {
        initComponents();
    }

    public frmCategorias(Usuario usuario, NavigationController navigationController) {
        this.usuario = usuario;
        this.navigationController = navigationController;
        this.sessionController = new SessionController(usuario);
        this.categoriaController = new CategoriaController(this, usuario);
        initComponents();
        configurarInterfaz();
    }

    private void configurarInterfaz(){
        setTitle("Gestión de Categorías - " + usuario.getNombres());
        setLocationRelativeTo(null);

        configurarTablaCategorias();
        categoriaController.cargarCategorias();
    }

    private void configurarTablaCategorias(){
        //columnas para incluir el ID (oculto)
        String[] columnas = {"ID", "Nombre de Categoria", "N° de tareas vinculadas"};

        modeloTabla = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTableMostrarCategoriasDisponibles.setModel(modeloTabla);

        // Ocultar la columna del ID
        jTableMostrarCategoriasDisponibles.removeColumn(jTableMostrarCategoriasDisponibles.getColumnModel().getColumn(0));

        jTableMostrarCategoriasDisponibles.getColumnModel().getColumn(0).setPreferredWidth(300);
        jTableMostrarCategoriasDisponibles.getColumnModel().getColumn(1).setPreferredWidth(150);

        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        jTableMostrarCategoriasDisponibles.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        jTableMostrarCategoriasDisponibles.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                manejarSeleccionCategoria();
            }
        });
    }


    public void mostrarCategorias(List<Categoria> categorias){
        modeloTabla.setRowCount(0);

        for (Categoria categoria : categorias){
            Object[] fila = {
                    categoria.getId(),
                    categoria.getNombre(),
                    categoria.getNumeroTareas()
            };
            modeloTabla.addRow(fila);
        }

        if (categorias.isEmpty()){
            Object[] filaVacia = {-1, "No hay categorias registradas", 0};
            modeloTabla.addRow(filaVacia);
        }

        System.out.println("Tabla actualizada con " + categorias.size() + " categorías.");
    }

    private void abrirFormularioNuevaCategoria(){
        FrmAuxNuevaCategoria formNuevaCategoria = new FrmAuxNuevaCategoria(this, usuario, categoriaController);
        formNuevaCategoria.setVisible(true);
    }

    private void editarCategoriaSeleccionada() {
        if (categoriaSeleccionada == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una categoría para editar",
                    "Selección Requerida",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        abrirFormularioEditarCategoria();
    }

    private void abrirFormularioEditarCategoria() {
        FrmAuxEditarCategoria formEditarCategoria = new FrmAuxEditarCategoria(
                this,
                usuario,
                categoriaController,
                categoriaSeleccionada
        );
        formEditarCategoria.setVisible(true);
    }

    private void manejarSeleccionCategoria(){
        int filaSeleccionada = jTableMostrarCategoriasDisponibles.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idCategoria = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);

            if (idCategoria != -1) {
                // Buscar la categoría por ID
                List<Categoria> categorias = categoriaController.obtenerCategoriasActuales();
                categoriaSeleccionada = categorias.stream()
                        .filter(cat -> cat.getId() == idCategoria)
                        .findFirst()
                        .orElse(null);

                if (categoriaSeleccionada != null) {
                    jButtonEditarCategoria.setEnabled(true);
                    jButtonEliminarCategoria.setEnabled(true);
                } else {
                    limpiarSeleccion();
                }
            } else {
                limpiarSeleccion();
            }
        } else {
            limpiarSeleccion();
        }
    }

    private void limpiarSeleccion(){
        categoriaSeleccionada = null;
        jButtonEditarCategoria.setEnabled(false);
        jButtonEliminarCategoria.setEnabled(false);
        jButtonEliminarCategoria.setToolTipText(null);
        jTableMostrarCategoriasDisponibles.clearSelection();
    }

    private void eliminarCategoriaSeleccionada(){
        if (categoriaSeleccionada == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una categoría para eliminar",
                    "Selección Requerida",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar la categoría:\n\n" +
                        "'" + categoriaSeleccionada.getNombre() + "'?\n\n" +
                        "Esta acción no se puede deshacer.",
                "Confirmar Eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            ejecutarEliminacion();
        }
    }

    private void ejecutarEliminacion(){
        try {
            // Verificar si la categoría tiene tareas asociadas
            if (categoriaSeleccionada.getNumeroTareas() > 0) {
                mostrarDialogoTareasAsociadas();
                return;
            }

            // Proceder con la eliminación
            boolean exito = categoriaController.eliminarCategoriaDirecta(categoriaSeleccionada.getId());

            if (exito) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Categoría eliminada exitosamente",
                        "Eliminación Exitosa",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

                // Limpiar selección y recargar categorías
                limpiarSeleccion();
                categoriaController.cargarCategorias();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error al eliminar la categoría",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al eliminar categoría: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoTareasAsociadas(){
        int opcion = javax.swing.JOptionPane.showConfirmDialog(this,
                "No se puede eliminar la categoría '" + categoriaSeleccionada.getNombre() + "'\n" +
                        "porque tiene " + categoriaSeleccionada.getNumeroTareas() + " tareas asociadas.\n\n" +
                        "¿Desea ver las tareas asociadas a esta categoría?",
                "No se puede eliminar",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jButtonInicio = new javax.swing.JButton();
        jButtonTareas = new javax.swing.JButton();
        jButtonPrioridad = new javax.swing.JButton();
        jButtonEstado = new javax.swing.JButton();
        jButtonExportar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonCerrarSesion = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMostrarCategoriasDisponibles = new javax.swing.JTable();
        jButtonEliminarCategoria = new javax.swing.JButton();
        jButtonEditarCategoria = new javax.swing.JButton();
        jTextFieldBuscarCategoriaPorNombre = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButtonCategoriaNueva = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(231, 231, 239));

        jPanel2.setBackground(new java.awt.Color(72, 74, 93));

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/categoria.png"))); // NOI18N
        jLabel9.setText("Categoria");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel9)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jButtonInicio.setBackground(new java.awt.Color(0, 0, 51));
        jButtonInicio.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButtonInicio.setForeground(new java.awt.Color(255, 255, 255));
        jButtonInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/casa.png"))); // NOI18N
        jButtonInicio.setText("Inicio");
        jButtonInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInicioActionPerformed(evt);
            }
        });

        jButtonTareas.setBackground(new java.awt.Color(0, 0, 51));
        jButtonTareas.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButtonTareas.setForeground(new java.awt.Color(255, 255, 255));
        jButtonTareas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/check blanco24px.png"))); // NOI18N
        jButtonTareas.setText("Tareas");
        jButtonTareas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTareasActionPerformed(evt);
            }
        });

        jButtonPrioridad.setBackground(new java.awt.Color(0, 0, 51));
        jButtonPrioridad.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButtonPrioridad.setForeground(new java.awt.Color(255, 255, 255));
        jButtonPrioridad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/estrella.png"))); // NOI18N
        jButtonPrioridad.setText("Prioridad");
        jButtonPrioridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrioridadActionPerformed(evt);
            }
        });

        jButtonEstado.setBackground(new java.awt.Color(0, 0, 51));
        jButtonEstado.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButtonEstado.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/archivos.png"))); // NOI18N
        jButtonEstado.setText("Estado");
        jButtonEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEstadoActionPerformed(evt);
            }
        });

        jButtonExportar.setBackground(new java.awt.Color(0, 0, 51));
        jButtonExportar.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButtonExportar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonExportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exportar (1).png"))); // NOI18N
        jButtonExportar.setText("Exportar");
        jButtonExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonTareas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonPrioridad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonExportar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jButtonInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonTareas, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPrioridad, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExportar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(41, 43, 66));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuario 32.png"))); // NOI18N

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo.png"))); // NOI18N

        jButtonCerrarSesion.setBackground(new java.awt.Color(255, 0, 51));
        jButtonCerrarSesion.setForeground(new java.awt.Color(0, 0, 102));
        jButtonCerrarSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cerrarSesion.png"))); // NOI18N
        jButtonCerrarSesion.setBorder(null);
        jButtonCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(6, 6, 6))
                            .addComponent(jButtonCerrarSesion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35))))
        );

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(32, 32, 32));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu.png"))); // NOI18N
        jLabel12.setText(" Categorias registradas");

        jTableMostrarCategoriasDisponibles.setBackground(new java.awt.Color(195, 216, 229));
        jTableMostrarCategoriasDisponibles.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jTableMostrarCategoriasDisponibles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nombre de la Categoria", "N° de tareas vinculadas"
            }
        ));
        jTableMostrarCategoriasDisponibles.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(jTableMostrarCategoriasDisponibles);

        jButtonEliminarCategoria.setBackground(new java.awt.Color(255, 51, 51));
        jButtonEliminarCategoria.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButtonEliminarCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/borrar.png"))); // NOI18N
        jButtonEliminarCategoria.setText("Eliminar");
        jButtonEliminarCategoria.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, new java.awt.Color(255, 51, 51), new java.awt.Color(255, 51, 51), new java.awt.Color(255, 51, 51), new java.awt.Color(255, 51, 51)));
        jButtonEliminarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarCategoriaActionPerformed(evt);
            }
        });

        jButtonEditarCategoria.setBackground(new java.awt.Color(248, 246, 138));
        jButtonEditarCategoria.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButtonEditarCategoria.setForeground(new java.awt.Color(51, 51, 51));
        jButtonEditarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar.png"))); // NOI18N
        jButtonEditarCategoria.setText("Editar");
        jButtonEditarCategoria.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, new java.awt.Color(250, 233, 104), new java.awt.Color(250, 233, 104), new java.awt.Color(250, 233, 104), new java.awt.Color(250, 233, 104)));
        jButtonEditarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditarCategoriaActionPerformed(evt);
            }
        });

        jTextFieldBuscarCategoriaPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBuscarCategoriaPorNombreActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lupa negro 24px.png"))); // NOI18N

        jButtonCategoriaNueva.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/signo-de-mas.png"))); // NOI18N
        jButtonCategoriaNueva.setBorder(null);
        jButtonCategoriaNueva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCategoriaNuevaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonCategoriaNueva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldBuscarCategoriaPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(jButtonEliminarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(102, 102, 102)
                        .addComponent(jButtonEditarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(104, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jTextFieldBuscarCategoriaPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCategoriaNueva, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonEliminarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonEditarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    
    //textfiels para buscar categoria por nombres
    private void jTextFieldBuscarCategoriaPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBuscarCategoriaPorNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBuscarCategoriaPorNombreActionPerformed

    //boton para editar categoria
    private void jButtonEditarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditarCategoriaActionPerformed
        editarCategoriaSeleccionada();
    }//GEN-LAST:event_jButtonEditarCategoriaActionPerformed

    //botón para ir a inicio
    private void jButtonInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInicioActionPerformed
        navigationController.volverAlDashboard(this);
    }//GEN-LAST:event_jButtonInicioActionPerformed

    //botón para ir a Tareas
    private void jButtonTareasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTareasActionPerformed
        navigationController.irATareasDesdeCategorias(this);
    }//GEN-LAST:event_jButtonTareasActionPerformed

    //botón para ir a prioridad
    private void jButtonPrioridadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrioridadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrioridadActionPerformed

    //botón para ir a estado
    private void jButtonEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonEstadoActionPerformed

    //botón para ir a exportar
    private void jButtonExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonExportarActionPerformed

    //botón para cerrar sesión
    private void jButtonCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarSesionActionPerformed
        sessionController.cerrarSesion(this);
    }//GEN-LAST:event_jButtonCerrarSesionActionPerformed

    //botón para eliminar categoria
    private void jButtonEliminarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarCategoriaActionPerformed
        eliminarCategoriaSeleccionada();
    }//GEN-LAST:event_jButtonEliminarCategoriaActionPerformed

    //abrir interfaz de agregar categorias nuevas
    private void jButtonCategoriaNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCategoriaNuevaActionPerformed
        abrirFormularioNuevaCategoria();
    }//GEN-LAST:event_jButtonCategoriaNuevaActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCategoriaNueva;
    private javax.swing.JButton jButtonCerrarSesion;
    private javax.swing.JButton jButtonEditarCategoria;
    private javax.swing.JButton jButtonEliminarCategoria;
    private javax.swing.JButton jButtonEstado;
    private javax.swing.JButton jButtonExportar;
    private javax.swing.JButton jButtonInicio;
    private javax.swing.JButton jButtonPrioridad;
    private javax.swing.JButton jButtonTareas;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableMostrarCategoriasDisponibles;
    private javax.swing.JTextField jTextFieldBuscarCategoriaPorNombre;
    // End of variables declaration//GEN-END:variables
}
