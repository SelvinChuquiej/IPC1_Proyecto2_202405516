/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.selvin.model.OrdenTrabajoModel;

/**
 *
 * @author Selvi
 */
public class VerProgesoAdminController {

    private OrdenTrabajoController ordenTrabajoController;

    public VerProgesoAdminController(OrdenTrabajoController ordenTrabajoController) {
        this.ordenTrabajoController = ordenTrabajoController;
    }

    public void mostrarTodasLasOrdenes(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        for (int i = 0; i < ordenTrabajoController.countListos; i++) {
            OrdenTrabajoModel orden = ordenTrabajoController.vehiculosListos[i];
            model.addRow(new Object[]{
                orden.getNumeroOrden(),
                orden.getVehiculo().getPlaca(),
                orden.getVehiculo().getMarca(),
                orden.getVehiculo().getModelo(),
                orden.getCliente().getNombreCompleto(),
                orden.getServicio().getNombre(),
                orden.getServicio().getPrecioTotal(),
                orden.getEstado(),
                orden.getFechaFinalizacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            });
        }
    }
}
