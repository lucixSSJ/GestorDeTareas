/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortareas.appTaskFlow;

import gestortareas.interfacesGUI.frmInicioSesion;

import javax.swing.*;

/**
 *
 * @author Luciano
 */
public class GestorTareasApp {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            try {
                frmInicioSesion inicioSesion = new frmInicioSesion();
                inicioSesion.setVisible(true);
                inicioSesion.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error al iniciar la aplicación: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
}
