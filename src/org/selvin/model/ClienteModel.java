/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.model;

import java.io.Serializable;

/**
 *
 * @author Selvi
 */
public class ClienteModel extends LoginModel implements Serializable {

    private long dpi; //ID
    private String nombreCompleto;
    private String tipoCliente;// normal u oro
    private int serviciosRealizados;
    private VehiculoModel[] vehiculos;

    public ClienteModel() {
    }

    public ClienteModel(long dpi, String nombreCompleto, String tipoCliente) {
        this.dpi = dpi;
        this.nombreCompleto = nombreCompleto;
        this.tipoCliente = tipoCliente;
    }

    public ClienteModel(long dpi, String nombreCompleto, String tipoCliente, int serviciosRealizados, VehiculoModel[] vehiculos) {
        this.dpi = dpi;
        this.nombreCompleto = nombreCompleto;
        this.tipoCliente = tipoCliente;
        this.serviciosRealizados = 0;
        this.vehiculos = vehiculos;
    }

    public long getDpi() {
        return dpi;
    }

    public void setDpi(long dpi) {
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

    public int getServiciosRealizados() {
        return serviciosRealizados;
    }

    public void setServiciosRealizados(int serviciosRealizados) {
        this.serviciosRealizados = serviciosRealizados;
    }

}
