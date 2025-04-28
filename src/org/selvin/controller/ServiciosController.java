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
import org.selvin.model.ServicioModel;
import org.selvin.view.ServiciosView;

/**
 *
 * @author Selvi
 */
public class ServiciosController {

    private static final String ARCHIVO_SERVICIOS = Main.CARPETA_DAT + "servicios.dat";

    public ServicioModel[] servicios = new ServicioModel[25];
    private ServiciosView serviciosView;
    private RepuestosController repuestosController;
    private RepuestoModel[] repuestosExistentes;
    private int id = 1;
    private int contServicios = 0;
    private DefaultTableModel dtm;

    public ServiciosController(RepuestosController repuestosController, RepuestoModel[] repuestosExistentes) {
        this.repuestosController = repuestosController;
        this.repuestosExistentes = repuestosExistentes;
        cargarServiciosDesdeArchivo();
    }

    public boolean seleccionarArchivoTMS(JTextField txtRuta) {
        JFileChooser dlg = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos TMS (*.tms)", "tms");
        dlg.setFileFilter(filter);
        int option = dlg.showOpenDialog(serviciosView);
        if (option == JFileChooser.APPROVE_OPTION) {
            File archivo = dlg.getSelectedFile();
            txtRuta.setText(archivo.getPath());
            leerArchivoServicio(archivo);
            return true;
        }
        return false;
    }

    private void leerArchivoServicio(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null && contServicios < servicios.length) {
                if (!linea.trim().isEmpty()) {
                    procesarLineaServicio(linea);
                }
            }
            guardarServicios();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarLineaServicio(String linea) {
        String[] partes = linea.split("-");
        if (partes.length == 5) {
            try {
                String nombreServicio = partes[0].trim();
                String marca = partes[1].trim();
                String modelo = partes[2].trim();
                String[] idsRepuestos = partes[3].trim().split(";");
                double precioManoObra = Double.parseDouble(partes[4].trim());

                if (nombreServicio.equalsIgnoreCase("Diagnóstico")) {
                    servicios[contServicios++] = new ServicioModel(id++, nombreServicio, marca, modelo, new RepuestoModel[0], precioManoObra, precioManoObra);
                    return;
                }

                RepuestoModel[] repuestosServicio = new RepuestoModel[idsRepuestos.length];
                int repuestosValidos = 0;
                double precioRepuestos = 0.0;

                for (String id : idsRepuestos) {
                    if (!id.trim().isEmpty()) {
                        int idRepuesto = Integer.parseInt(id.trim());
                        RepuestoModel repuesto = buscarRepuesto(idRepuesto, marca, modelo);
                        if (repuesto != null) {
                            repuestosServicio[repuestosValidos++] = repuesto;
                            precioRepuestos += repuesto.getPrecio();
                        }
                    }
                }
                RepuestoModel[] repuestosFinal = new RepuestoModel[repuestosValidos];
                System.arraycopy(repuestosServicio, 0, repuestosFinal, 0, repuestosValidos);

                double precioTotal = precioRepuestos + precioManoObra;

                servicios[contServicios++] = new ServicioModel(id++, nombreServicio, marca, modelo, repuestosFinal, precioManoObra, precioTotal);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error: Formato incorrecto en línea - se esperaban 5 partes");
        }
    }

    private RepuestoModel buscarRepuesto(int idRepuesto, String marca, String modelo) {
        for (RepuestoModel repuesto : repuestosExistentes) {
            if (repuesto != null && repuesto.getId() == idRepuesto && repuesto.getMarca().equalsIgnoreCase(marca) && repuesto.getModelo().equalsIgnoreCase(modelo)) {
                return repuesto;
            }
        }
        JOptionPane.showMessageDialog(null, "Repuesto con ID " + idRepuesto + " no encontrado o no coincide con marca/modelo");
        return null;
    }

    public void cargarServicios(JTable tblServicios) {
        dtm = (DefaultTableModel) tblServicios.getModel();
        dtm.setRowCount(0);
        for (ServicioModel s : servicios) {
            if (s != null) {
                Object[] datos = {
                    s.getId(),
                    s.getNombre(),
                    s.getMarca(),
                    s.getModelo(),
                    getRepuestosComoTexto(s.getIdsRepuestos()),
                    s.getPrecioManoObra(),
                    s.getPrecioTotal()
                };
                dtm.addRow(datos);
            }
        }
    }

    private String getRepuestosComoTexto(RepuestoModel[] repuestos) {
        if (repuestos == null || repuestos.length == 0) {
            return "Ninguno";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repuestos.length; i++) {
            if (repuestos[i] != null) {
                sb.append(repuestos[i].getId());
                if (i < repuestos.length - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    public ServicioModel[] getServicios() {
        ServicioModel[] result = new ServicioModel[contServicios];
        System.arraycopy(servicios, 0, result, 0, contServicios);
        return result;
    }

    public void cargarServiciosDesdeArchivo() {
        File archivo = new File(ARCHIVO_SERVICIOS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                servicios = (ServicioModel[]) ois.readObject();
                contServicios = 0;
                int maxId = 0;

                for (ServicioModel s : servicios) {
                    if (s != null) {
                        contServicios++;
                        if (s.getId() > maxId) {
                            maxId = s.getId();
                        }
                    }
                }

                id = maxId + 1;  // Actualiza el contador ID
                System.out.println("Servicios cargados correctamente desde " + ARCHIVO_SERVICIOS);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se encontró el archivo de servicios. Se iniciará vacío.");
        }
    }

    public void guardarServicios() {  // Cambia el nombre para evitar confusión
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_SERVICIOS))) {
            oos.writeObject(servicios);
            System.out.println("Servicios guardados correctamente en " + ARCHIVO_SERVICIOS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
