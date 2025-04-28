/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.selvin.main.Main;
import org.selvin.model.ClienteModel;
import org.selvin.model.VehiculoModel;
import org.selvin.view.ClienteAutoView;

/**
 *
 * @author Selvi
 */
public class ClienteAutomovilController {

    private static final String ARCHIVO_DATOS = Main.CARPETA_DAT + "clienteAutoDatos.dat";

    private ClienteAutoView clienteAutoView;
    private ClienteModel[] clientesExistentes;
    private VehiculoModel[] vehiculosExistentes;
    private RegistrarClienteController registrarClienteController;
    private VehiculoController vehiculoController;
    private DefaultTableModel dtm;

    public ClienteAutomovilController(ClienteModel[] clientesExistentes, VehiculoModel[] vehiculosExistentes, RegistrarClienteController registrarClienteController, VehiculoController vehiculoController) {
        this.clientesExistentes = clientesExistentes;
        this.vehiculosExistentes = vehiculosExistentes;

        this.registrarClienteController = registrarClienteController;
        this.vehiculoController = vehiculoController;

        cargarDatos();
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
            guardarDatos();
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

                long dpiCliente = Long.parseLong(dpi.trim());
                boolean clienteExiste = false;
                for (ClienteModel cliente : clientesExistentes) {

                    if (cliente != null && cliente.getDpi() == dpiCliente) {
                        clienteExiste = true;
                        break;
                    }
                }

                if (!clienteExiste) {
                    boolean clienteAdd = registrarClienteController.addClientes(usuario, contraseña, dpiCliente, nombre);
                    if (clienteAdd) {
                        VehiculoModel[] vehiculos = procesarLineaVehiculo(automoviles, dpiCliente);
                        for (VehiculoModel vehiculo : vehiculos) {
                            if (vehiculo != null) {
                                vehiculoController.addVehiculos(vehiculo.getPlaca(), vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getRutaImagen(), dpiCliente);
                            }
                        }
                        vehiculoController.guardarVehiculos();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public VehiculoModel[] procesarLineaVehiculo(String listaVehiculos, long dpiCliente) {
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

    public void cargarClienteAutos(JTable tblClienteAuto) {
        dtm = (DefaultTableModel) tblClienteAuto.getModel();
        dtm.setRowCount(0);
        tblClienteAuto.getColumnModel().getColumn(7).setCellRenderer(new VehiculoController.ImageRenderer());

        for (ClienteModel c : clientesExistentes) {
            if (c != null) {
                for (VehiculoModel v : vehiculosExistentes) {
                    if (v != null && v.getDpiLog() == c.getDpi()) {
                        Object[] datos = {
                            c.getDpi(),
                            c.getNombreCompleto(),
                            c.getUsuario(),
                            c.getTipoCliente(),
                            v.getPlaca(),
                            v.getMarca(),
                            v.getModelo(),
                            v.getRutaImagen(),};
                        dtm.addRow(datos);
                    }
                }
            }
        }
    }

    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(clientesExistentes);
            oos.writeObject(vehiculosExistentes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                clientesExistentes = (ClienteModel[]) ois.readObject();
                vehiculosExistentes = (VehiculoModel[]) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
