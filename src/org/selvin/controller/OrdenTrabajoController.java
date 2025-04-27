/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
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
    private OrdenTrabajoModel[] colaEspera = new OrdenTrabajoModel[100];
    private OrdenTrabajoModel[] carrosListos = new OrdenTrabajoModel[100];
    private int countOrdenes = 0;
    private int countColaEspera = 0;
    private int countCarrosListos = 0;
    private int idOrden = 1;
    private ServiciosController serviciosController;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public OrdenTrabajoController(EmpleadoModel[] mecanicos, ServiciosController serviciosController) {
        this.mecanicos = mecanicos;
        this.serviciosController = serviciosController;
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

    public boolean verficarMarcaModelo(VehiculoModel vehiculo, ServicioModel servicio) {
        return vehiculo.getMarca().equalsIgnoreCase(servicio.getMarca()) && vehiculo.getModelo().equalsIgnoreCase(servicio.getModelo());
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
            ordenes[countOrdenes++] = newOrden;
            new OrdenTrabajoThread(newOrden, this, serviciosController).start();
        } else {
            agregarAColaEspera(newOrden);
        }
    }

    private void agregarAColaEspera(OrdenTrabajoModel newOrden) {
        if (countColaEspera < colaEspera.length) {
            if (newOrden.getCliente().getTipoCliente().equalsIgnoreCase("ORO")) {
                System.arraycopy(colaEspera, 0, colaEspera, 1, countColaEspera);
                colaEspera[0] = newOrden;
            } else {
                colaEspera[countColaEspera] = newOrden;
            }
            countColaEspera++;

            System.out.println("Vehículo en cola de espera: " + newOrden.getVehiculo().getPlaca());
        } else {
            System.out.println("La cola de espera ha alcanzado su capacidad máxima");
        }
    }

    public EmpleadoModel mecanicoDisponible() {
        for (EmpleadoModel mecanico : mecanicos) {
            if (mecanico != null && mecanico.getTipo().equalsIgnoreCase("Mecanico") && mecanico.getEstado().equalsIgnoreCase("Libre")) {
                mecanico.setEstado("Ocupado");
                return mecanico;
            }
        }
        return null;
    }

    public void liberarMecanico(EmpleadoModel mecanico) {
        if (mecanico != null) {
            mecanico.setEstado("Libre");
            asignarSiguienteDeCola();
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

    public void moverACarrosListos(OrdenTrabajoModel orden) {
        orden.finalizarOrden();
        orden.setProcesado(true);
        if (countCarrosListos < carrosListos.length) {
            carrosListos[countCarrosListos++] = orden;
            System.out.println("Vehículo " + orden.getVehiculo().getPlaca() + " movido a carros listos. Esperando pago.");
        } else {
            System.out.println("No hay espacio para más carros listos.");
        }
    }

    public boolean registrarPago(int numeroOrden) {
        for (int i = 0; i < countCarrosListos; i++) {
            if (carrosListos[i].getNumeroOrden() == numeroOrden) {
                carrosListos[i].setPagado(true);

                // Eliminar de la lista de carros listos
                System.arraycopy(carrosListos, i + 1, carrosListos, i, countCarrosListos - i - 1);
                countCarrosListos--;
                carrosListos[countCarrosListos] = null;

                System.out.println("Pago registrado para orden #" + numeroOrden + ". Vehículo puede ser retirado.");
                return true;
            }
        }
        System.out.println("No se encontró la orden #" + numeroOrden + " en carros listos.");
        return false;
    }

    public void mostrarCarrosListosPendientes(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        OrdenTrabajoModel[] pendientes = getCarrosListosPendientes();
        for (OrdenTrabajoModel orden : pendientes) {
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

    public void procesarPago(JTable tabla) {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int numeroOrden = (int) tabla.getValueAt(filaSeleccionada, 0);
            if (registrarPago(numeroOrden)) {
                mostrarCarrosListosPendientes(tabla);
                JOptionPane.showMessageDialog(null, "Pago registrado exitosamente");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un vehículo de la lista", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public OrdenTrabajoModel[] getCarrosListosPendientes() {
        OrdenTrabajoModel[] pendientes = new OrdenTrabajoModel[countCarrosListos];
        int count = 0;

        for (int i = 0; i < countCarrosListos; i++) {
            if (!carrosListos[i].isPagado()) {
                pendientes[count++] = carrosListos[i];
            }
        }

        OrdenTrabajoModel[] resultado = new OrdenTrabajoModel[count];
        System.arraycopy(pendientes, 0, resultado, 0, count);
        return resultado;
    }

    public void actualizarPlacas(JLabel lblCola, JLabel lblServicio, JLabel lblListo) {
        // Placas en cola
        StringBuilder placasCola = new StringBuilder("En cola: ");
        for (int i = 0; i < countColaEspera; i++) {
            if (colaEspera[i] != null) {
                placasCola.append(colaEspera[i].getVehiculo().getPlaca());
                if (i < countColaEspera - 1) {
                    placasCola.append(", ");
                }
            }
        }
        lblCola.setText(placasCola.length() == 8 ? "En cola: Ninguno" : placasCola.toString());

        // Placas en servicio
        StringBuilder placasServicio = new StringBuilder("En servicio: ");
        int enServicio = 0;
        for (int i = 0; i < countOrdenes; i++) {
            if (ordenes[i] != null && ordenes[i].getEstado().equals("en servicio")) {
                placasServicio.append(ordenes[i].getVehiculo().getPlaca());
                enServicio++;
                if (enServicio < countOrdenes) {
                    placasServicio.append(", ");
                }
            }
        }
        lblServicio.setText(enServicio == 0 ? "En servicio: Ninguno" : placasServicio.toString());

        // Placas listas
        StringBuilder placasListo = new StringBuilder("Listos: ");
        int listosCount = 0;
        for (int i = 0; i < countCarrosListos; i++) {
            if (carrosListos[i] != null && !carrosListos[i].isPagado()) {
                placasListo.append(carrosListos[i].getVehiculo().getPlaca());
                listosCount++;
                if (i < countCarrosListos - 1) {
                    placasListo.append(", ");
                }
            }
        }
        lblListo.setText(listosCount == 0 ? "Listos: Ninguno" : placasListo.toString());
    }
}
