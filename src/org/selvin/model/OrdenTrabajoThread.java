/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.model;

import org.selvin.controller.OrdenTrabajoController;

/**
 *
 * @author Selvi
 */
public class OrdenTrabajoThread extends Thread {

    private OrdenTrabajoModel ordenTrabajoModel;
    private OrdenTrabajoController ordenTrabajoController;

    public OrdenTrabajoThread(OrdenTrabajoModel ordenTrabajoModel, OrdenTrabajoController ordenTrabajoController) {
        this.ordenTrabajoModel = ordenTrabajoModel;
        this.ordenTrabajoController = ordenTrabajoController;
    }

    @Override
    public void run() {
        try {
            System.out.println("Vehículo en servicio: " + ordenTrabajoModel.getVehiculo().getPlaca());
            Thread.sleep(5000); // Simular 5 segundos de servicio

            System.out.println("Vehículo servicio terminado: " + ordenTrabajoModel.getVehiculo().getPlaca());

            // Liberar mecánico
            EmpleadoModel mecanico = ordenTrabajoModel.getMecanico();
            ordenTrabajoController.liberarMecanico(mecanico);

            // Mover a carros listos
            ordenTrabajoController.moverACarrosListos(ordenTrabajoModel);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
