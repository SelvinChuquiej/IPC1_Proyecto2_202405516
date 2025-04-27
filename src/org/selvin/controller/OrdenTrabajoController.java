/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.selvin.model.ClienteModel;
import org.selvin.model.EmpleadoModel;
import org.selvin.model.OrdenTrabajoModel;
import org.selvin.model.OrdenTrabajoThread;
import org.selvin.model.ServicioModel;
import org.selvin.model.VehiculoModel;

/**
 *
 * @author Selvi
 */
public class OrdenTrabajoController {

    private DefaultTableModel dtm;
    private EmpleadoModel[] mecanicos;
    private OrdenTrabajoModel[] ordenes = new OrdenTrabajoModel[100];
    private OrdenTrabajoModel[] colaEspera = new OrdenTrabajoModel[11];
    private OrdenTrabajoModel[] carrosListos = new OrdenTrabajoModel[100];
    private int countOrdenes = 0;
    private int countColaEspera = 0;
    private int countCarrosListos = 0;
    private int idOrden = 1;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public OrdenTrabajoController(EmpleadoModel[] mecanicos) {
        this.mecanicos = mecanicos;
    }

    public boolean verficarMarcaModelo(VehiculoModel vehiculo, ServicioModel servicio) {
        return vehiculo.getMarca().equalsIgnoreCase(servicio.getMarca()) && vehiculo.getModelo().equalsIgnoreCase(servicio.getModelo());
    }

    public void mostrarInfo(JTable tblVehiculoServicio, VehiculoModel vehiculo, ServicioModel servicio) {
        if (verficarMarcaModelo(vehiculo, servicio)) {
            dtm = (DefaultTableModel) tblVehiculoServicio.getModel();
            Object[] datos = {
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                servicio.getNombre()
            };
            dtm.addRow(datos);
        }
    }

    public void crearOrden(VehiculoModel vehiculo, ServicioModel servicio) {
        if (verficarMarcaModelo(vehiculo, servicio)) {
            ClienteModel cliente = (ClienteModel) LoginController.getUsuarioLogueado();
            OrdenTrabajoModel newOrden = new OrdenTrabajoModel(idOrden++, vehiculo, cliente, servicio, LocalDateTime.now(), null);

            cliente.setServiciosRealizados(cliente.getServiciosRealizados() + 1);
            if (cliente.getServiciosRealizados() >= 4 && !cliente.getTipoCliente().equalsIgnoreCase("ORO")) {
                cliente.setTipoCliente("ORO");
                System.out.println("Cliente " + cliente.getNombreCompleto() + " ahora es tipo ORO.");
            }

            asignarMecanico(newOrden);
        }
    }

    public void asignarMecanico(OrdenTrabajoModel newOrden) {
        EmpleadoModel mecanico = mecanicoDisponible();
        if (mecanico != null) {
            newOrden.setMecanico(mecanico);
            mecanico.setEstado("Ocupado");

            if (countOrdenes < ordenes.length) {
                ordenes[countOrdenes] = newOrden;
                countOrdenes++;
            } else {
                System.out.println("No hay espacio de ordenes activas");
            }
            new OrdenTrabajoThread(newOrden, this).start();
        } else {
            agregarAColaEspera(newOrden, newOrden.getCliente());
        }
    }

    public EmpleadoModel mecanicoDisponible() {
        for (EmpleadoModel mecanico : mecanicos) {
            if (mecanico != null && mecanico.getTipo().equalsIgnoreCase("Mecanico")
                    && mecanico.getEstado().equalsIgnoreCase("Libre")) {
                mecanico.setEstado("Ocupado");
                return mecanico;
            }
        }
        return null;
    }

    private void agregarAColaEspera(OrdenTrabajoModel newOrden, ClienteModel cliente) {
        if (countColaEspera < colaEspera.length) {
            if (cliente.getTipoCliente().equalsIgnoreCase("ORO")) {
                for (int i = countColaEspera; i > 0; i--) {
                    colaEspera[i] = colaEspera[i - 1];
                }
                colaEspera[0] = newOrden;
            } else {
                colaEspera[countColaEspera] = newOrden;
            }
            countColaEspera++;
            System.out.println("Orden agregada a la cola de espera (" + cliente.getTipoCliente() + ").");
            new Thread(() -> {
                try {
                    System.out.println("Vehículo en cola de espera: " + newOrden.getVehiculo().getPlaca());
                    Thread.sleep(11000); // 11 segundos en cola
                    System.out.println("Vehículo saliendo de cola: " + newOrden.getVehiculo().getPlaca());
                    asignarMecanico(newOrden);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            System.out.println("La cola de espera ha alcanzado su capacidad máxima");
        }
    }

    public void liberarMecanico(EmpleadoModel mecanico) {
        if (mecanico != null) {
            mecanico.setEstado("Libre");
            asignarSiguienteDeCola();
        }
    }

    public void moverACarrosListos(OrdenTrabajoModel orden) {
        if (countCarrosListos < carrosListos.length) {
            carrosListos[countCarrosListos++] = orden;
            System.out.println("Vehículo listo para entrega: " + orden.getVehiculo().getPlaca());
        } else {
            System.out.println("No hay espacio para más carros listos.");
        }
    }

    private void asignarSiguienteDeCola() {
        if (countColaEspera > 0) {
            OrdenTrabajoModel siguiente = colaEspera[0];

            System.arraycopy(colaEspera, 1, colaEspera, 0, countColaEspera - 1);
            colaEspera[--countColaEspera] = null;

            asignarMecanico(siguiente);
        }
    }
}
