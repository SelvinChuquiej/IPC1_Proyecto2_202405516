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
    private DefaultTableModel dtm;

    public ClienteAutomovilController(ClienteModel[] clientesExistentes, VehiculoModel[] vehiculosExistentes) {
        this.clientesExistentes = clientesExistentes;
        this.vehiculosExistentes = vehiculosExistentes;
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
                String id = partes[0].trim();
                String nombre = partes[1].trim();
                String usuario = partes[2].trim();
                String contraseña = partes[3].trim();
                String tipoCliente = partes[4].trim();
                String automoviles = partes[5].trim();
            } catch (Exception e) {
            }
        }
    }

    public VehiculoModel[] procesarVehiculos(String listaAutos) {
        String[] autos = listaAutos.split(";");
        VehiculoModel[] vehiculos = new VehiculoModel[autos.length];
        for (int i = 0; i < autos.length; i++) {
            String[] datosAuto = autos[i].trim().split(",");
            if (datosAuto.length == 4) {
                vehiculos[i] = new VehiculoModel(
                        datosAuto[0].trim(),
                        datosAuto[1].trim(),
                        datosAuto[2].trim(),
                        datosAuto[3].trim()
                );
            }
        }
        return vehiculos;
    }

}
