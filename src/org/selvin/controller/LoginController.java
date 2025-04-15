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
public class LoginController {

    private RegistrarseController registrarseController;

    public LoginController(RegistrarseController registrarseController) {
        this.registrarseController = registrarseController;
    }

    public boolean login(String usuario, String contrasena) {
        for (EmpleadoModel empleado : registrarseController.getEmpleados()) {
            if (empleado.getUsuario().equals(usuario) && empleado.getContrasena().equals(contrasena)) {
                return true;
            }
        }

        for (ClienteModel cliente : registrarseController.getClientes()) {
            if (cliente.getUsuario().equals(usuario) && cliente.getContrasena().equals(contrasena)) {
                return true;
            }
        }

        return false;
    }
}
