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
public class VehiculoModel {

    private String placa;
    private String marca;
    private String modelo;
    private String rutaImagen;
    private long dpiLog;

    public VehiculoModel() {
    }

    public VehiculoModel(String placa, String marca, String modelo, String rutaImagen, long dpiLog) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.rutaImagen = rutaImagen;
        this.dpiLog = dpiLog;
    }

    public long getDpiLog() {
        return dpiLog;
    }

    public void setDpiLog(long dpiLog) {
        this.dpiLog = dpiLog;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    @Override
    public String toString() {
        return placa + " | " + marca + " | " + modelo;
    }

}
