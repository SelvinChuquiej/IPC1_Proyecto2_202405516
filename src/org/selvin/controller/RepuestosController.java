/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.selvin.model.RepuestoModel;
import org.selvin.view.RepuestosView;

/**
 *
 * @author Selvi
 */
public class RepuestosController {

    private RepuestosView repuestosView;
    public Vector<RepuestoModel> repuestos = new Vector<RepuestoModel>();
    private int id = 1;

    public RepuestosController() {
    }

    public void seleccionarArchivo(JTextField txtRuta) {
        JFileChooser dlg = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos TMR (*.tmr)", "tmr");
        dlg.setFileFilter(filter);
        int option = dlg.showOpenDialog(repuestosView);
        if (option == JFileChooser.APPROVE_OPTION) {
            File archivo = dlg.getSelectedFile();
            txtRuta.setText(archivo.getPath());
            leerArchivoRepuestos(archivo);
        }
    }

    public void leerArchivoRepuestos(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                procesarLineaRepuesto(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarLineaRepuesto(String linea) {
        // Formato: nombreRepuesto-marca-modelo-Existencias-Precio
        String[] partes = linea.split("-");

        if (partes.length == 5) {
            try {
                String nombre = partes[0].trim();
                String marca = partes[1].trim();
                String modelo = partes[2].trim();
                int existencias = Integer.parseInt(partes[3].trim());
                double precio = Double.parseDouble(partes[4].trim());

                // Crear y agregar el nuevo repuesto
                RepuestoModel nuevoRepuesto = new RepuestoModel(id++, nombre, marca, modelo, existencias, precio);

                repuestos.add(nuevoRepuesto);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erro");
        }
    }

    // Método para guardar los repuestos en archivo binario (serialización)
    public void guardarRepuestos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("repuestos.dat"))) {
            oos.writeObject(repuestos);
            oos.writeInt(id); // Guardamos también el último ID usado
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrarRepuestos() {
        for (RepuestoModel c : repuestos) {
            System.out.println("id: " + c.getId());
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("marca: " + c.getMarca());
            System.out.println("modelo: " + c.getModelo());
            System.out.println("existencias: " + c.getExistencias());
            System.out.println("precio: " + c.getPrecio());
            System.out.println("--------------------");
        }
    }
}
