/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.selvin.model;

import java.io.Serializable;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author Selvi
 */
public class ActualizacionProgreso implements Serializable {

    private JProgressBar pbrCola;
    private JProgressBar pbrEnServicio;
    private JProgressBar pbrListo;
    private JLabel jlbPlacaCola;
    private JLabel jlbPlacaServicio;
    private JLabel jlbListo;

    public ActualizacionProgreso(JProgressBar pbrCola, JProgressBar pbrEnServicio, JProgressBar pbrListo, JLabel jlbPlacaCola, JLabel jlbPlacaServicio, JLabel jlbListo) {
        this.pbrCola = pbrCola;
        this.pbrEnServicio = pbrEnServicio;
        this.pbrListo = pbrListo;
        this.jlbPlacaCola = jlbPlacaCola;
        this.jlbPlacaServicio = jlbPlacaServicio;
        this.jlbListo = jlbListo;
        configurarBarras();
    }

    private void configurarBarras() {
        configurarBarra(pbrCola);
        configurarBarra(pbrEnServicio);
        configurarBarra(pbrListo);
    }

    private void configurarBarra(JProgressBar barra) {
        if (barra != null) {
            barra.setStringPainted(true);
            barra.setMaximum(100);
            barra.setValue(0);
        }
    }

    public void updateCola(String placa, int progress) {
        SwingUtilities.invokeLater(() -> {
            if (pbrCola != null) {
                pbrCola.setValue(progress);
            }
            if (jlbPlacaCola != null) {
                jlbPlacaCola.setText(placa.isEmpty() ? "---" : placa);
            }
        });
    }

    public void updateEnServicio(String placa, int progress) {
        SwingUtilities.invokeLater(() -> {
            if (pbrEnServicio != null) {
                pbrEnServicio.setValue(progress);
            }
            if (jlbPlacaServicio != null) {
                jlbPlacaServicio.setText(placa.isEmpty() ? "---" : placa);
            }
        });
    }

    public void updateListo(String placa, int progress) {
        SwingUtilities.invokeLater(() -> {
            if (pbrListo != null) {
                pbrListo.setValue(progress);
            }
            if (jlbListo != null) {
                jlbListo.setText(placa.isEmpty() ? "---" : placa);
            }
        });
    }
}
