/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.controller;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import org.selvin.main.Main;
import org.selvin.model.ClienteModel;
import org.selvin.model.VehiculoModel;

/**
 *
 * @author Selvi
 */
public class VehiculoController {

    private static final String ARCHIVO_VEHICULOS = Main.CARPETA_DAT + "vehiculos.dat";

    public VehiculoModel[] vehiculos = new VehiculoModel[25];
    private RegistrarVehiculoView registrarAutoView;
    private LoginController loginController;
    private int countVehiculo = 0;
    private String IMAGES_DIR = "img_vehiculos/";
    private DefaultTableModel dtm;
    private ClienteAutomovilController clienteAutomovilController;

    public VehiculoController() {
        File dir = new File(IMAGES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        cargarVehiculos();
    }

    public boolean addVehiculos(String placa, String Marca, String modelo, String rutaImagen, long dpi) {
        if (countVehiculo >= vehiculos.length) {
            return false;
        }
        for (int i = 0; i < countVehiculo; i++) {
            if (vehiculos[i] != null && vehiculos[i].getPlaca().equals(placa)) {
                return false;
            }
        }
        try {
            String rutaImg = copiarImg(rutaImagen, placa);
            VehiculoModel vehiculo = new VehiculoModel(placa, Marca, modelo, rutaImagen, dpi);
            vehiculos[countVehiculo++] = vehiculo;
            guardarVehiculos();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addVehiculos(String placa, String marca, String modelo, String rutaImagen) {
        ClienteModel cliente = (ClienteModel) loginController.getUsuarioLogueado();
        if (cliente == null) {
            return false;
        }
        return addVehiculos(placa, marca, modelo, rutaImagen, cliente.getDpi());
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

    public static class ImageRenderer extends DefaultTableCellRenderer {

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
        tblVehiculos.setRowHeight(120); 
          tblVehiculos.getColumnModel().getColumn(3).setPreferredWidth(160); 
        tblVehiculos.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());
        ClienteModel cliente = (ClienteModel) loginController.getUsuarioLogueado();
        for (VehiculoModel v : vehiculos) {
            if (v != null && cliente.getDpi() == v.getDpiLog()) {
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
            if (!Character.isLetter(placa.charAt(i))) {
                return false;
            }
        }
        for (int i = 3; i < 6; i++) {
            if (!Character.isDigit(placa.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void guardarVehiculos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_VEHICULOS))) {
            oos.writeObject(vehiculos);
            oos.writeInt(countVehiculo); // Guardamos también el contador
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarVehiculos() {
        File archivo = new File(ARCHIVO_VEHICULOS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                vehiculos = (VehiculoModel[]) ois.readObject();
                countVehiculo = ois.readInt();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
