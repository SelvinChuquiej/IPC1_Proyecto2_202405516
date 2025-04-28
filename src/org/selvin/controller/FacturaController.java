package org.selvin.controller;

import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.selvin.model.OrdenTrabajoModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Selvi
 */
public class FacturaController {

    private OrdenTrabajoController ordenTrabajoController;

    public FacturaController(OrdenTrabajoController ordenTrabajoController) {
        this.ordenTrabajoController = ordenTrabajoController;
    }

    public boolean registrarPago(int numeroOrden) {
        for (int i = 0; i < ordenTrabajoController.countListos; i++) {
            if (ordenTrabajoController.vehiculosListos[i].getNumeroOrden() == numeroOrden) {
                ordenTrabajoController.vehiculosListos[i].setPagado(true);
                ordenTrabajoController.vehiculosListos[i].setEstado("Pagado");
                System.out.println("Pago registrado para orden #" + numeroOrden);
                return true;
            }
        }
        System.out.println("No se encontró la orden #" + numeroOrden);
        return false;
    }

    public void mostrarCarrosListosPendientes(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0); // Limpiar tabla

        // Recorrer todas las órdenes pero mostrar solo las no pagadas
        for (int i = 0; i < ordenTrabajoController.countListos; i++) {
            OrdenTrabajoModel orden = ordenTrabajoController.vehiculosListos[i];
            if (!orden.isPagado()) {
                model.addRow(new Object[]{
                    orden.getNumeroOrden(),
                    orden.getVehiculo().getPlaca(),
                    orden.getVehiculo().getMarca(),
                    orden.getVehiculo().getModelo(),
                    orden.getServicio().getNombre(),
                    orden.getServicio().getPrecioTotal(),
                    orden.getFechaFinalizacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                });
            }
        }
    }

    public void procesarPago(JTable tabla) {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int numeroOrden = (int) tabla.getValueAt(filaSeleccionada, 0);
            if (registrarPago(numeroOrden)) {
                mostrarCarrosListosPendientes(tabla); // Actualizar vista
                JOptionPane.showMessageDialog(null, "Pago registrado exitosamente");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un vehículo de la lista", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

}
