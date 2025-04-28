/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.selvin.model.ActualizacionProgreso;
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
    public OrdenTrabajoModel[] vehiculosEnCola = new OrdenTrabajoModel[100];
    private OrdenTrabajoModel[] vehiculosEnServicio = new OrdenTrabajoModel[100];
    private OrdenTrabajoModel[] vehiculosListos = new OrdenTrabajoModel[100];

    private int countOrdenes = 0;
    public int countEnCola = 0;
    private int countEnServicio = 0;
    private int countListos = 0;
    private int idOrden = 1;

    private ServiciosController serviciosController;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ActualizacionProgreso actualizacionProgreso;

    private boolean mostrandoProgresoCola = false;

    public final ReentrantLock lockProgresoCola = new ReentrantLock();

    public void setActualizacionProgreso(ActualizacionProgreso actualizacionProgreso) {
        this.actualizacionProgreso = actualizacionProgreso;
    }

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

    public synchronized void crearOrden(VehiculoModel vehiculo, ServicioModel servicio) {
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

    public synchronized void asignarMecanico(OrdenTrabajoModel newOrden) {
        EmpleadoModel mecanico = mecanicoDisponible();
        if (mecanico != null) {
            newOrden.setMecanico(mecanico);
            newOrden.setEstado("en servicio");
            ordenes[countOrdenes++] = newOrden;
            executorService.execute(new OrdenTrabajoThread(newOrden, this, serviciosController, actualizacionProgreso));
            mostrarSiguienteEnCola();
        } else {
            agregarAColaEspera(newOrden);
            if (countEnCola == 1) {
                executorService.execute(new OrdenTrabajoThread(newOrden, this, serviciosController, actualizacionProgreso));
            } else {
                System.out.println("Vehículo en cola de espera: " + newOrden.getVehiculo().getPlaca());
            }
        }
    }

    public synchronized EmpleadoModel mecanicoDisponible() {
        for (EmpleadoModel mecanico : mecanicos) {
            if (mecanico != null && mecanico.getTipo().equalsIgnoreCase("Mecanico") && mecanico.getEstado().equalsIgnoreCase("Libre")) {
                mecanico.setEstado("Ocupado");
                return mecanico;
            }
        }
        return null;
    }

    public synchronized void liberarMecanico(EmpleadoModel mecanico) {
        if (mecanico != null) {
            mecanico.setEstado("Libre");
            asignarSiguienteDeCola();
            mostrarSiguienteEnCola();
        }
    }

    public synchronized void moverACarrosListos(OrdenTrabajoModel orden) {
        orden.finalizarOrden();
        orden.setProcesado(true);

        for (int i = 0; i < countEnServicio; i++) {
            if (vehiculosEnServicio[i] == orden) {
                System.arraycopy(vehiculosEnServicio, i + 1, vehiculosEnServicio, i, countEnServicio - i - 1);
                countEnServicio--;
                vehiculosEnServicio[countEnServicio] = null;
                break;
            }
        }

        vehiculosListos[countListos++] = orden;

        if (actualizacionProgreso != null) {
            actualizacionProgreso.updateEnServicio("", 0);
            actualizacionProgreso.updateListo(orden.getVehiculo().getPlaca(), 0);
        }
    }

    public boolean registrarPago(int numeroOrden) {
        for (int i = 0; i < countListos; i++) {
            if (vehiculosListos[i].getNumeroOrden() == numeroOrden) {
                vehiculosListos[i].setPagado(true);
                System.arraycopy(vehiculosListos, i + 1, vehiculosListos, i, countListos - i - 1);
                countListos--;
                vehiculosListos[countListos] = null;
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
        OrdenTrabajoModel[] pendientes = new OrdenTrabajoModel[countListos];
        int count = 0;

        for (int i = 0; i < countListos; i++) {
            if (!vehiculosListos[i].isPagado()) {
                pendientes[count++] = vehiculosListos[i];
            }
        }

        OrdenTrabajoModel[] resultado = new OrdenTrabajoModel[count];
        System.arraycopy(pendientes, 0, resultado, 0, count);
        return resultado;
    }

    private synchronized void agregarAColaEspera(OrdenTrabajoModel newOrden) {
        if (countEnCola < vehiculosEnCola.length) {
            if (newOrden.getCliente().getTipoCliente().equalsIgnoreCase("ORO")) {
                System.arraycopy(vehiculosEnCola, 0, vehiculosEnCola, 1, countEnCola);
                vehiculosEnCola[0] = newOrden;
            } else {
                vehiculosEnCola[countEnCola] = newOrden;
            }
            countEnCola++;
            newOrden.setEstado("espera");
        }
    }

    private synchronized void asignarSiguienteDeCola() {
        if (countEnCola > 0) {
            OrdenTrabajoModel siguiente = vehiculosEnCola[0];
            System.arraycopy(vehiculosEnCola, 1, vehiculosEnCola, 0, countEnCola - 1);
            vehiculosEnCola[--countEnCola] = null;
            asignarMecanico(siguiente);
        }
    }

    private synchronized void removerDeCola(OrdenTrabajoModel orden) {
        for (int i = 0; i < countEnCola; i++) {
            if (vehiculosEnCola[i] == orden) {
                System.arraycopy(vehiculosEnCola, i + 1, vehiculosEnCola, i, countEnCola - i - 1);
                countEnCola--;
                vehiculosEnCola[countEnCola] = null;
                break;
            }
        }
    }

    private synchronized void mostrarSiguienteEnCola() {
        if (countEnCola > 0 && !mostrandoProgresoCola) {
            mostrandoProgresoCola = true;
            OrdenTrabajoModel siguiente = vehiculosEnCola[0];
            executorService.execute(new OrdenTrabajoThread(siguiente, this, serviciosController, actualizacionProgreso) {
                @Override
                public void run() {
                    try {
                        super.run();
                    } finally {
                        synchronized (OrdenTrabajoController.this) {
                            mostrandoProgresoCola = false;
                            mostrarSiguienteEnCola();
                        }
                    }
                }
            });
        } else if (countEnCola == 0 && actualizacionProgreso != null) {
            actualizacionProgreso.updateCola("", 0);
        }
    }
}
