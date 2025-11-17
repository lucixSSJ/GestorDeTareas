/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appTaskFlow;

import interfacesGUI.frmPantallaPrincipal;

/**
 *
 * @author Luciano
 */
public class Main {
    public static void main(String[] args){        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Verificar conexión a la base de datos al iniciar
                if (utilidad.DatabaseConnection.testConnection()) {
                    System.out.println("✓ Aplicación iniciada con conexión a base de datos exitosa");
                } else {
                    System.out.println("⚠ Aplicación iniciada en modo SIN BASE DE DATOS (los datos se guardarán en memoria)");
                    System.out.println("  Para usar base de datos: instala el driver MySQL y configura la conexión");
                }
                
                // Iniciar la aplicación independientemente del estado de la base de datos
                frmPantallaPrincipal pantallaPrincipal = new frmPantallaPrincipal();
                pantallaPrincipal.setVisible(true);
            }
        });
    }
    
}
