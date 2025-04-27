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
public class ServicioModel {

    private int id;
    private String nombre;
    private String marca;
    private String modelo;
    private RepuestoModel[] idsRepuestos;
    private double precioManoObra;
    private double precioTotal;

    public ServicioModel() {
    }

    public ServicioModel(int id, String nombre, String marca, String modelo, RepuestoModel[] idsRepuestos, double precioManoObra, double precioTotal) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.idsRepuestos = idsRepuestos;
        this.precioManoObra = precioManoObra;
        this.precioTotal = precioTotal;
    }

    public RepuestoModel[] obtenerID() {
        return idsRepuestos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public RepuestoModel[] getIdsRepuestos() {
        return idsRepuestos;
    }

    public void setIdsRepuestos(RepuestoModel[] idsRepuestos) {
        this.idsRepuestos = idsRepuestos;
    }

    public double getPrecioManoObra() {
        return precioManoObra;
    }

    public void setPrecioManoObra(double precioManoObra) {
        this.precioManoObra = precioManoObra;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    @Override
    public String toString() {
        return nombre + " | " + marca + " | " + modelo;
    }

}
