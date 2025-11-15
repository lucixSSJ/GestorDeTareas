/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appTaskFlow;

import interfacesGUI.frmTareasGeneral;

/**
 *
 * @author Luciano
 */
public class Main {
    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new frmTareasGeneral().setVisible(true);
            }
        });
    }
    
}
