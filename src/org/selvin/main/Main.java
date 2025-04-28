/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.selvin.main;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.selvin.controller.ClienteAutomovilController;
import org.selvin.controller.FacturaController;
import org.selvin.controller.LoginController;
import org.selvin.controller.OrdenTrabajoController;
import org.selvin.controller.VehiculoController;
import org.selvin.controller.RegistrarClienteController;
import org.selvin.controller.RepuestosController;
import org.selvin.controller.ServiciosController;
import org.selvin.controller.VerProgesoAdminController;
import org.selvin.controller.VerProgresoClienteController;
import org.selvin.view.AdminMainView;
import org.selvin.view.ClienteAutoView;
import org.selvin.view.ClienteMainView;
import org.selvin.view.FacturaView;
import org.selvin.view.LoginView;
import org.selvin.view.RegistrarVehiculoView;
import org.selvin.view.RegistrarClienteView;
import org.selvin.view.RepuestosVerView;
import org.selvin.view.RepuestosView;
import org.selvin.view.ServiciosVerView;
import org.selvin.view.ServiciosView;
import org.selvin.view.VerClienteAutoView;
import org.selvin.view.VerProgresoAdminView;
import org.selvin.view.VerProgresoClienteView;
import org.selvin.view.VerVehiculosView;

/**
 *
 * @author Selvi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static final String CARPETA_DAT = "datos/";

    private JFrame ventanaActual;
    private LoginController loginController;
    private RegistrarClienteController registrarClienteController;
    private VehiculoController vehiculoController;
    private RepuestosController repuestosController;
    private ServiciosController serviciosController;
    private ClienteAutomovilController clienteAutomovilController;
    private VerProgresoClienteController verProgresoClienteController;
    private OrdenTrabajoController ordenTrabajoController;
    private FacturaController facturaController;
    private VerProgesoAdminController verProgesoAdminController;

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
    private VerClienteAutoView verClienteAutoView;
    private VerProgresoClienteView verProgresoClienteView;
    private FacturaView facturaView;
    private VerProgresoAdminView verProgresoAdminView;

    public Main() {

        File directorio = new File(CARPETA_DAT);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        registrarClienteController = new RegistrarClienteController();
        loginController = new LoginController(registrarClienteController.clientes, registrarClienteController.empleados);
        vehiculoController = new VehiculoController();

        repuestosController = new RepuestosController();
        serviciosController = new ServiciosController(repuestosController, repuestosController.repuestos);

        clienteAutomovilController = new ClienteAutomovilController(registrarClienteController.clientes, vehiculoController.vehiculos, registrarClienteController, vehiculoController);

        verProgresoClienteController = new VerProgresoClienteController(registrarClienteController.clientes, vehiculoController.vehiculos, serviciosController.servicios);
        ordenTrabajoController = new OrdenTrabajoController(registrarClienteController.empleados, registrarClienteController.clientes, serviciosController);

        facturaController = new FacturaController(ordenTrabajoController);
        verProgesoAdminController = new VerProgesoAdminController(ordenTrabajoController);

        loginView = new LoginView(this, loginController);
        registrarClienteView = new RegistrarClienteView(this, registrarClienteController);
        registrarVehiculoView = new RegistrarVehiculoView(this, vehiculoController);
        adminMainView = new AdminMainView(this);
        clienteMainView = new ClienteMainView(this);

        repuestosView = new RepuestosView(this, repuestosController);
        repuestosVerView = new RepuestosVerView(this, repuestosController);

        serviciosView = new ServiciosView(this, serviciosController);
        serviciosVerView = new ServiciosVerView(this, serviciosController);

        verVehiculosView = new VerVehiculosView(this, vehiculoController);

        clienteAutoView = new ClienteAutoView(this, clienteAutomovilController);
        verClienteAutoView = new VerClienteAutoView(this, clienteAutomovilController);

        verProgresoClienteView = new VerProgresoClienteView(this, verProgresoClienteController, ordenTrabajoController);

        facturaView = new FacturaView(this, facturaController);
        verProgresoAdminView = new VerProgresoAdminView(this, verProgesoAdminController);
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

    public void mostrarClienteAutoView() {
        cambiarVentana(clienteAutoView);
        clienteAutoView.setLocationRelativeTo(null);
    }

    public void mostrarVerClienteAutoView() {
        cambiarVentana(verClienteAutoView);
        verClienteAutoView.setLocationRelativeTo(null);
        verClienteAutoView.cargarClienteAutos();
    }

    public void mostrarVerProgresoClienteView() {
        cambiarVentana(verProgresoClienteView);
        verProgresoClienteView.setLocationRelativeTo(null);
        verProgresoClienteView.cargarDatos();
    }

    public void mostrarFactura() {
        cambiarVentana(facturaView);
        facturaView.setLocationRelativeTo(null);
        facturaView.cargar();
    }

    public void mostrarVerProgresoAdmin() {
        cambiarVentana(verProgresoAdminView);
        verProgresoAdminView.setLocationRelativeTo(null);
        verProgresoAdminView.cargarDatos();
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
