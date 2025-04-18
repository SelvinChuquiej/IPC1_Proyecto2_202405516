/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.selvin.main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.selvin.controller.LoginController;
import org.selvin.controller.RegistrarseController;
import org.selvin.controller.RepuestosController;
import org.selvin.controller.ServiciosController;
import org.selvin.view.AdminMainView;
import org.selvin.view.ClienteMainView;
import org.selvin.view.LoginView;
import org.selvin.view.RegistrarseView;
import org.selvin.view.RepuestosVerView;
import org.selvin.view.RepuestosView;
import org.selvin.view.ServiciosVerView;
import org.selvin.view.ServiciosView;

/**
 *
 * @author Selvi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    private JFrame ventanaActual;

    private LoginController loginController;
    private RegistrarseController registrarseController;
    private RepuestosController repuestosController;
    private ServiciosController serviciosController;

    private LoginView loginView;
    private RegistrarseView registrarseView;
    private AdminMainView adminMainView;
    private ClienteMainView clienteMainView;
    private RepuestosView repuestosView;
    private RepuestosVerView repuestosVerView;
    private ServiciosView serviciosView;
    private ServiciosVerView serviciosVerView;

    public Main() {

        registrarseController = new RegistrarseController();
        loginController = new LoginController(registrarseController);
        repuestosController = new RepuestosController();
        serviciosController = new ServiciosController(repuestosController.repuestos);

        loginView = new LoginView(this, loginController);
        registrarseView = new RegistrarseView(this, registrarseController);
        adminMainView = new AdminMainView(this);
        clienteMainView = new ClienteMainView(this);
        repuestosView = new RepuestosView(this, repuestosController);
        repuestosVerView = new RepuestosVerView(this, repuestosController);
        serviciosView = new ServiciosView(this, serviciosController);
        serviciosVerView = new ServiciosVerView(this, serviciosController);
        //mostrarLoginView();
        mostrarRepuestosView();
    }

    public void mostrarLoginView() {
        cambiarVentana(loginView);
        loginView.setLocationRelativeTo(null);
    }

    public void mostrarRegistrarView() {
        cambiarVentana(registrarseView);
        registrarseView.setLocationRelativeTo(null);
    }

    public void mostrarAdminMainView() {
        cambiarVentana(adminMainView);
        adminMainView.setLocationRelativeTo(null);
    }

    public void mostrarClienteMainView() {
        cambiarVentana(clienteMainView);
        clienteMainView.setLocationRelativeTo(null);
    }

    public void mostrarRepuestosView() {
        cambiarVentana(repuestosView);
        repuestosView.setLocationRelativeTo(null);
    }

    public void mostrarRepuestosVerView() {
        cambiarVentana(repuestosVerView);
        repuestosVerView.setLocationRelativeTo(null);
        repuestosVerView.cargarRepuestos();
    }

    public void mostrarServiciosView() {
        cambiarVentana(serviciosView);
        serviciosView.setLocationRelativeTo(null);
    }
    
    public void mostrarServiciosVerView(){
        cambiarVentana(serviciosVerView);
        serviciosVerView.setLocationRelativeTo(null);
        serviciosVerView.cargarServicios();
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
