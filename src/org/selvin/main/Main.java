/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.selvin.main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.selvin.view.LoginView;

/**
 *
 * @author Selvi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    private JFrame ventanaActual;

    private LoginView loginView;

    public Main() {
        loginView = new LoginView();
        
        
        mostrarLoginView();
    }

    private void mostrarLoginView() {
        cambiarVentana(loginView);
        loginView.setLocationRelativeTo(null);
    }

    private void cambiarVentana(Ventana newVentana) {
        if (ventanaActual != null) {
            ventanaActual.setVisible(true);
        }
        newVentana.mostrar();
        ventanaActual = (JFrame) newVentana;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> new Main());
    }
}
