/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.selvin.controller.OrdenTrabajoController;
import org.selvin.controller.ServiciosController;

/**
 *
 * @author Selvi
 */
public class OrdenTrabajoThread extends Thread {

    private OrdenTrabajoModel ordenTrabajoModel;
    private OrdenTrabajoController ordenTrabajoController;
    private ServiciosController serviciosController;
    private ActualizacionProgreso actualizacionProgreso;
    private boolean clienteAceptoServicio = false;
    private Random random = new Random();

    public OrdenTrabajoThread(OrdenTrabajoModel ordenTrabajoModel, OrdenTrabajoController ordenTrabajoController, ServiciosController serviciosController, ActualizacionProgreso actualizacionProgreso) {
        this.ordenTrabajoModel = ordenTrabajoModel;
        this.ordenTrabajoController = ordenTrabajoController;
        this.serviciosController = serviciosController;
        this.actualizacionProgreso = actualizacionProgreso;
    }

    @Override
    public void run() {
        try {
            if (ordenTrabajoModel.isProcesado()) {
                return;
            }
            ordenTrabajoModel.setProcesado(true);

            if ("espera".equals(ordenTrabajoModel.getEstado())) {
                mostrarEnCola();
                while (ordenTrabajoModel.getMecanico() == null && !Thread.currentThread().isInterrupted()) {
                    Thread.sleep(100);
                }
                if (ordenTrabajoModel.getMecanico() != null) {
                    if (ordenTrabajoModel.getServicio().getNombre().equalsIgnoreCase("Diagnóstico")) {
                        realizarDiagnostico();
                    } else {
                        procesarServicioNormal();
                    }
                }
            } else if ("en servicio".equals(ordenTrabajoModel.getEstado())) {
                if (ordenTrabajoModel.getServicio().getNombre().equalsIgnoreCase("Diagnóstico")) {
                    realizarDiagnostico();
                } else {
                    procesarServicioNormal();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException ex) {
            Logger.getLogger(OrdenTrabajoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mostrarEnCola() throws InterruptedException {
        String placa = ordenTrabajoModel.getVehiculo().getPlaca();
        ordenTrabajoController.lockProgresoCola.lock();
        try {
            for (int i = 0; i <= 100 && !Thread.currentThread().isInterrupted(); i++) {
                if (actualizacionProgreso != null) {
                    actualizacionProgreso.updateCola(placa, i);
                }
                Thread.sleep(110);
            }
        } finally {
            ordenTrabajoController.lockProgresoCola.unlock();
        }
    }

    private void procesarServicioNormal() throws InterruptedException {
        String placa = ordenTrabajoModel.getVehiculo().getPlaca();
        for (int i = 0; i <= 100 && !Thread.currentThread().isInterrupted(); i++) {
            if (actualizacionProgreso != null) {
                actualizacionProgreso.updateEnServicio(placa, i);
            }
            Thread.sleep(50);
        }

        for (int i = 0; i <= 100 && !Thread.currentThread().isInterrupted(); i++) {
            if (actualizacionProgreso != null) {
                actualizacionProgreso.updateListo(placa, i);
            }
            Thread.sleep(20);
        }

        ordenTrabajoController.moverACarrosListos(ordenTrabajoModel);
        ordenTrabajoController.liberarMecanico(ordenTrabajoModel.getMecanico());
    }

    private void realizarDiagnostico() throws InterruptedException, InvocationTargetException {
        Thread.sleep(2000);

        ServicioModel[] serviciosDisponibles = serviciosController.getServicios();
        ServicioModel[] serviciosCompatibles = filtrarServiciosCompatibles(serviciosDisponibles);

        if (serviciosCompatibles.length > 0) {
            ServicioModel servicioDiagnosticado = seleccionarServicioAleatorio(serviciosCompatibles);

            System.out.println("Diagnóstico completado. Servicio recomendado: " + servicioDiagnosticado.getNombre());

            if (SwingUtilities.isEventDispatchThread()) {
                int option = JOptionPane.showConfirmDialog(null, "Diagnóstico completo. Servicio recomendado:\n" + "Nombre: " + servicioDiagnosticado.getNombre() + "\n" + "¿Desea autorizar este servicio?", "Autorización de Servicio", JOptionPane.YES_NO_OPTION);
                clienteAceptoServicio = (option == JOptionPane.YES_OPTION);
            } else {
                SwingUtilities.invokeAndWait(() -> {
                    int option = JOptionPane.showConfirmDialog(null, "Diagnóstico completo. Servicio recomendado:\n" + "Nombre: " + servicioDiagnosticado.getNombre() + "\n" + "¿Desea autorizar este servicio?", "Autorización de Servicio", JOptionPane.YES_NO_OPTION);
                    clienteAceptoServicio = (option == JOptionPane.YES_OPTION);
                });
            }

            if (clienteAceptoServicio) {
                System.out.println("Cliente ha aceptado el servicio recomendado.");
                ordenTrabajoModel.setServicio(servicioDiagnosticado);
                procesarServicioNormal();
            } else {
                System.out.println("Cliente ha rechazado el servicio recomendado.");
                finalizarOrden();
            }
        } else {
            System.out.println("No se encontraron servicios compatibles para este vehículo.");
            finalizarOrden();

        }
    }

    private void finalizarOrden() {
        ordenTrabajoModel.setEstado("Finalizado");
        ordenTrabajoModel.setProcesado(true);
        ordenTrabajoController.moverACarrosListos(ordenTrabajoModel);
    }

    private ServicioModel[] filtrarServiciosCompatibles(ServicioModel[] servicios) {
        return java.util.Arrays.stream(servicios).filter(s -> s != null
                && !s.getNombre().equalsIgnoreCase("Diagnóstico")
                && s.getMarca().equalsIgnoreCase(ordenTrabajoModel.getVehiculo().getMarca())
                && s.getModelo().equalsIgnoreCase(ordenTrabajoModel.getVehiculo().getModelo()))
                .toArray(ServicioModel[]::new);
    }

    private ServicioModel seleccionarServicioAleatorio(ServicioModel[] servicios) {
        return servicios[random.nextInt(servicios.length)];
    }
}
