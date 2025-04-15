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
public class ClienteModel implements Serializable  {
 
    private String dpi;
    private String nombreCompleto;
    private String usuario;
    private String contraseña;
    private String tipoCliente; // normal u oro
    private VehiculoModel[] vehiculos;

    public ClienteModel() {
    }

    public ClienteModel(String dpi, String nombreCompleto, String usuario, String contraseña, String tipoCliente, VehiculoModel[] vehiculos) {
        this.dpi = dpi;
        this.nombreCompleto = nombreCompleto;
        this.usuario = usuario;
        this.contraseña = contraseña;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
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
