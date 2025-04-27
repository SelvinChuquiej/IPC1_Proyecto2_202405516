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
    private boolean clienteAceptoServicio = false;
    private Random random = new Random();

    public OrdenTrabajoThread(OrdenTrabajoModel ordenTrabajoModel, OrdenTrabajoController ordenTrabajoController, ServiciosController serviciosController) {
        this.ordenTrabajoModel = ordenTrabajoModel;
        this.ordenTrabajoController = ordenTrabajoController;
        this.serviciosController = serviciosController;
    }

    @Override
    public void run() {
        try {
            // Verificar si ya fue procesado
            if (ordenTrabajoModel.isProcesado()) {
                System.out.println("Orden ya procesada, ignorando: " + ordenTrabajoModel.getNumeroOrden());
                return;
            }

            ordenTrabajoModel.setProcesado(true); // Marcar como procesado inmediatamente

            if (ordenTrabajoModel.getServicio().getNombre().equalsIgnoreCase("Diagnóstico")) {
                realizarDiagnostico();
            } else {
                realizarServicioNormal();
            }

            if (!ordenTrabajoModel.getServicio().getNombre().equalsIgnoreCase("Diagnóstico") || (ordenTrabajoModel.getServicio().getNombre().equalsIgnoreCase("Diagnóstico") && !clienteAceptoServicio)) {
                System.out.println("Vehículo listo para entrega: " + ordenTrabajoModel.getVehiculo().getPlaca());
                Thread.sleep(2000);
            }

            ordenTrabajoController.moverACarrosListos(ordenTrabajoModel);
            ordenTrabajoController.liberarMecanico(ordenTrabajoModel.getMecanico());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException ex) {
            Logger.getLogger(OrdenTrabajoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void realizarDiagnostico() throws InterruptedException, InvocationTargetException {
        System.out.println("Realizando diagnóstico para vehículo: " + ordenTrabajoModel.getVehiculo().getPlaca());

        // Simular tiempo de diagnóstico
        Thread.sleep(2000);

        // Obtener servicios disponibles para esta marca y modelo
        ServicioModel[] serviciosDisponibles = serviciosController.getServicios();
        ServicioModel[] serviciosCompatibles = filtrarServiciosCompatibles(serviciosDisponibles);

        if (serviciosCompatibles.length > 0) {
            // Seleccionar un servicio aleatorio (excluyendo el diagnóstico)
            ServicioModel servicioDiagnosticado = seleccionarServicioAleatorio(serviciosCompatibles);

            System.out.println("Diagnóstico completado. Servicio recomendado: " + servicioDiagnosticado.getNombre());

            if (SwingUtilities.isEventDispatchThread()) {
                int option = JOptionPane.showConfirmDialog(null, "Diagnóstico completo. Servicio recomendado:\n" + "Nombre: " + servicioDiagnosticado.getNombre() + "\n" + "¿Desea autorizar este servicio?", "Autorización de Servicio", JOptionPane.YES_NO_OPTION);
                clienteAceptoServicio = (option == JOptionPane.YES_OPTION);
            } else {
                // Para hilos secundarios, usar invokeLater
                SwingUtilities.invokeAndWait(() -> {
                    int option = JOptionPane.showConfirmDialog(null, "Diagnóstico completo. Servicio recomendado:\n" + "Nombre: " + servicioDiagnosticado.getNombre() + "\n" + "¿Desea autorizar este servicio?", "Autorización de Servicio", JOptionPane.YES_NO_OPTION);
                    clienteAceptoServicio = (option == JOptionPane.YES_OPTION);
                });
            }

            if (clienteAceptoServicio) {
                System.out.println("Cliente ha aceptado el servicio recomendado.");
                ordenTrabajoModel.setServicio(servicioDiagnosticado);
                realizarServicioNormal();
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

    private void realizarServicioNormal() throws InterruptedException {
        System.out.println("Vehículo en servicio: " + ordenTrabajoModel.getVehiculo().getPlaca() + " - Servicio: " + ordenTrabajoModel.getServicio().getNombre());
        Thread.sleep(11000);
        System.out.println("Vehículo servicio terminado: " + ordenTrabajoModel.getVehiculo().getPlaca());
    }
}
