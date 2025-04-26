/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.selvin.model.RepuestoModel;
import org.selvin.model.ServicioModel;
import org.selvin.view.ServiciosView;

/**
 *
 * @author Selvi
 */
public class ServiciosController {

    public ServicioModel[] servicios = new ServicioModel[25];
    private ServiciosView serviciosView;
    private RepuestosController repuestosController;
    private int id = 1;
    private int contServicios = 0;
    private DefaultTableModel dtm;

    public ServiciosController(RepuestosController repuestosControllers) {
        this.repuestosController = repuestosControllers;
    }

    public void seleccionarArchivoTMS(JTextField txtRuta) {
        JFileChooser dlg = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos TMS (*.tms)", "tms");
        dlg.setFileFilter(filter);
        int option = dlg.showOpenDialog(serviciosView);
        if (option == JFileChooser.APPROVE_OPTION) {
            File archivo = dlg.getSelectedFile();
            txtRuta.setText(archivo.getPath());
            leerArchivoServicio(archivo);
        }
    }

    private void leerArchivoServicio(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null && contServicios < servicios.length) {
                if (!linea.trim().isEmpty()) {
                    procesarLineaServicio(linea);
                }
            }
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

                for (String idStr : idsRepuestos) {
                    if (!idStr.trim().isEmpty()) {
                        int idRepuesto = Integer.parseInt(idStr.trim());
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
                System.out.println("Error");
            }
        } else {
            System.err.println("Error: Formato incorrecto en línea - se esperaban 5 partes");
        }
    }

    private RepuestoModel buscarRepuesto(int idRepuesto, String marca, String modelo) {
        for (RepuestoModel repuesto : repuestosController.getRepuestos()) {
            if (repuesto != null && repuesto.getId() == idRepuesto && repuesto.getMarca().equalsIgnoreCase(marca) && repuesto.getModelo().equalsIgnoreCase(modelo)) {
                return repuesto;
            }
        }
        System.out.println("Repuesto con ID " + idRepuesto + " no encontrado o no coincide con marca/modelo");
        return null;
    }

    /*public void mostrarServicios() {
        for (ServicioModel s : servicios) {
            if (s != null) {
                System.out.println("id: " + s.getId());
                System.out.println("Nombre: " + s.getNombre());
                System.out.println("marca: " + s.getMarca());
                System.out.println("modelo: " + s.getModelo());
                System.out.println("repuestos: ");
                RepuestoModel[] rp = s.getIdsRepuestos();
                if (rp != null && rp.length > 0) {
                    for (int i = 0; i < rp.length; i++) {
                        if (rp[i] != null) {
                            System.out.print(rp[i].getId());
                            if (i < rp.length - 1) {
                                System.out.print(", ");
                            }
                        }
                    }
                } else {
                    System.out.print("Ninguno");
                }
                System.out.println();
                System.out.println("precio Mano de Obra: " + s.getPrecioManoObra());
                System.out.println("precio Mano Total: " + s.getPrecioTotal());
                System.out.println("--------------------");
            }
        }
    }*/
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
}
