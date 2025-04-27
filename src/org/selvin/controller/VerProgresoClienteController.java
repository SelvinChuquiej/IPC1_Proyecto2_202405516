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

    private ClienteModel[] clientesExistentes;
    private VehiculoModel[] vehiculosExistentes;
    private ServicioModel[] serviciosExistentes;

    private DefaultTableModel dtm;

    public VerProgresoClienteController(ClienteModel[] clientesExistentes, VehiculoModel[] vehiculosExistentes, ServicioModel[] serviciosExistentes) {
        this.clientesExistentes = clientesExistentes;
        this.vehiculosExistentes = vehiculosExistentes;
        this.serviciosExistentes = serviciosExistentes;
    }

    public void cargarVehiculos(JComboBox<VehiculoModel> cmbVehiculos) {
        cmbVehiculos.removeAllItems();
        for (ClienteModel c : clientesExistentes) {
            if (c != null) {
                for (VehiculoModel v : vehiculosExistentes) {
                    if (v != null && v.getDpiLog() == c.getDpi()) {
                        cmbVehiculos.addItem(v);
                    }
                }
            }
        }
    }

    public void cargarServicios(JComboBox<ServicioModel> cmbServicios) {
        cmbServicios.removeAllItems();
        for (ServicioModel s : serviciosExistentes) {
            if (s != null) {
                cmbServicios.addItem(s);
            }
        }
    }
}
