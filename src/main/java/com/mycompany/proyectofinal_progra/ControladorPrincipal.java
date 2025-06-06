/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal_progra;

/**
 *
 * @author cachi
 */
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.Component;
import javax.swing.SwingUtilities;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList; 
import java.util.Arrays;    
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; 
import javax.swing.filechooser.FileNameExtensionFilter; // IMPORTAR PARA FILTRAR EXTENSIONES.


/**
 * ACTÚA COMO INTERMEDIARIO ENTRE EL MODELO (LIBRO, HOJA, CELDA) Y LA VISTA (VISTAPRINCIPAL).
 * MANEJA LA LÓGICA DE NEGOCIO, LOS EVENTOS DE USUARIO Y LAS ACTUALIZACIONES DE LA INTERFAZ.
 */
public class ControladorPrincipal {
    private Libro libro; // EL MODELO DE DATOS PRINCIPAL (LIBRO DE HOJAS DE CÁLCULO).
    private VistaPrincipal vista; // LA VISTA DE LA APLICACIÓN.

    // Flag para evitar re-entrancy en TableModelListener.
    // Esto previene que una actualización programática de la celda dispare un nuevo evento de modelo
    // y cause un bucle infinito o un bloqueo de la interfaz.
    private boolean isUpdatingCellProgrammatically = false;


    // PATRONES REGEX PARA PARSEAR FÓRMULAS.
    // ESTE PATRÓN RECONOCE FORMULAS QUE EMPIEZAN CON "=", SEGUIDAS DE UNA OPERACIÓN (SUMA, PRODUCTO, RESTA)
    // Y ARGUMENTOS ENTRE PARÉNTESIS.
    private static final Pattern PATRON_FORMULA = Pattern.compile(
        "=\\s*(SUMA|PRODUCTO|RESTA)\\s*\\(\\s*(.*?)\\s*\\)", Pattern.CASE_INSENSITIVE
    );
    // ESTE PATRÓN SEPARA LOS ARGUMENTOS DE UNA FUNCIÓN, CONSIDERANDO COMAS FUERA DE PARÉNTESIS (PARA NO CORTAR COORDENADAS).
    private static final Pattern PATRON_ARGUMENTOS_SPLIT = Pattern.compile(
        ",(?![^()]*\\))" // SEPARA POR COMA, PERO NO SI LA COMA ESTÁ DENTRO DE PARÉNTESIS
    );
    // PATRÓN PARA DETECTAR UNA REFERENCIA DE CELDA TIPO EXCEL (EJ. A1, B2).
    private static final Pattern PATRON_REFERENCIA_A1 = Pattern.compile("^[A-Z]+\\d+$");
    // PATRÓN PARA DETECTAR UNA REFERENCIA DE CELDA TIPO COORDENADA (EJ. (1,1), (10,5)).
    private static final Pattern PATRON_REFERENCIA_COORD = Pattern.compile("^\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)$");
    // PATRÓN PARA DETECTAR SI UN ARGUMENTO ES UN NOMBRE DE HOJA (EJ. "Hoja 1").
    // SE HA HECHO MÁS ESPECÍFICO PARA EVITAR CONFLICTOS CON REFERENCIAS DE CELDA A1.
    private static final Pattern PATRON_NOMBRE_HOJA = Pattern.compile("^Hoja\\s+\\d+$", Pattern.CASE_INSENSITIVE);


    /**
     * CONSTRUCTOR DEL CONTROLADOR PRINCIPAL.
     * INICIALIZA EL MODELO Y LA VISTA, Y CONFIGURA LOS OYENTES DE EVENTOS.
     *
     * @param libro EL OBJETO LIBRO QUE ACTÚA COMO MODELO DE DATOS.
     * @param vista EL OBJETO VISTAPRINCIPAL QUE ACTÚA COMO INTERFAZ DE USUARIO.
     */
    public ControladorPrincipal(Libro libro, VistaPrincipal vista) {
        this.libro = libro;
        this.vista = vista;
        inicializarControlador();
        cargarPrimeraHoja(); // ASEGURAR QUE LA PRIMERA HOJA SE CARGUE AL INICIAR
    }

    /**
     * INICIALIZA LOS OYENTES DE EVENTOS PARA LOS COMPONENTES DE LA VISTA.
     */
    private void inicializarControlador() {
        // OYENTE PARA LA BARRA DE FÓRMULAS: ACTUALIZA LA CELDA SELECCIONADA AL ESCRIBIR.
        // ESTE LISTENER AHORA SOLO ALMACENA LA FÓRMULA/VALOR EN LA CELDA, PERO NO ACTUALIZA EL VALOR MOSTRADO EN LA TABLA
        // HASTA QUE SE PRESIONA "APLICAR" O SE CAMBIA EL ENFOQUE DE LA CELDA.
        vista.campoFormula.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // SOLO ALMACENAR LA FÓRMULA/VALOR PENDIENTE EN LA CELDA.
                actualizarCeldaPendienteDesdeCampoFormula();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // SOLO ALMACENAR LA FÓRMULA/VALOR PENDIENTE EN LA CELDA.
                actualizarCeldaPendienteDesdeCampoFormula();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // ESTE MÉTODO ES PARA CAMBIOS DE ATRIBUTOS, NO PARA TEXTO PLANO.
            }
        });

        // OYENTE PARA EL BOTÓN "APLICAR FÓRMULA": INTENTA EVALUAR LA FÓRMULA AL HACER CLIC.
        vista.aplicarFormulaBoton.addActionListener(e -> aplicarFormula());

        // OYENTE PARA CAMBIOS DE SELECCIÓN DE PESTAÑA (HOJA) EN EL JTABBEDPANE.
        vista.tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SwingUtilities.invokeLater(() -> {
                    // ACTUALIZAR LA HOJA ACTUAL EN EL MODELO CADA VEZ QUE LA PESTAÑA CAMBIA.
                    int selectedIndex = vista.tabbedPane.getSelectedIndex();
                    if (selectedIndex != -1) {
                        String nombreHojaSeleccionada = vista.tabbedPane.getTitleAt(selectedIndex);
                        Hoja hojaSeleccionada = libro.obtenerHojaPorNombre(nombreHojaSeleccionada);
                        if (hojaSeleccionada != null) {
                            libro.setHojaActual(hojaSeleccionada);
                        }
                    }
                    configurarListenersTablaActual(); // RECONFIGURAR LISTENERS PARA LA NUEVA TABLA.
                    actualizarCampoFormulaConCeldaSeleccionada(); // ACTUALIZAR CAMPO DE FÓRMULA.
                });
            }
        });

        // OYENTE PARA EL MENÚ "NUEVA HOJA".
        // ASUMIMOS QUE itemNuevaHoja ahora es parte de un menú "Archivo" en VistaPrincipal.
        vista.itemNuevaHoja.addActionListener(e -> agregarNuevaHoja());
        
        // OYENTE PARA EL MENÚ "GUARDAR".
        vista.itemGuardar.addActionListener(e -> guardarLibro());
        
        // OYENTE PARA EL MENÚ "ABRIR".
        vista.itemAbrir.addActionListener(e -> abrirLibro());

        // OYENTE PARA EL MENÚ "ELIMINAR HOJA".
        // Asumimos que itemEliminarHoja será un JMenuItem público en VistaPrincipal.
        vista.itemEliminarHoja.addActionListener(e -> eliminarHojaSeleccionada());


        // OYENTE PARA EL MENÚ "TABLA HASH".
        // ASUME QUE LA CLASE CONTROLADORTABLAHASH EXISTIRÁ Y TENDRÁ UN MÉTODO PARA MOSTRAR LA INTERFAZ.
        vista.itemTablaHash.addActionListener(e -> {
            ControladorTablaHash.mostrarInterfazTablaHash(vista);
        });

        // CONFIGURAR LISTENER INICIAL PARA LA TABLA DE LA PRIMERA HOJA CARGADA.
        configurarListenersTablaActual();
    }

    /**
     * CONFIGURA LOS LISTENERS DE SELECCIÓN Y MODELO PARA LA TABLA ACTUALMENTE ACTIVA.
     * ESTO ES NECESARIO CADA VEZ QUE CAMBIA LA HOJA SELECCIONADA O SE AGREGA UNA NUEVA HOJA.
     *
     * NOTA: LAS LÍNEAS DE REMOCIÓN EXPLÍCITA DE LISTENERS SE HAN ELIMINADO AQUÍ,
     * YA QUE LA FUNCIÓN 'sincronizarVistaConModelo()' SIEMPRE CREA NUEVAS INSTANCIAS DE JTABLE
     * PARA CADA PESTAÑA, HACIENDO QUE LA REMOCIÓN EXPLÍCITA DE LISTENERS SEA REDUNDANTE.
     * LOS LISTENERS SE AÑADEN A LAS NUEVAS INSTANCIAS DE TABLA.
     */
    private void configurarListenersTablaActual() {
        JTable tablaActual = vista.getTablaActual();
        if (tablaActual != null) {
            // AÑADIR NUEVOS LISTENERS DE SELECCIÓN DE FILAS Y COLUMNAS.
            tablaActual.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    actualizarCampoFormulaConCeldaSeleccionada();
                }
            });
            tablaActual.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    actualizarCampoFormulaConCeldaSeleccionada();
                }
            });

            // AÑADIR LISTENER PARA CAMBIOS EN LOS DATOS DEL MODELO DE LA TABLA (EDICIÓN MANUAL).
            tablaActual.getModel().addTableModelListener(e -> {
                // Solo proceder si es un evento de UPDATE y no es una actualización programática
                if (e.getType() == TableModelEvent.UPDATE && !isUpdatingCellProgrammatically) {
                    int fila = e.getFirstRow();
                    int columnaVista = e.getColumn(); // COLUMNA EN LA VISTA (INCLUYE COLUMNA '#')

                    // Asegurarse de que la actualización es para una celda de datos, no la columna de número de fila
                    if (columnaVista > 0) { 
                        String nuevoValorVista = (String) tablaActual.getModel().getValueAt(fila, columnaVista);
                        int columnaLogica = columnaVista - 1; // CONVERTIR A ÍNDICE LÓGICO (0-BASED)

                        // Establecer el flag para indicar que una actualización programática está a punto de ocurrir
                        isUpdatingCellProgrammatically = true;
                        try {
                            // Si el valor ingresado directamente en la celda es una fórmula
                            if (nuevoValorVista.startsWith("=")) {
                                evaluateAndDisplayCell(fila, columnaLogica, nuevoValorVista);
                            } else {
                                // Si no es una fórmula, establecer el valor literal y borrar la fórmula
                                Hoja hojaActual = libro.getHojaActual();
                                if (hojaActual != null) {
                                    NodoCelda nodoCelda = hojaActual.obtenerCelda(fila, columnaLogica);
                                    if (nodoCelda != null) {
                                        Celda celda = nodoCelda.getCelda();
                                        celda.setValor(nuevoValorVista);
                                        celda.setFormula(""); 
                                        actualizarCeldaEnVista(fila, columnaLogica, nuevoValorVista); // Esta llamada dispara un TableModelEvent, de ahí la necesidad del flag
                                    }
                                }
                            }
                            // Siempre actualizar el campo de fórmula en la vista
                            SwingUtilities.invokeLater(() -> vista.campoFormula.setText(nuevoValorVista));
                        } finally {
                            // Siempre resetear el flag después de la actualización, incluso si ocurre un error
                            isUpdatingCellProgrammatically = false;
                        }
                    }
                }
            });

            // AÑADIR MOUSELISTENER PARA SELECCIÓN DE CELDA (PARA CAPTURAR CLICS ESPECÍFICOS).
            tablaActual.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = tablaActual.rowAtPoint(e.getPoint());
                    int columna = tablaActual.columnAtPoint(e.getPoint()); 
                    if (fila >= 0 && columna > 0) { // Asegurarse de que no sea la columna de encabezado de fila
                        SwingUtilities.invokeLater(() -> actualizarCampoFormulaConCeldaSeleccionada());
                    }
                }
            });
        }
    }


    /**
     * SINCRONIZA LA VISTA (JTABBEDPANE Y JTABLES) CON EL ESTADO ACTUAL DEL LIBRO.
     * ESTO IMPLICA LIMPIAR Y RECONSTRUIR LAS PESTAÑAS Y SUS TABLAS ASOCIADAS.
     */
    private void sincronizarVistaConModelo() {
        vista.tabbedPane.removeAll(); // LIMPIAR TODAS LAS PESTAÑAS EXISTENTES.
        vista.modelosTablaPorHoja.clear(); // LIMPIAR EL MAPA DE MODELOS.

        NodoHoja actual = libro.getCabeza();
        while (actual != null) {
            Hoja hoja = actual.getHoja();
            DefaultTableModel model = crearModeloTablaParaHoja(hoja);
            vista.modelosTablaPorHoja.put(hoja.getNombre(), model);
            JTable tabla = new JTable(model);
            vista.configurarTabla(tabla); // APLICAR CONFIGURACIONES VISUALES A LA TABLA.
            JScrollPane scrollPane = new JScrollPane(tabla);
            vista.tabbedPane.addTab(hoja.getNombre(), scrollPane); // AGREGAR PESTAÑA CON LA TABLA.
            actual = actual.getSiguiente();
        }

        // ASEGURAR QUE LA HOJA ACTUAL DEL MODELO ESTÉ SELECCIONADA EN LA VISTA.
        if (libro.getHojaActual() != null) {
            int selectedIndex = vista.tabbedPane.indexOfTab(libro.getHojaActual().getNombre());
            if (selectedIndex != -1) {
                vista.tabbedPane.setSelectedIndex(selectedIndex);
            }
        } else if (vista.tabbedPane.getTabCount() > 0) {
            // SI NO HAY HOJA ACTUAL PERO HAY PESTAÑAS, SELECCIONAR LA PRIMERA.
            vista.tabbedPane.setSelectedIndex(0);
        }

        // DESPUÉS DE SINCRONIZAR, RECONFIGURAR LISTENERS PARA LA HOJA ACTUAL Y ACTUALIZAR CAMPO DE FÓRMULA.
        configurarListenersTablaActual();
        actualizarCampoFormulaConCeldaSeleccionada();
    }


    /**
     * CREA UN DEFAULTTABLEMODEL Y LO POPULA CON LOS DATOS DE UNA HOJA.
     * INCLUYE LA COLUMNA DE NÚMERO DE FILA (#) EN EL MODELO VISUAL.
     *
     * @param hoja LA HOJA DE LA CUAL OBTENER LOS DATOS.
     * @return UN DEFAULTTABLEMODEL CON LOS DATOS DE LA HOJA LISTOS PARA LA JTABLE.
     */
    private DefaultTableModel crearModeloTablaParaHoja(Hoja hoja) {
        String[] columnNames = new String[hoja.getColumnas() + 1];
        columnNames[0] = "#"; // PRIMERA COLUMNA PARA LOS NÚMEROS DE FILA.
        for (int i = 0; i < hoja.getColumnas(); i++) {
            columnNames[i + 1] = UtilidadesCelda.getExcelColumnName(i); // NOMBRES DE COLUMNA A, B, C...
        }

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // LA COLUMNA DE NÚMEROS DE FILA NO ES EDITABLE.
            }
        };

        // POPULAR EL MODELO CON LOS DATOS DE LA HOJA.
        for (int i = 0; i < hoja.getFilas(); i++) {
            Object[] rowData = new Object[hoja.getColumnas() + 1];
            rowData[0] = i + 1; // NÚMERO DE FILA (1-BASED).
            for (int j = 0; j < hoja.getColumnas(); j++) {
                NodoCelda nodo = hoja.obtenerCelda(i, j);
                if (nodo != null && nodo.getCelda() != null) {
                    rowData[j + 1] = nodo.getCelda().getValor(); // OBTENER EL VALOR CALCULADO/MOSTRADO.
                } else {
                    rowData[j + 1] = ""; // CELDA VACÍA.
                }
            }
            model.addRow(rowData);
        }
        return model;
    }


    /**
     * ALMACENA LA FÓRMULA O VALOR TEMPORALMENTE EN LA CELDA SELECCIONADA CUANDO EL USUARIO
     * ESCRIBE EN EL CAMPO DE FÓRMULAS. EL VALOR CALCULADO O LITERAL DE LA CELDA
     * EN LA TABLA SOLO SE ACTUALIZA CUANDO SE LLAMA A 'aplicarFormula()'.
     * ESTO RESUELVE EL PROBLEMA DE 'COPIAR VALORES' AL CAMBIAR DE CELDA.
     */
    private void actualizarCeldaPendienteDesdeCampoFormula() {
        int filaSeleccionada = vista.getFilaSeleccionada();
        int colSeleccionada = vista.getColumnaSeleccionada(); 

        if (filaSeleccionada != -1 && colSeleccionada != -1) {
            Hoja hojaActual = libro.getHojaActual();
            if (hojaActual != null) {
                NodoCelda nodoCelda = hojaActual.obtenerCelda(filaSeleccionada, colSeleccionada);
                if (nodoCelda != null) {
                    Celda celda = nodoCelda.getCelda();
                    String textoCampo = vista.campoFormula.getText();

                    if (textoCampo.startsWith("=")) {
                        celda.setFormula(textoCampo);
                        // NO SE ACTUALIZA EL VALOR MOSTRADO DE LA CELDA AQUÍ, SOLO LA FÓRMULA.
                    } else {
                        // SI NO ES FÓRMULA, ALMACENAR COMO VALOR LITERAL TEMPORALMENTE.
                        celda.setValor(textoCampo);
                        celda.setFormula(""); // BORRAR FÓRMULA SI SE INGRESA UN VALOR DIRECTO.
                    }
                    // LA ACTUALIZACIÓN DE LA VISTA (JTABLE) SE DEJA PARA 'aplicarFormula()' O 'TableModelListener'
                }
            }
        }
    }


    /**
     * ACTUALIZA EL CAMPO DE FÓRMULAS EN LA VISTA CON EL CONTENIDO (VALOR CALCULADO O FÓRMULA ORIGINAL)
     * DE LA CELDA ACTUALMENTE SELECCIONADA EN LA TABLA.
     */
    private void actualizarCampoFormulaConCeldaSeleccionada() {
        int filaSeleccionada = vista.getFilaSeleccionada();
        int colSeleccionada = vista.getColumnaSeleccionada(); // ESTE YA ES EL ÍNDICE LÓGICO (0-BASED)

        if (filaSeleccionada != -1 && colSeleccionada != -1) {
            Hoja hojaActual = libro.getHojaActual();
            if (hojaActual != null) {
                NodoCelda nodoCelda = hojaActual.obtenerCelda(filaSeleccionada, colSeleccionada);
                if (nodoCelda != null) {
                    Celda celda = nodoCelda.getCelda();
                    if (!celda.getFormula().isEmpty()) {
                        vista.campoFormula.setText(celda.getFormula()); // MOSTRAR LA FÓRMULA ORIGINAL.
                    } else {
                        vista.campoFormula.setText(celda.getValor()); // MOSTRAR EL VALOR ACTUAL.
                    }
                } else {
                    vista.campoFormula.setText(""); // CELDA NO ENCONTRADA (FUERA DE RANGO O NO EXISTE).
                }
            }
        } else {
            vista.campoFormula.setText(""); // NINGUNA CELDA SELECCIONADA.
        }
    }


    /**
     * INTENTA APLICAR Y EVALUAR LA FÓRMULA ALMACENADA EN LA CELDA SELECCIONADA.
     * Este método es llamado por el botón "Aplicar".
     */
    private void aplicarFormula() {
        int filaSeleccionada = vista.getFilaSeleccionada();
        int colSeleccionada = vista.getColumnaSeleccionada();
        String expresion = vista.campoFormula.getText().trim();

        // Delega la evaluación y visualización al nuevo método.
        evaluateAndDisplayCell(filaSeleccionada, colSeleccionada, expresion);
    }

    /**
     * EVALÚA UNA EXPRESIÓN DE FÓRMULA DADA PARA UNA CELDA ESPECÍFICA Y ACTUALIZA SU VISUALIZACIÓN.
     * Este método centraliza la lógica de evaluación y manejo de errores.
     *
     * @param fila La fila de la celda (0-based).
     * @param col La columna de la celda (0-based).
     * @param expresion La cadena de la fórmula a evaluar.
     */
    private void evaluateAndDisplayCell(int fila, int col, String expresion) {
        if (fila == -1 || col == -1) {
            JOptionPane.showMessageDialog(vista, "POR FAVOR, SELECCIONE UNA CELDA PARA APLICAR LA FÓRMULA.", "ERROR", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Hoja hojaActual = libro.getHojaActual();
        if (hojaActual == null) {
            JOptionPane.showMessageDialog(vista, "NO HAY HOJA ACTIVA PARA APLICAR LA FÓRMULA.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        NodoCelda nodoCelda = hojaActual.obtenerCelda(fila, col);
        if (nodoCelda == null) {
            JOptionPane.showMessageDialog(vista, "ERROR: LA CELDA SELECCIONADA ESTÁ FUERA DE RANGO VÁLIDO EN LA HOJA ACTUAL.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Celda celda = nodoCelda.getCelda();

        // Si la expresión está vacía o no es una fórmula (no empieza con '='), borrar fórmula y establecer valor literal.
        if (expresion.isEmpty() || !expresion.startsWith("=")) {
            celda.setValor(expresion); // Almacena el texto tal cual o vacío.
            celda.setFormula("");
            actualizarCeldaEnVista(fila, col, expresion);
            return;
        }

        try {
            double resultado = evaluarExpresion(expresion);
            celda.setValor(String.valueOf(resultado)); // Almacenar el resultado numérico como string.
            celda.setFormula(expresion); // Almacenar la fórmula original.
            actualizarCeldaEnVista(fila, col, String.valueOf(resultado));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, "ERROR EN LA FÓRMULA: " + ex.getMessage(), "ERROR DE FÓRMULA", JOptionPane.ERROR_MESSAGE);
            celda.setValor("!ERROR!"); // Indicar error en la celda.
            celda.setFormula(expresion); // Conservar la fórmula errónea para facilitar la edición.
            actualizarCeldaEnVista(fila, col, "!ERROR!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "ERROR DESCONOCIDO AL EVALUAR LA FÓRMULA: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            celda.setValor("!ERROR!");
            celda.setFormula(expresion);
            actualizarCeldaEnVista(fila, col, "!ERROR!");
        }
    }


    /**
     * EVALÚA UNA EXPRESIÓN DE FÓRMULA DADA.
     * EL FORMATO ESPERADO ES "=OPERACION([NOMBRE_HOJA], ARG1, ARG2, ...)"
     * DONDE [NOMBRE_HOJA] ES OPCIONAL Y LOS ARGUMENTOS PUEDEN SER REFERENCIAS DE CELDA (A1, (FILA,COLUMNA))
     * O LITERALES NUMÉRICOS.
     *
     * @param expresion LA CADENA DE LA FÓRMULA A EVALUAR.
     * @return EL RESULTADO NUMÉRICO DE LA EVALUACIÓN.
     * @throws IllegalArgumentException SI LA EXPRESIÓN ES INVÁLIDA O HAY ERRORES EN LAS REFERENCIAS.
     */
    private double evaluarExpresion(String expresion) throws IllegalArgumentException {
        Matcher matcher = PATRON_FORMULA.matcher(expresion);
        if (!matcher.matches()) {
            // Se ha cambiado el ejemplo de fórmula para que coincida con la nueva sintaxis.
            throw new IllegalArgumentException("FORMATO DE FÓRMULA INVÁLIDO. EJEMPLO: =SUMA(Hoja 1, A1, (2,3))");
        }

        String operation = matcher.group(1).toUpperCase(); // "SUMA", "PRODUCTO", "RESTA".
        String argsString = matcher.group(2); // CADENA COMPLETA DE ARGUMENTOS.

        // DIVIDIR LA CADENA DE ARGUMENTOS USANDO EL PATRÓN PARA NO CORTAR COORDENADAS.
        String[] rawArgs = PATRON_ARGUMENTOS_SPLIT.split(argsString);

        // VALIDAR QUE HAYA AL MENOS UN ARGUMENTO.
        if (rawArgs.length == 0 || (rawArgs.length == 1 && rawArgs[0].trim().isEmpty())) {
            throw new IllegalArgumentException("LA FUNCIÓN '" + operation + "' REQUIERE AL MENOS UN ARGUMENTO VÁLIDO.");
        }

        List<Double> valoresNumericos = new ArrayList<>();
        Hoja targetSheet = libro.getHojaActual(); // INICIAR CON LA HOJA ACTUAL COMO OBJETIVO POR DEFECTO.

        int startIndex = 0;
        String firstArgTrimmed = rawArgs[0].trim();
        
        // REVISIÓN DEL ORDEN DE EVALUACIÓN PARA LA PRIMERA REFERENCIA
        // Primero, intentar como referencia de celda (A1 o (fila,columna)) en la hoja actual
        // Si no es una referencia de celda válida, entonces intentar como nombre de hoja.

        boolean isFirstArgCellRef = false;
        if (PATRON_REFERENCIA_COORD.matcher(firstArgTrimmed).matches()) {
            // Es una referencia de coordenada, se procesará en el bucle principal con la hoja actual.
            startIndex = 0;
            isFirstArgCellRef = true;
        } else if (PATRON_REFERENCIA_A1.matcher(firstArgTrimmed).matches()) {
            // Es una referencia A1, se procesará en el bucle principal con la hoja actual.
            startIndex = 0;
            isFirstArgCellRef = true;
        }

        // Si el primer argumento NO fue una referencia de celda, entonces intentar como nombre de hoja
        if (!isFirstArgCellRef && PATRON_NOMBRE_HOJA.matcher(firstArgTrimmed).matches()) {
            String nombreHoja = firstArgTrimmed;
            Hoja hojaEncontrada = libro.obtenerHojaPorNombre(nombreHoja);
            if (hojaEncontrada == null) {
                throw new IllegalArgumentException("LA HOJA '" + nombreHoja + "' REFERENCIADA NO EXISTE.");
            }
            targetSheet = hojaEncontrada; // Establecer la hoja objetivo.
            startIndex = 1; // Empezar a procesar celdas desde el segundo argumento.
        }

        if (targetSheet == null) {
            throw new IllegalArgumentException("NO SE PUDO DETERMINAR LA HOJA OBJETIVO PARA LA OPERACIÓN.");
        }

        // PROCESAR LOS ARGUMENTOS RESTANTES (REFERENCIAS DE CELDA O LITERALES).
        for (int i = startIndex; i < rawArgs.length; i++) {
            String cellRefStr = rawArgs[i].trim();
            if (cellRefStr.isEmpty()) continue; // IGNORAR ARGUMENTOS VACÍOS.

            double val;
            try {
                if (PATRON_REFERENCIA_COORD.matcher(cellRefStr).matches()) {
                    // SI ES FORMATO (FILA,COLUMNA).
                    int[] coords = parseCoordenadasCelda(cellRefStr);
                    int row = coords[0];
                    int col = coords[1];
                    // VALIDAR RANGO DE LA CELDA EN LA HOJA OBJETIVO.
                    if (row < 0 || row >= targetSheet.getFilas() || col < 0 || col >= targetSheet.getColumnas()) {
                        throw new IllegalArgumentException("REFERENCIA DE CELDA (" + (row+1) + "," + (col+1) + ") EN HOJA '" + targetSheet.getNombre() + "' FUERA DE RANGO.");
                    }
                    val = targetSheet.obtenerValorNumericoCelda(row, col); // OBTENER EL VALOR NUMÉRICO.
                } else if (PATRON_REFERENCIA_A1.matcher(cellRefStr).matches()) {
                    // SI ES FORMATO A1 (EJ. A1, B5).
                    int[] coords = UtilidadesCelda.parseReferenciaCelda(cellRefStr);
                    int row = coords[0];
                    int col = coords[1];
                    // VALIDAR RANGO DE LA CELDA EN LA HOJA OBJETIVO.
                    if (row < 0 || row >= targetSheet.getFilas() || col < 0 || col >= targetSheet.getColumnas()) {
                        throw new IllegalArgumentException("REFERENCIA DE CELDA '" + cellRefStr + "' EN HOJA '" + targetSheet.getNombre() + "' FUERA DE RANGO.");
                    }
                    val = targetSheet.obtenerValorNumericoCelda(row, col); // OBTENER EL VALOR NUMÉRICO.
                } else {
                    // SI NO ES UNA REFERENCIA DE CELDA VÁLIDA, INTENTAR PARSEAR COMO NÚMERO LITERAL.
                    try {
                        val = Double.parseDouble(cellRefStr);
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("ARGUMENTO INVÁLIDO EN FÓRMULA: '" + cellRefStr + "'. DEBE SER REFERENCIA DE CELDA, NÚMERO O NOMBRE DE HOJA.");
                    }
                }
                valoresNumericos.add(val);
            } catch (Exception e) {
                throw new IllegalArgumentException("ERROR AL PROCESAR ARGUMENTO '" + cellRefStr + "': " + e.getMessage());
            }
        }

        // VERIFICAR QUE SE HAYAN OBTENIDO VALORES NUMÉRICOS PARA LA OPERACIÓN.
        if (valoresNumericos.isEmpty()) {
            throw new IllegalArgumentException("LA FUNCIÓN '" + operation + "' REQUIERE AL MENOS UN ARGUMENTO NUMÉRICO VÁLIDO.");
        }

        // REALIZAR LA OPERACIÓN SEGÚN EL TIPO ESPECIFICADO.
        double result = 0.0;
        switch (operation) {
            case "SUMA":
                result = valoresNumericos.stream().mapToDouble(Double::doubleValue).sum();
                break;
            case "PRODUCTO":
                result = 1.0; // INICIALIZAR EN 1 PARA MULTIPLICACIÓN.
                for (double d : valoresNumericos) {
                    result *= d;
                }
                break;
            case "RESTA":
                if (valoresNumericos.size() < 1) { // UNA RESTA CON 0 ARGUMENTOS NO TIENE SENTIDO.
                    throw new IllegalArgumentException("LA FUNCIÓN RESTA REQUIERE AL MENOS UN NÚMERO.");
                }
                result = valoresNumericos.get(0); // EL PRIMER ELEMENTO ES EL MINUENDO.
                for (int i = 1; i < valoresNumericos.size(); i++) {
                    result -= valoresNumericos.get(i); // RESTAR LOS ELEMENTOS SUBSECUENTES.
                }
                break;
            default:
                throw new IllegalArgumentException("OPERACIÓN NO SOPORTADA: " + operation);
        }
        return result;
    }


    /**
     * PARSEA UNA CADENA DE COORDENADAS DE CELDA CON EL FORMATO "(FILA,COLUMNA)"
     * Y DEVUELVE UN ARRAY DE ENTEROS [FILA, COLUMNA] (AMBOS 0-BASED).
     *
     * @param coordStr LA CADENA DE COORDENADAS (EJ. "(1,1)").
     * @return UN ARRAY INT[] DONDE [0] ES LA FILA (0-BASED) Y [1] ES LA COLUMNA (0-BASED).
     * @throws IllegalArgumentException SI EL FORMATO ES INVÁLIDO O LOS NÚMEROS NO SON VÁLIDOS.
     */
    private int[] parseCoordenadasCelda(String coordStr) throws IllegalArgumentException {
        // QUITAR PARÉNTESIS Y ESPACIOS.
        String cleaned = coordStr.replaceAll("[\\(\\)]", "").trim();
        String[] parts = cleaned.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("FORMATO DE COORDENADAS INVÁLIDO: " + coordStr + ". ESPERADO: (FILA,COLUMNA).");
        }
        try {
            int fila = Integer.parseInt(parts[0].trim()) - 1;    // CONVERTIR A 0-BASED (1 = FILA 0).
            int columna = Integer.parseInt(parts[1].trim()) - 1; // CONVERTIR A 0-BASED (1 = COLUMNA 0).
            if (fila < 0 || columna < 0) {
                 throw new IllegalArgumentException("FILA Y COLUMNA EN COORDENADAS DEBEN SER NÚMEROS POSITIVOS (MAYORES A CERO).");
            }
            return new int[]{fila, columna};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("LOS VALORES DE FILA Y COLUMNA DEBEN SER NÚMEROS VÁLIDOS EN '" + coordStr + "'.");
        }
    }


    /**
     * ACTUALIZA EL VALOR DE UNA CELDA ESPECÍFICA EN LA VISTA (JTABLE) DE LA HOJA ACTUAL.
     * ESTE MÉTODO SE UTILIZA PARA REFLEJAR LOS CAMBIOS DEL MODELO EN LA INTERFAZ.
     *
     * @param fila LA FILA DE LA CELDA (0-BASED).
     * @param col LA COLUMNA DE LA CELDA (0-BASED, LÓGICA, SIN CONTAR COLUMNA '#').
     * @param valor EL NUEVO VALOR A MOSTRAR EN LA CELDA.
     */
    private void actualizarCeldaEnVista(int fila, int col, String valor) {
        Hoja hojaActual = libro.getHojaActual();
        if (hojaActual != null) {
            DefaultTableModel model = vista.modelosTablaPorHoja.get(hojaActual.getNombre());
            if (model != null) {
                // +1 PORQUE EL MODELO DE LA TABLA TIENE LA COLUMNA '#' AL PRINCIPIO.
                model.setValueAt(valor, fila, col + 1);
            }
        }
    }

    /**
     * AGREGA UNA NUEVA HOJA AL LIBRO Y LA REFLEJA EN LA INTERFAZ DE USUARIO COMO UNA NUEVA PESTAÑA.
     */
    private void agregarNuevaHoja() {
        String nombreNuevaHoja = JOptionPane.showInputDialog(vista, "INGRESE EL NOMBRE DE LA NUEVA HOJA:", "NUEVA HOJA", JOptionPane.QUESTION_MESSAGE);
        if (nombreNuevaHoja != null && !nombreNuevaHoja.trim().isEmpty()) {
            // VERIFICAR SI YA EXISTE UNA HOJA CON ESE NOMBRE.
            if (libro.obtenerHojaPorNombre(nombreNuevaHoja) != null) {
                JOptionPane.showMessageDialog(vista, "YA EXISTE UNA HOJA CON EL NOMBRE '" + nombreNuevaHoja + "'. POR FAVOR, ELIJA OTRO NOMBRE.", "NOMBRE DUPLICADO", JOptionPane.WARNING_MESSAGE);
                return;
            }

            final int NUM_FILAS = 1000;    // NÚMERO DE FILAS PREDETERMINADAS PARA LA NUEVA HOJA (SOLICITADO: 1000).
            final int NUM_COLUMNAS = 100; // NÚMERO DE COLUMNAS PREDETERMINADAS PARA LA NUEVA HOJA (SOLICITADO: 100).
            libro.agregarHoja(nombreNuevaHoja, NUM_FILAS, NUM_COLUMNAS);
            sincronizarVistaConModelo(); // RECARGAR PESTAÑAS Y TABLAS PARA REFLEJAR LA NUEVA HOJA.
            // SELECCIONAR LA NUEVA HOJA EN LA VISTA (LA ÚLTIMA PESTAÑA AGREGADA).
            vista.tabbedPane.setSelectedIndex(vista.tabbedPane.getTabCount() - 1);
            JOptionPane.showMessageDialog(vista, "HOJA '" + nombreNuevaHoja + "' CREADA CORRECTAMENTE.", "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, "EL NOMBRE DE LA HOJA NO PUEDE ESTAR VACÍO.", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * ELIMINA LA HOJA ACTUALMENTE SELECCIONADA DEL LIBRO.
     * Muestra una confirmación al usuario antes de eliminar.
     */
    private void eliminarHojaSeleccionada() {
        int selectedIndex = vista.tabbedPane.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(vista, "NO HAY NINGUNA HOJA SELECCIONADA PARA ELIMINAR.", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (libro.getNumeroHojas() <= 1) {
            JOptionPane.showMessageDialog(vista, "NO SE PUEDE ELIMINAR LA ÚLTIMA HOJA DEL LIBRO.", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreHojaAEliminar = vista.tabbedPane.getTitleAt(selectedIndex);
        int confirm = JOptionPane.showConfirmDialog(vista, 
                                                    "¿ESTÁ SEGURO DE QUE DESEA ELIMINAR LA HOJA '" + nombreHojaAEliminar + "'?\nESTA ACCIÓN NO SE PUEDE DESHACER.", 
                                                    "CONFIRMAR ELIMINACIÓN DE HOJA", 
                                                    JOptionPane.YES_NO_OPTION, 
                                                    JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            libro.eliminarHoja(nombreHojaAEliminar); // Llama al método del modelo para eliminar la hoja.
            sincronizarVistaConModelo(); // Actualiza la vista para reflejar la eliminación.
            JOptionPane.showMessageDialog(vista, "LA HOJA '" + nombreHojaAEliminar + "' HA SIDO ELIMINADA.", "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    /**
     * CARGA LA PRIMERA HOJA DEL LIBRO EN LA VISTA AL INICIO DE LA APLICACIÓN.
     * SI NO HAY HOJAS, SE MUESTRA UN MENSAJE SUGIRIENDO AGREGAR UNA.
     */
    private void cargarPrimeraHoja() {
        // UTILIZA SWINGUTILITIES.INVOKELATER PARA ASEGURAR QUE LAS OPERACIONES DE UI SE HAGAN EN EL HILO DE EVENTOS DE SWING.
        SwingUtilities.invokeLater(() -> {
            if (libro.getNumeroHojas() > 0 && libro.getHojaActual() != null) {
                sincronizarVistaConModelo();
            } else {
                JOptionPane.showMessageDialog(vista, "LIBRO VACÍO. POR FAVOR, AGREGUE UNA NUEVA HOJA O ABRA UN LIBRO EXISTENTE.", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }


    /**
     * GUARDA EL ESTADO ACTUAL DEL OBJETO LIBRO EN UN ARCHIVO BINARIO
     * UTILIZANDO SERIALIZACIÓN JAVA.
     */
    private void guardarLibro() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("GUARDAR LIBRO DE CÁLCULO");
        // Establecer un filtro de extensión para archivos de la aplicación (.sps)
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Hoja de Cálculo (*.sps)", "sps");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(vista); // MOSTRAR DIÁLOGO DE GUARDADO.

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // ASEGURAR QUE EL ARCHIVO TENGA LA EXTENSIÓN ".sps".
            if (!fileToSave.getName().toLowerCase().endsWith(".sps")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".sps");
            }
            try (FileOutputStream fileOut = new FileOutputStream(fileToSave);
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(libro); // SERIALIZAR Y ESCRIBIR EL OBJETO LIBRO.
                JOptionPane.showMessageDialog(vista, "LIBRO GUARDADO CORRECTAMENTE EN:\n" + fileToSave.getAbsolutePath() + "\n(Este archivo se puede abrir con esta aplicación)", "GUARDAR", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException i) {
                JOptionPane.showMessageDialog(vista, "ERROR AL GUARDAR EL LIBRO: " + i.getMessage(), "ERROR DE GUARDADO", JOptionPane.ERROR_MESSAGE);
                i.printStackTrace(); // IMPRIMIR LA PILA DE LLAMADAS PARA DEPURACIÓN.
            }
        }
    }

    /**
     * ABRE UN LIBRO PREVIAMENTE GUARDADO DESDE UN ARCHIVO BINARIO
     * Y LO ESTABLECE COMO EL LIBRO ACTUAL DE LA APLICACIÓN.
     */
    private void abrirLibro() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("ABRIR LIBRO DE CÁLCULO");
        // Establecer un filtro de extensión para archivos de la aplicación (.sps)
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Hoja de Cálculo (*.sps)", "sps");
        fileChooser.setFileFilter(filter);


        int userSelection = fileChooser.showOpenDialog(vista); // MOSTRAR DIÁLOGO DE APERTURA.

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try (FileInputStream fileIn = new FileInputStream(fileToOpen);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                Libro loadedLibro = (Libro) in.readObject(); // DESERIALIZAR EL OBJETO LIBRO.
                this.libro = loadedLibro; // REEMPLAZAR EL LIBRO ACTUAL CON EL CARGADO.
                // ASEGURAR QUE LA HOJA ACTUAL ESTÉ BIEN CONFIGURADA DESPUÉS DE LA CARGA, SI NO LO ESTÁ.
                if (this.libro.getCabeza() != null && this.libro.getHojaActual() == null) {
                    this.libro.setHojaActual(this.libro.getCabeza().getHoja());
                }
                sincronizarVistaConModelo(); // ACTUALIZAR LA VISTA CON LOS DATOS DEL NUEVO LIBRO.
                JOptionPane.showMessageDialog(vista, "LIBRO CARGADO CORRECTAMENTE DESDE:\n" + fileToOpen.getAbsolutePath(), "ABRIR", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException i) {
                JOptionPane.showMessageDialog(vista, "ERROR AL ABRIR EL LIBRO: " + i.getMessage(), "ERROR DE APERTURA", JOptionPane.ERROR_MESSAGE);
                i.printStackTrace(); // IMPRIMIR LA PILA DE LLAMADAS PARA DEPURACIÓN.
            }
        }
    }
}