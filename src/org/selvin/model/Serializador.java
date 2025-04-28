/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Selvi
 */
public class Serializador {

    public static void serializarObjeto(String archivo, Object objeto) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(archivo); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(objeto);
        }
    }

    public static Object deserializarObjeto(String archivo) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(archivo); ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return in.readObject();
        }
    }
}
