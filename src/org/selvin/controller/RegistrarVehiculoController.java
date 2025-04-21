/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import org.selvin.view.RegistrarVehiculoView;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.selvin.model.ClienteModel;
import org.selvin.model.VehiculoModel;

/**
 *
 * @author Selvi
 */
public class RegistrarVehiculoController {

    public VehiculoModel[] vehiculos = new VehiculoModel[25];
    private RegistrarVehiculoView registrarAutoView;
    private LoginController loginController;
    private int countVehiculo = 0;
    private String IMAGES_DIR = "img_vehiculos/";
    private DefaultTableModel dtm;

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
        ClienteModel cliente = (ClienteModel) loginController.getUsuarioLogueado();
        try {
            String rutaImg = copiarImg(rutaImagen, placa);
            VehiculoModel vehiculo = new VehiculoModel(placa, Marca, modelo, rutaImagen, cliente.getDpi());
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

    public class ImageRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String) {
                String rutaImagen = (String) value;
                if (rutaImagen != null && !rutaImagen.isEmpty()) {
                    try {
                        ImageIcon icon = new ImageIcon(rutaImagen);
                        Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_FAST);
                        setIcon(new ImageIcon(img));
                    } catch (Exception e) {
                        setText("Error al cargar");
                    }
                } else {
                    setText("Sin imagen");
                }
            }
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
    }

    public void cargarVehiculos(JTable tblVehiculos) {
        dtm = (DefaultTableModel) tblVehiculos.getModel();
        dtm.setRowCount(0);
        tblVehiculos.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());
        ClienteModel cliente = (ClienteModel) loginController.getUsuarioLogueado();
        for (VehiculoModel v : vehiculos) {
            if (v != null && cliente.getDpi().equals(v.getDpiLog())) {
                Object[] datos = {
                    v.getPlaca(),
                    v.getMarca(),
                    v.getModelo(),
                    v.getRutaImagen()
                };
                dtm.addRow(datos);
            }
        }
    }

    public void ordenarPorPlacaAsc() {
        int salto = countVehiculo / 2;
        while (salto > 0) {
            for (int i = salto; i < countVehiculo; i++) {
                VehiculoModel temp = vehiculos[i];
                int j;
                for (j = i; j >= salto; j -= salto) {
                    String placaActual = vehiculos[j - salto].getPlaca();
                    String placaTemp = temp.getPlaca();
                    if (placaActual.compareTo(placaTemp) > 0) {
                        vehiculos[j] = vehiculos[j - salto];
                    } else {
                        break;
                    }
                }
                vehiculos[j] = temp;
            }
            salto /= 2;
        }
    }

    public void ordenarPorPlacaDesc() {
        int salto = countVehiculo / 2;
        while (salto > 0) {
            for (int i = salto; i < countVehiculo; i++) {
                VehiculoModel temp = vehiculos[i];
                int j;
                for (j = i; j >= salto; j -= salto) {
                    String placaActual = vehiculos[j - salto].getPlaca();
                    String placaTemp = temp.getPlaca();
                    if (placaActual.compareTo(placaTemp) < 0) {
                        vehiculos[j] = vehiculos[j - salto];
                    } else {
                        break;
                    }
                }
                vehiculos[j] = temp;
            }
            salto /= 2;
        }
    }

    public boolean placaValida(String placa) {
        if (placa == null || placa.length() != 6) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            if (!Character.isDigit(placa.charAt(i))) {
                return false;
            }
        }
        for (int i = 3; i < 6; i++) {
            if (!Character.isLetter(placa.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
