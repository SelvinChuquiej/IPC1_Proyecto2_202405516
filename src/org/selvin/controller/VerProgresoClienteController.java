/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.selvin.model.ClienteModel;
import org.selvin.model.ServicioModel;
import org.selvin.model.VehiculoModel;

/**
 *
 * @author Selvi
 */
public class VerProgresoClienteController {

    private RegistrarClienteController registrarClienteController;
    private VehiculoController vehiculoController;
    private ServiciosController serviciosController;

    private DefaultTableModel dtm;

    public VerProgresoClienteController(RegistrarClienteController registrarClienteController, VehiculoController vehiculoController, ServiciosController serviciosController) {
        this.registrarClienteController = registrarClienteController;
        this.vehiculoController = vehiculoController;
        this.serviciosController = serviciosController;
    }

    public void cargarVehiculos(JComboBox<VehiculoModel> cmbVehiculos) {
        cmbVehiculos.removeAllItems();
        for (ClienteModel c : registrarClienteController.getClientes()) {
            if (c != null) {
                for (VehiculoModel v : vehiculoController.getVehiculos()) {
                    if (v != null && v.getDpiLog() != null && v.getDpiLog().equals(c.getDpi())) {
                        cmbVehiculos.addItem(v);
                    }
                }
            }
        }
    }

    public void cargarServicios(JComboBox<ServicioModel> cmbServicios) {
        cmbServicios.removeAllItems();
        for (ServicioModel s : serviciosController.getServicios()) {
            if (s != null) {
                cmbServicios.addItem(s);
            }
        }
    }
    
}
