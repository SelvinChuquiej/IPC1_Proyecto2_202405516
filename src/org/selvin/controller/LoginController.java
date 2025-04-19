/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import org.selvin.controller.RegistrarClienteController;
import org.selvin.model.ClienteModel;
import org.selvin.model.EmpleadoModel;

/**
 *
 * @author Selvi
 */
public class LoginController {

    private RegistrarClienteController registrarseClienteController;
    private static Object usuarioLogueado;

    public LoginController(RegistrarClienteController registrarseController) {
        this.registrarseClienteController = registrarseController;
    }

    public Object login(String usuario, String contrasena) {
        for (EmpleadoModel empleado : registrarseClienteController.getEmpleados()) {
            if (empleado.getUsuario().equals(usuario) && empleado.getContrasena().equals(contrasena)) {
                usuarioLogueado = empleado;
                return empleado;
            }
        }

        for (ClienteModel cliente : registrarseClienteController.getClientes()) {
            if (cliente.getUsuario().equals(usuario) && cliente.getContrasena().equals(contrasena)) {
                usuarioLogueado = cliente;
                return cliente;
            }
        }

        return null;
    }

    public static Object getUsuarioLogueado() {
        return usuarioLogueado;
    }

}
