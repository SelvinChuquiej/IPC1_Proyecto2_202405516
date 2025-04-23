/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.selvin.main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.selvin.controller.ClienteAutomovilController;
import org.selvin.controller.LoginController;
import org.selvin.controller.RegistrarVehiculoController;
import org.selvin.controller.RegistrarClienteController;
import org.selvin.controller.RepuestosController;
import org.selvin.controller.ServiciosController;
import org.selvin.view.AdminMainView;
import org.selvin.view.ClienteAutoView;
import org.selvin.view.ClienteMainView;
import org.selvin.view.LoginView;
import org.selvin.view.RegistrarVehiculoView;
import org.selvin.view.RegistrarClienteView;
import org.selvin.view.RepuestosVerView;
import org.selvin.view.RepuestosView;
import org.selvin.view.ServiciosVerView;
import org.selvin.view.ServiciosView;
import org.selvin.view.VerVehiculosView;

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
    private RegistrarClienteController registrarClienteController;
    private RegistrarVehiculoController registrarVehiculoController;
    private RepuestosController repuestosController;
    private ServiciosController serviciosController;
    private ClienteAutomovilController clienteAutomovilController;

    private LoginView loginView;
    private RegistrarClienteView registrarClienteView;
    private RegistrarVehiculoView registrarVehiculoView;
    private AdminMainView adminMainView;
    private ClienteMainView clienteMainView;
    private RepuestosView repuestosView;
    private RepuestosVerView repuestosVerView;
    private ServiciosView serviciosView;
    private ServiciosVerView serviciosVerView;
    private VerVehiculosView verVehiculosView;
    private ClienteAutoView clienteAutoView;

    public Main() {
        registrarClienteController = new RegistrarClienteController();
        loginController = new LoginController(registrarClienteController);
        registrarVehiculoController = new RegistrarVehiculoController();

        repuestosController = new RepuestosController();
        serviciosController = new ServiciosController(repuestosController.repuestos);

        clienteAutomovilController = new ClienteAutomovilController(registrarClienteController.clientes, registrarVehiculoController.vehiculos, registrarClienteController, registrarVehiculoController);

        loginView = new LoginView(this, loginController);
        registrarClienteView = new RegistrarClienteView(this, registrarClienteController);
        registrarVehiculoView = new RegistrarVehiculoView(this, registrarVehiculoController);
        adminMainView = new AdminMainView(this);
        clienteMainView = new ClienteMainView(this);

        repuestosView = new RepuestosView(this, repuestosController);
        repuestosVerView = new RepuestosVerView(this, repuestosController);

        serviciosView = new ServiciosView(this, serviciosController);
        serviciosVerView = new ServiciosVerView(this, serviciosController);

        verVehiculosView = new VerVehiculosView(this, registrarVehiculoController);

        clienteAutoView = new ClienteAutoView(this, clienteAutomovilController);
        mostrarLoginView();
    }

    public void mostrarLoginView() {
        cambiarVentana(loginView);
        loginView.setLocationRelativeTo(null);
    }

    public void mostrarRegistrarClienteView() {
        cambiarVentana(registrarClienteView);
        registrarClienteView.setLocationRelativeTo(null);
    }

    public void mostrarRegistrarVehiculoView() {
        cambiarVentana(registrarVehiculoView);
        registrarVehiculoView.setLocationRelativeTo(null);
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

    public void mostrarServiciosVerView() {
        cambiarVentana(serviciosVerView);
        serviciosVerView.setLocationRelativeTo(null);
        serviciosVerView.cargarServicios();
    }

    public void mostrarVerVehiculosView() {
        cambiarVentana(verVehiculosView);
        verVehiculosView.setLocationRelativeTo(null);
        verVehiculosView.cargarVehiculos();
    }

    public void mostrarClienteAutoView(){
        cambiarVentana(clienteAutoView);
        clienteAutoView.setLocationRelativeTo(null);
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
