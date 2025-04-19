/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.util.Vector;
import org.selvin.model.ClienteModel;
import org.selvin.model.EmpleadoModel;

/**
 *
 * @author Selvi
 */
public class RegistrarClienteController {

    public ClienteModel[] clientes = new ClienteModel[25];
    public EmpleadoModel[] empleados = new EmpleadoModel[25];
    private int countCliente = 0;
    private int countEmpleado = 0;

    public RegistrarClienteController() {
        addAdmin();
    }

    private void addAdmin() {
        if (countEmpleado < empleados.length) {
            EmpleadoModel empleado = new EmpleadoModel("Amdin", "Administrador");
            empleado.setUsuario("admin");
            empleado.setContrasena("admin");
            empleados[countEmpleado++] = empleado;
        }
    }

    public boolean addClientes(String usuario, String contrasena, String dpi, String nombre) {
        if (countCliente >= clientes.length) {
            return false;
        }

        for (int i = 0; i < countCliente; i++) {
            if (clientes[i].getUsuario().equals(usuario) || clientes[i].getDpi().equals(dpi)) {
                return false;
            }
        }

        ClienteModel cliente = new ClienteModel(dpi, nombre, "normal");
        cliente.setUsuario(usuario);
        cliente.setContrasena(contrasena);
        clientes[countCliente++] = cliente;
        return true;
    }

    public EmpleadoModel[] getEmpleados() {
        EmpleadoModel[] result = new EmpleadoModel[countEmpleado];
        System.arraycopy(empleados, 0, result, 0, countEmpleado);
        return result;
    }

    public ClienteModel[] getClientes() {
        ClienteModel[] result = new ClienteModel[countCliente];
        System.arraycopy(clientes, 0, result, 0, countCliente);
        return result;
    }
}
