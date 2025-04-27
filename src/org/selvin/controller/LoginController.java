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

    private static Object usuarioLogueado;

    private ClienteModel[] clientesExistentes;
    private EmpleadoModel[] empleadosExistentes;

    public LoginController(ClienteModel[] clientesExistentes, EmpleadoModel[] empleadosExistentes) {
        this.clientesExistentes = clientesExistentes;
        this.empleadosExistentes = empleadosExistentes;
    }

    public Object login(String usuario, String contrasena) {
        for (EmpleadoModel empleado : empleadosExistentes) {
            if (empleado != null && empleado.getUsuario().equals(usuario) && empleado.getContrasena().equals(contrasena)) {
                usuarioLogueado = empleado;
                return empleado;
            }
        }

        for (ClienteModel cliente : clientesExistentes) {
            if (cliente != null && cliente.getUsuario().equals(usuario) && cliente.getContrasena().equals(contrasena)) {
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
