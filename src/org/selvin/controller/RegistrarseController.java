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
public class RegistrarseController {

    public Vector<ClienteModel> clientes = new Vector<ClienteModel>();
    public Vector<EmpleadoModel> empleados = new Vector<EmpleadoModel>();

    public RegistrarseController() {
        addAdmin();
    }

    private void addAdmin() {
        EmpleadoModel empleado = new EmpleadoModel("Amdin", "Administrador");
        empleado.setUsuario("admin");
        empleado.setContrasena("admin");
        empleados.add(empleado);
    }

    public boolean addClientes(String usuario, String contrasena, String dpi, String nombre) {

        for (ClienteModel c : clientes) {
            if (c.getUsuario().equals(usuario) || c.getDpi().equals(dpi)) {
                return false;
            }
        }

        ClienteModel cliente = new ClienteModel(dpi, nombre, "normal");
        cliente.setUsuario(usuario);
        cliente.setContrasena(contrasena);
        clientes.add(cliente);
        return true;
    }

    public Vector<EmpleadoModel> getEmpleados() {
        return empleados;
    }

    public Vector<ClienteModel> getClientes() {
        return clientes;
    }
}
