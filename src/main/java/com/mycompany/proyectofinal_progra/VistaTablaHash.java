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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List; // ASEGURARSE DE QUE ESTA IMPORTACIÓN ESTÉ PRESENTE.

/**
 * REPRESENTA LA INTERFAZ DE USUARIO PARA LA FUNCIONALIDAD DE LA TABLA HASH.
 * PERMITE AL USUARIO INGRESAR DATOS Y VISUALIZAR CÓMO SE ALMACENAN EN LA TABLA HASH
 * CON SUS RESPECTIVOS ÍNDICES.
 */
public class VistaTablaHash extends JFrame {
    private JTextArea inputDataArea; // ÁREA DE TEXTO PARA INGRESAR DATOS (UNO POR LÍNEA).
    private JButton btnGenerar;       // BOTÓN PARA GENERAR LA TABLA HASH.
    private JTable tablaHashDisplay;  // TABLA PARA MOSTRAR LA ESTRUCTURA HASH.
    private DefaultTableModel tablaHashModel; // MODELO DE DATOS PARA LA JTABLE.

    /**
     * CONSTRUCTOR PARA CREAR UNA NUEVA INSTANCIA DE VISTATABLAHASH.
     * CONFIGURA TODOS LOS COMPONENTES DE LA INTERFAZ DE USUARIO.
     */
    public VistaTablaHash() {
        setTitle("GENERADOR DE TABLA HASH");
        setSize(600, 700);
        setLocationRelativeTo(null); // CENTRAR LA VENTANA EN LA PANTALLA.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // CERRAR SOLO ESTA VENTANA.

        inicializarComponentes(); // CONFIGURAR ELEMENTOS DE LA INTERFAZ.
        establecerLayout(); // DISPONER LOS COMPONENTES EN LA VENTANA.
    }

    /**
     * INICIALIZA Y CONFIGURA LOS COMPONENTES DE LA INTERFAZ DE USUARIO.
     */
    private void inicializarComponentes() {
        inputDataArea = new JTextArea(10, 30);
        inputDataArea.setLineWrap(true);
        inputDataArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(inputDataArea);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder("INGRESE DATOS (UNO POR LÍNEA)"));

        btnGenerar = new JButton("GENERAR TABLA HASH");

        // CONFIGURACIÓN DEL MODELO DE LA TABLA HASH.
        String[] columnNames = {"ÍNDICE HASH", "DATO"};
        tablaHashModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // LAS CELDAS DE LA TABLA HASH NO SON EDITABLES.
            }
        };
        tablaHashDisplay = new JTable(tablaHashModel);
        tablaHashDisplay.getTableHeader().setReorderingAllowed(false); // EVITAR REORDENAR COLUMNAS.
        tablaHashDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // PERMITIR SOLO UNA SELECCIÓN.

        JScrollPane tableScrollPane = new JScrollPane(tablaHashDisplay);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("TABLA HASH GENERADA"));
    }

    /**
     * ESTABLECE EL LAYOUT Y AÑADE LOS COMPONENTES A LA VENTANA.
     */
    private void establecerLayout() {
        setLayout(new BorderLayout(10, 10)); // BORDERLAYOUT CON ESPACIADO.

        // PANEL SUPERIOR PARA INPUT Y BOTÓN.
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(inputDataArea, BorderLayout.CENTER);
        topPanel.add(btnGenerar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH); // AÑADIR AL NORTE DEL FRAME.

        // PANEL CENTRAL PARA LA TABLA.
        add(new JScrollPane(tablaHashDisplay), BorderLayout.CENTER); // AÑADIR AL CENTRO DEL FRAME.

        // ESPACIO ENTRE COMPONENTES Y BORDES DE LA VENTANA.
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * OBTIENE EL ÁREA DE TEXTO DONDE EL USUARIO INGRESA LOS DATOS.
     *
     * @return EL JTEXTAREA PARA LA ENTRADA DE DATOS.
     */
    public JTextArea getInputDataArea() {
        return inputDataArea;
    }

    /**
     * OBTIENE EL BOTÓN PARA GENERAR LA TABLA HASH.
     *
     * @return EL JBUTTON PARA LA ACCIÓN DE GENERAR.
     */
    public JButton getBtnGenerar() {
        return btnGenerar;
    }

    /**
     * ACTUALIZA LOS DATOS MOSTRADOS EN LA JTABLE DE LA TABLA HASH.
     * LIMPIA EL MODELO EXISTENTE Y AÑADE LAS NUEVAS FILAS.
     *
     * @param datos UN OBJETO LISTA DE ARRAYS DE STRING, DONDE CADA ARRAY ES UNA FILA
     * CON [ÍNDICE HASH, DATO].
     */
    public void actualizarTabla(List<String[]> datos) {
        tablaHashModel.setRowCount(0); // LIMPIAR FILAS EXISTENTES.
        for (String[] row : datos) {
            tablaHashModel.addRow(row); // AÑADIR CADA FILA DE DATOS.
        }
    }
}


