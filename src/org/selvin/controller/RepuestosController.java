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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.selvin.main.Main;
import org.selvin.model.RepuestoModel;
import org.selvin.view.RepuestosView;

/**
 *
 * @author Selvi
 */
public class RepuestosController {

    private static final String ARCHIVO_REPUESTOS = Main.CARPETA_DAT + "repuestos.dat";

    public RepuestoModel[] repuestos = new RepuestoModel[25];
    private RepuestosView repuestosView;
    private int id = 1;
    private int contRepuestos = 0;
    private DefaultTableModel dtm;

    public RepuestosController() {
        cargarRepuestosDesdeArchivo();
    }

    public boolean seleccionarArchivoTMR(JTextField txtRuta) {
        JFileChooser dlg = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos TMR (*.tmr)", "tmr");
        dlg.setFileFilter(filter);
        int option = dlg.showOpenDialog(repuestosView);
        if (option == JFileChooser.APPROVE_OPTION) {
            File archivo = dlg.getSelectedFile();
            txtRuta.setText(archivo.getPath());
            leerArchivoRepuestos(archivo);
            return true;
        }
        return false;
    }

    private void leerArchivoRepuestos(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null && contRepuestos < repuestos.length) {
                if (!linea.trim().isEmpty()) {
                    procesarLineaRepuesto(linea);
                }
            }
            guardarRepuestos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarLineaRepuesto(String linea) {
        String[] partes = linea.split("-");
        if (partes.length == 5) {
            try {
                String nombre = partes[0].trim();
                String marca = partes[1].trim();
                String modelo = partes[2].trim();
                int existencias = Integer.parseInt(partes[3].trim());
                double precio = Double.parseDouble(partes[4].trim());

                if (contRepuestos < repuestos.length) {
                    RepuestoModel nuevoRepuesto = new RepuestoModel(id++, nombre, marca, modelo, existencias, precio);
                    repuestos[contRepuestos++] = nuevoRepuesto;
                } else {
                    JOptionPane.showMessageDialog(null, "Capacidad máxima de repuestos alcanzada");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "El archivo no cumple con el formato");
        }
    }

    public void cargarRepuestos(JTable tblRepuestos) {
        dtm = (DefaultTableModel) tblRepuestos.getModel();
        dtm.setRowCount(0);
        for (RepuestoModel r : repuestos) {
            if (r != null) {
                Object[] datos = {
                    r.getId(),
                    r.getNombre(),
                    r.getMarca(),
                    r.getModelo(),
                    r.getExistencias(),
                    r.getPrecio()
                };
                dtm.addRow(datos);
            }
        }
    }

    public void cargarRepuestosDesdeArchivo() {
        File archivo = new File(ARCHIVO_REPUESTOS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                repuestos = (RepuestoModel[]) ois.readObject();
                contRepuestos = 0;
                int maxId = 0;

                for (RepuestoModel r : repuestos) {
                    if (r != null) {
                        contRepuestos++;
                        if (r.getId() > maxId) {
                            maxId = r.getId();
                        }
                    }
                }

                id = maxId + 1;  // Actualiza el contador ID
                System.out.println("Repuestos cargados correctamente desde " + ARCHIVO_REPUESTOS);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se encontró el archivo de repuestos. Se iniciará vacío.");
        }
    }

    public void guardarRepuestos() {  // Cambia el nombre para evitar confusión
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_REPUESTOS))) {
            oos.writeObject(repuestos);
            System.out.println("Repuestos guardados correctamente en " + ARCHIVO_REPUESTOS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
