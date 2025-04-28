/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Selvi
 */
public class OrdenTrabajoModel implements Serializable{

    private int numeroOrden;
    private VehiculoModel vehiculo;
    private ClienteModel cliente;
    private ServicioModel servicio;
    private LocalDateTime fecha;
    private EmpleadoModel mecanico;
    private String estado;// espera, en servicio, listo
    private boolean procesado = false;
    private boolean pagado = false;
    private LocalDateTime fechaFinalizacion;

    public OrdenTrabajoModel() {
    }

    public OrdenTrabajoModel(int numeroOrden, VehiculoModel vehiculo, ClienteModel cliente, ServicioModel servicio, LocalDateTime fecha, EmpleadoModel mecanico) {
        this.numeroOrden = numeroOrden;
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.servicio = servicio;
        this.fecha = fecha;
        this.mecanico = mecanico;
        this.estado = (mecanico != null) ? "en servicio" : "espera";
    }

    public void finalizarOrden() {
        this.estado = "Finalizado";
        this.fechaFinalizacion = LocalDateTime.now();
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public VehiculoModel getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(VehiculoModel vehiculo) {
        this.vehiculo = vehiculo;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public ServicioModel getServicio() {
        return servicio;
    }

    public void setServicio(ServicioModel servicio) {
        this.servicio = servicio;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EmpleadoModel getMecanico() {
        return mecanico;
    }

    public void setMecanico(EmpleadoModel mecanico) {
        this.mecanico = mecanico;
        if (mecanico != null) {
            this.estado = "en servicio";
        } else {
            this.estado = "espera";
        }
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isProcesado() {
        return procesado;
    }

    public void setProcesado(boolean procesado) {
        this.procesado = procesado;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

}
