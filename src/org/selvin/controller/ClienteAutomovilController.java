/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.selvin.model.ClienteModel;
import org.selvin.model.VehiculoModel;
import org.selvin.view.ClienteAutoView;

/**
 *
 * @author Selvi
 */
public class ClienteAutomovilController {

    private ClienteAutoView clienteAutoView;
    private ClienteModel[] clientesExistentes;
    private VehiculoModel[] vehiculosExistentes;
    private RegistrarClienteController registrarClienteController;
    private RegistrarVehiculoController registrarVehiculoController;
    private DefaultTableModel dtm;

    public ClienteAutomovilController(ClienteModel[] clientesExistentes, VehiculoModel[] vehiculosExistentes, RegistrarClienteController registrarClienteController, RegistrarVehiculoController registrarVehiculoController) {
        this.clientesExistentes = clientesExistentes;
        this.vehiculosExistentes = vehiculosExistentes;
        this.registrarClienteController = registrarClienteController;
        this.registrarVehiculoController = registrarVehiculoController;
    }

    public void seleccionarArchivoTMCA(JTextField txtRuta) {
        JFileChooser dlg = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos TMCA (*.tmca)", "tmca");
        dlg.setFileFilter(filter);
        int option = dlg.showOpenDialog(clienteAutoView);
        if (option == JFileChooser.APPROVE_OPTION) {
            File archivo = dlg.getSelectedFile();
            txtRuta.setText(archivo.getPath());
            leerArchivoClienteAuto(archivo);
        }
    }

    public void leerArchivoClienteAuto(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    procesarLineaClienteAuto(linea);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void procesarLineaClienteAuto(String linea) {
        String[] partes = linea.split("-");
        if (partes.length == 6) {
            try {
                String dpi = partes[0].trim();
                String nombre = partes[1].trim();
                String usuario = partes[2].trim();
                String contraseña = partes[3].trim();
                String tipoCliente = partes[4].trim();
                String automoviles = partes[5].trim();

                boolean clienteExiste = false;
                for (ClienteModel cliente : registrarClienteController.getClientes()) {
                    if (cliente != null && cliente.getDpi().equals(dpi)) {
                        clienteExiste = true;
                        break;
                    }
                }

                if (!clienteExiste) {
                    boolean clienteAdd = registrarClienteController.addClientes(usuario, contraseña, dpi, nombre);
                    if (clienteAdd) {
                        VehiculoModel[] vehiculos = procesarLineaVehiculo(automoviles, dpi);
                        for (VehiculoModel vehiculo : vehiculos) {
                            if (vehiculo != null) {
                                registrarVehiculoController.addVehiculos(vehiculo.getPlaca(), vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getRutaImagen(), dpi);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public VehiculoModel[] procesarLineaVehiculo(String listaVehiculos, String dpiCliente) {
        String[] autos = listaVehiculos.split(";");
        VehiculoModel[] vehiculos = new VehiculoModel[autos.length];
        for (int i = 0; i < autos.length; i++) {
            String[] datosAuto = autos[i].trim().split(",");
            if (datosAuto.length == 4) {
                vehiculos[i] = new VehiculoModel(
                        datosAuto[0].trim(),
                        datosAuto[1].trim(),
                        datosAuto[2].trim(),
                        datosAuto[3].trim(),
                        dpiCliente
                );
            }
        }
        return vehiculos;
    }

    public void mostrarClienteAuto() {
        // Obtener todos los clientes registrados
        ClienteModel[] clientes = registrarClienteController.getClientes();

        // Recorrer cada cliente
        for (ClienteModel cliente : clientes) {
            if (cliente != null) {
                System.out.println("DPI: " + cliente.getDpi());
                System.out.println("Nombre: " + cliente.getNombreCompleto());
                System.out.println("Usuario: " + cliente.getUsuario());
                System.out.println("Tipo Cliente: " + cliente.getTipoCliente());
                System.out.println("--- Automóviles ---");

                // Obtener y mostrar los vehículos de este cliente
                boolean tieneAutos = false;
                for (VehiculoModel vehiculo : registrarVehiculoController.vehiculos) {
                    if (vehiculo != null && vehiculo.getDpiLog() != null
                            && vehiculo.getDpiLog().equals(cliente.getDpi())) {
                        System.out.println("  Placa: " + vehiculo.getPlaca());
                        System.out.println("  Marca: " + vehiculo.getMarca());
                        System.out.println("  Modelo: " + vehiculo.getModelo());
                        System.out.println("  Imagen: " + vehiculo.getRutaImagen());
                        System.out.println("  --------------------");
                        tieneAutos = true;
                    }
                }

                if (!tieneAutos) {
                    System.out.println("  El cliente no tiene automóviles registrados.");
                }
                System.out.println("========================================");
            }
        }
    }
}
