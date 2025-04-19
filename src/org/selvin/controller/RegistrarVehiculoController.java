/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.selvin.view.RegistrarVehiculoView;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.selvin.model.VehiculoModel;

/**
 *
 * @author Selvi
 */
public class RegistrarVehiculoController {

    public VehiculoModel[] vehiculos = new VehiculoModel[25];
    private RegistrarVehiculoView registrarAutoView;
    private int countVehiculo = 0;
    private String IMAGES_DIR = "img_vehiculos/";

    public RegistrarVehiculoController() {
        File dir = new File(IMAGES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public boolean addVehiculos(String placa, String Marca, String modelo, String rutaImagen) {
        if (countVehiculo >= vehiculos.length) {
            return false;
        }
        try {
            String rutaImg = copiarImg(rutaImagen, placa);
            VehiculoModel vehiculo = new VehiculoModel(placa, Marca, modelo, rutaImagen);
            vehiculos[countVehiculo++] = vehiculo;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String copiarImg(String rutaImgOriginal, String placa) throws IOException {
        File imgOriginal = new File(rutaImgOriginal);

        String nombreOriginal = imgOriginal.getName();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));

        String nombreImg = "vehiculo_" + placa + extension;
        Path destino = Path.of(IMAGES_DIR + nombreImg);
        Files.copy(imgOriginal.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return destino.toString();
    }

    public void seleccionarImagen(JTextField txtRuta) {
        JFileChooser dlg = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos JPG, PNG (*.jpg, *.png)", "jpg", "png");
        dlg.setFileFilter(filter);
        int option = dlg.showOpenDialog(registrarAutoView);
        if (option == JFileChooser.APPROVE_OPTION) {
            File archivo = dlg.getSelectedFile();
            txtRuta.setText(archivo.getPath());
        }
    }
}
