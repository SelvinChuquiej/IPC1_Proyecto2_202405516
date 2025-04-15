/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.model;

/**
 *
 * @author Selvi
 */
public class ClienteModel extends LoginModel {

    private String dpi;
    private String nombreCompleto;
    private String tipoCliente; // normal u oro
    private VehiculoModel[] vehiculos;

    public ClienteModel() {
    }

    public ClienteModel(String dpi, String nombreCompleto, String tipoCliente) {
        this.dpi = dpi;
        this.nombreCompleto = nombreCompleto;
        this.tipoCliente = tipoCliente;
    }

    
    public ClienteModel(String dpi, String nombreCompleto, String tipoCliente, VehiculoModel[] vehiculos) {
        this.dpi = dpi;
        this.nombreCompleto = nombreCompleto;
        this.tipoCliente = tipoCliente;
        this.vehiculos = vehiculos;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public VehiculoModel[] getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(VehiculoModel[] vehiculos) {
        this.vehiculos = vehiculos;
    }

}
