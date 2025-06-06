/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal_progra;

/**
 *
 * @author cachi
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * CONTROLADOR PARA LA INTERFAZ DE LA TABLA HASH.
 * ACTÚA COMO INTERMEDIARIO ENTRE LA VISTA (VISTATABLAHASH) Y EL MODELO (TABLAHASHPERSONALIZADA).
 * MANEJA LA LÓGICA DE GENERACIÓN Y VISUALIZACIÓN DE LA TABLA HASH.
 */
public class ControladorTablaHash {
    private static final int TAMANO_TABLA_HASH = 10; // TAMAÑO PREDETERMINADO DE LA TABLA HASH.

    private VistaTablaHash vistaTablaHash; // LA VISTA ESPECÍFICA PARA LA TABLA HASH.
    private TablaHashPersonalizada tablaHashModelo; // EL MODELO DE DATOS DE LA TABLA HASH.

    /**
     * CONSTRUCTOR DEL CONTROLADOR DE LA TABLA HASH.
     * INICIALIZA LA VISTA Y EL MODELO DE LA TABLA HASH, Y CONFIGURA LOS OYENTES DE EVENTOS.
     *
     * @param parentFrame EL FRAME PADRE (VISTAPRINCIPAL) DESDE DONDE SE INVOCA ESTA VENTANA.
     * UTILIZADO PARA CENTRAR ESTA VENTANA RESPECTO AL PADRE.
     */
    public ControladorTablaHash(JFrame parentFrame) {
        this.vistaTablaHash = new VistaTablaHash();
        // INICIALIZAR EL MODELO DE LA TABLA HASH CON UN TAMAÑO PREDETERMINADO.
        this.tablaHashModelo = new TablaHashPersonalizada(TAMANO_TABLA_HASH);
        inicializarControlador();
        // CENTRAR LA VENTANA DE LA TABLA HASH CON RESPECTO AL FRAME PRINCIPAL.
        vistaTablaHash.setLocationRelativeTo(parentFrame);
    }

    /**
     * INICIALIZA LOS OYENTES DE EVENTOS PARA LOS COMPONENTES DE LA VISTA DE LA TABLA HASH.
     */
    private void inicializarControlador() {
        // OYENTE PARA EL BOTÓN "GENERAR TABLA HASH".
        vistaTablaHash.getBtnGenerar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarTablaHash();
            }
        });
    }

    /**
     * MUESTRA LA INTERFAZ DE LA TABLA HASH.
     * ESTE MÉTODO ES ESTÁTICO PARA PODER SER LLAMADO DIRECTAMENTE DESDE OTROS CONTROLADORES.
     *
     * @param parentFrame EL FRAME PADRE DESDE DONDE SE ABRE ESTA VENTANA.
     */
    public static void mostrarInterfazTablaHash(JFrame parentFrame) {
        SwingUtilities.invokeLater(() -> {
            ControladorTablaHash controlador = new ControladorTablaHash(parentFrame);
            controlador.vistaTablaHash.setVisible(true); // HACER VISIBLE LA VENTANA DE LA TABLA HASH.
        });
    }

    /**
     * RECOPILA LOS DATOS DE ENTRADA DEL ÁREA DE TEXTO, LOS HASHEA Y ACTUALIZA LA VISTA DE LA TABLA HASH.
     */
    private void generarTablaHash() {
        // RE-INICIALIZAR LA TABLA HASH CADA VEZ QUE SE GENERA, PARA LIMPIAR RESULTADOS ANTERIORES.
        tablaHashModelo = new TablaHashPersonalizada(TAMANO_TABLA_HASH);

        String rawData = vistaTablaHash.getInputDataArea().getText();
        String[] lines = rawData.split("\\n"); // DIVIDIR EL TEXTO POR LÍNEAS PARA OBTENER CADA DATO.

        if (lines.length == 0 || (lines.length == 1 && lines[0].trim().isEmpty())) {
            JOptionPane.showMessageDialog(vistaTablaHash, "POR FAVOR, INGRESE ALGUNOS DATOS EN EL CUADRO DE TEXTO.", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty()) { // IGNORAR LÍNEAS VACÍAS.
                tablaHashModelo.agregar(trimmedLine); // AGREGAR EL DATO A LA TABLA HASH.
            }
        }

        // OBTENER LOS DATOS PARA LA TABLA.
        List<String[]> datosParaTabla = tablaHashModelo.obtenerDatosParaVista();

        // ASEGURAR QUE LA ACTUALIZACIÓN DE LA TABLA SE HAGA EN EL HILO DE DESPACHO DE EVENTOS (EDT).
        SwingUtilities.invokeLater(() -> {
            vistaTablaHash.actualizarTabla(datosParaTabla);
        });

        JOptionPane.showMessageDialog(vistaTablaHash,
                "TABLA HASH GENERADA. TAMAÑO DE LA TABLA: " + TAMANO_TABLA_HASH + "\n" +
                "DATOS PROCESADOS: " + (lines.length - (rawData.isEmpty() ? 0 : 1)) , // CONTAR LÍNEAS NO VACÍAS.
                "INFORMACIÓN DE LA TABLA HASH", JOptionPane.INFORMATION_MESSAGE);
    }
}

