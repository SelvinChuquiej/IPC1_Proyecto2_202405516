/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import org.selvin.model.ClienteModel;
import org.selvin.model.EmpleadoModel;

/**
 *
 * @author Selvi
 */
public class RegistrarClienteController {

    public ClienteModel[] clientes = new ClienteModel[25];
    public EmpleadoModel[] empleados = new EmpleadoModel[5];
    private int countCliente = 0;
    private int countEmpleado = 0;

    public RegistrarClienteController() {
        addAdmin();
    }

    private void addAdmin() {
        if (countEmpleado < empleados.length) {
            EmpleadoModel admin = new EmpleadoModel("Amdin", "Administrador", "Libre");
            admin.setUsuario("admin");
            admin.setContrasena("admin");
            empleados[countEmpleado++] = admin;

            String[] nombresMecanicos = {"Carlos", "Luisa"};
            for (int i = 0; i < 2; i++) {
                EmpleadoModel mecanico = new EmpleadoModel(nombresMecanicos[i], "Mecanico", "Libre");
                mecanico.setUsuario("mecanico" + i);
                mecanico.setContrasena("mecanico" + i);
                empleados[countEmpleado++] = mecanico;
            }
        }
    }

    public boolean addClientes(String usuario, String contrasena, long dpi, String nombre) {
        if (countCliente >= clientes.length) {
            return false;
        }

        for (int i = 0; i < countCliente; i++) {
            if (clientes[i].getUsuario().equals(usuario) || clientes[i].getDpi() == dpi) {
                return false;
            }
        }

        ClienteModel cliente = new ClienteModel(dpi, nombre, "normal");
        cliente.setUsuario(usuario);
        cliente.setContrasena(contrasena);
        clientes[countCliente++] = cliente;
        ordenamientoCliente();
        return true;
    }

    public void ordenamientoCliente() {
        boolean flag = true;
        ClienteModel temp;
        while (flag) {
            flag = false;
            for (int i = 0; i < countCliente - 1; i++) {
                if (clientes[i].getDpi() > clientes[i + 1].getDpi()) {
                    temp = clientes[i];
                    clientes[i] = clientes[i + 1];
                    clientes[i + 1] = temp;
                    flag = true;
                }
            }
        }
    }
}
