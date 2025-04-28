/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.selvin.main.Main;
import org.selvin.model.ClienteModel;
import org.selvin.model.EmpleadoModel;

/**
 *
 * @author Selvi
 */
public class RegistrarClienteController {

    private static final String ARCHIVO_CLIENTES = Main.CARPETA_DAT + "clientes.dat";
    private static final String ARCHIVO_EMPLEADOS = Main.CARPETA_DAT + "empleados.dat";

    public ClienteModel[] clientes = new ClienteModel[25];
    public EmpleadoModel[] empleados = new EmpleadoModel[5];
    private int countCliente = 0;
    private int countEmpleado = 0;

    public RegistrarClienteController() {
        cargarDatos();
        if (countEmpleado == 0) {
            addAdmin();
        }
    }

    private void addAdmin() {
        if (countEmpleado < empleados.length) {
            EmpleadoModel admin = new EmpleadoModel("Amdin", "Administrador", "Libre");
            admin.setUsuario("admin");
            admin.setContrasena("admin");
            empleados[countEmpleado++] = admin;

            EmpleadoModel mecanico = new EmpleadoModel("Carlos", "Mecanico", "Libre");
            mecanico.setUsuario("mecanico");
            mecanico.setContrasena("mecanico");
            empleados[countEmpleado++] = mecanico;

            guardarEmpleados();
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
        guardarClientes();
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

    private void guardarClientes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CLIENTES))) {
            oos.writeInt(countCliente);
            for (int i = 0; i < countCliente; i++) {
                oos.writeObject(clientes[i]);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar clientes: " + e.getMessage());
        }
    }

    private void guardarEmpleados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_EMPLEADOS))) {
            oos.writeInt(countEmpleado);
            for (int i = 0; i < countEmpleado; i++) {
                oos.writeObject(empleados[i]);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar empleados: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        cargarClientes();
        cargarEmpleados();
    }

    private void cargarClientes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_CLIENTES))) {
            countCliente = ois.readInt();
            for (int i = 0; i < countCliente; i++) {
                clientes[i] = (ClienteModel) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar clientes (se iniciará con lista vacía): " + e.getMessage());
        }
    }

    private void cargarEmpleados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_EMPLEADOS))) {
            countEmpleado = ois.readInt();
            for (int i = 0; i < countEmpleado; i++) {
                empleados[i] = (EmpleadoModel) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar empleados (se iniciará con lista vacía): " + e.getMessage());
        }
    }
}
