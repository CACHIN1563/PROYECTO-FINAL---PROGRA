/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal_progra;

/**
 *
 * @author cachi
 */
/**
 * Represents an individual sheet within the spreadsheet workbook.
 * Uses an orthogonal matrix (implemented with NodoCelda) to store cells.
 */
import java.io.Serializable; // IMPORTAR LA INTERFAZ SERIALIZABLE.

/**
 * REPRESENTA UNA HOJA INDIVIDUAL DENTRO DEL LIBRO DE CÁLCULO.
 * UTILIZA UNA MATRIZ ORTOGONAL (IMPLEMENTADA CON NODOCELDA) PARA ALMACENAR CELDAS.
 */
public class Hoja implements Serializable {
    private static final long serialVersionUID = 1L; // PARA CONTROL DE VERSIONES DE SERIALIZACIÓN.

    private String nombre; // EL NOMBRE ÚNICO DE LA HOJA.
    private NodoCelda inicio; // EL NODO INICIAL (0,0) DE LA MATRIZ ORTOGONAL DE CELDAS.
    private int filas, columnas; // NÚMERO TOTAL DE FILAS Y COLUMNAS EN LA HOJA.

    /**
     * CONSTRUCTOR PARA CREAR UNA NUEVA HOJA.
     * INICIALIZA LA MATRIZ ORTOGONAL DE CELDAS.
     *
     * @param nombre EL NOMBRE DE LA HOJA.
     * @param filas EL NÚMERO DE FILAS EN LA HOJA.
     * @param columnas EL NÚMERO DE COLUMNAS EN LA HOJA.
     */
    public Hoja(String nombre, int filas, int columnas) {
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.inicio = construirMatriz(filas, columnas); // CONSTRUIR LA ESTRUCTURA DE LA MATRIZ.
    }

    /**
     * CONSTRUYE LA ESTRUCTURA DE LA MATRIZ ORTOGONAL VINCULANDO OBJETOS NODOCELDA.
     * ESTE MÉTODO CREA CELDAS Y ESTABLECE LAS REFERENCIAS DERECHA Y ABAJO PARA FORMAR LA MATRIZ.
     *
     * @param numFilas EL NÚMERO DE FILAS.
     * @param numColumnas EL NÚMERO DE COLUMNAS.
     * @return EL NODO INICIAL (0,0) DE LA MATRIZ.
     */
    private NodoCelda construirMatriz(int numFilas, int numColumnas) {
        if (numFilas <= 0 || numColumnas <= 0) {
            return null; // NO SE PUEDE CONSTRUIR UNA MATRIZ CON DIMENSIONES INVÁLIDAS.
        }

        // CREAR LA PRIMERA FILA.
        NodoCelda cabezaFila = new NodoCelda(new Celda(0, 0));
        NodoCelda actualFila = cabezaFila;
        for (int j = 1; j < numColumnas; j++) {
            NodoCelda nuevoNodo = new NodoCelda(new Celda(0, j));
            actualFila.setDerecha(nuevoNodo);
            actualFila = nuevoNodo;
        }

        // CREAR FILAS SUBSECUENTES Y ENLAZARLAS.
        NodoCelda nodoArribaEnColumna = cabezaFila;
        for (int i = 1; i < numFilas; i++) {
            NodoCelda cabezaNuevaFila = new NodoCelda(new Celda(i, 0));
            nodoArribaEnColumna.setAbajo(cabezaNuevaFila); // ENLAZAR LA CABEZA DE LA FILA ANTERIOR CON LA NUEVA.
            actualFila = cabezaNuevaFila;
            NodoCelda tempNodoArriba = nodoArribaEnColumna.getDerecha(); // INICIAR CON EL NODO DERECHO EN LA FILA DE ARRIBA.

            for (int j = 1; j < numColumnas; j++) {
                NodoCelda nuevoNodo = new NodoCelda(new Celda(i, j));
                actualFila.setDerecha(nuevoNodo); // ENLAZAR CON EL NODO A LA DERECHA EN LA MISMA FILA.
                if (tempNodoArriba != null) {
                    tempNodoArriba.setAbajo(nuevoNodo); // ENLAZAR CON EL NODO DE ARRIBA EN LA MISMA COLUMNA.
                    tempNodoArriba = tempNodoArriba.getDerecha(); // MOVER AL SIGUIENTE NODO EN LA FILA DE ARRIBA.
                }
                actualFila = nuevoNodo;
            }
            nodoArribaEnColumna = nodoArribaEnColumna.getAbajo(); // MOVER AL SIGUIENTE NODO ABAJO PARA LA PRÓXIMA FILA.
        }
        return cabezaFila; // RETORNAR EL NODO INICIAL (0,0).
    }

    /**
     * OBTIENE EL NODOCELDA EN LA POSICIÓN ESPECIFICADA (FILA Y COLUMNA 0-BASED).
     *
     * @param fila LA FILA DE LA CELDA.
     * @param columna LA COLUMNA DE LA CELDA.
     * @return EL NODOCELDA EN LA POSICIÓN ESPECIFICADA, O NULL SI ESTÁ FUERA DE RANGO.
     */
    public NodoCelda obtenerCelda(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            return null; // LA CELDA ESTÁ FUERA DE LOS LÍMITES DE LA HOJA.
        }
        NodoCelda actual = inicio;
        // NAVEGAR HACIA ABAJO HASTA LA FILA DESEADA.
        for (int i = 0; i < fila && actual != null; i++) {
            actual = actual.getAbajo();
        }
        // NAVEGAR HACIA LA DERECHA HASTA LA COLUMNA DESEADA.
        for (int j = 0; j < columna && actual != null; j++) {
            actual = actual.getDerecha();
        }
        return actual;
    }

    /**
     * OBTIENE EL VALOR NUMÉRICO DE UNA CELDA ESPECÍFICA EN LA HOJA.
     * SI LA CELDA NO EXISTE O SU VALOR NO ES UN NÚMERO, RETORNA 0.0.
     *
     * @param fila LA FILA DE LA CELDA (0-BASED).
     * @param columna LA COLUMNA DE LA CELDA (0-BASED).
     * @return EL VALOR NUMÉRICO DE LA CELDA.
     */
    public double obtenerValorNumericoCelda(int fila, int columna) {
        NodoCelda nodo = obtenerCelda(fila, columna);
        if (nodo != null && nodo.getCelda() != null) {
            return nodo.getCelda().getValorNumerico();
        }
        return 0.0; // VALOR PREDETERMINADO SI LA CELDA NO EXISTE O ESTÁ VACÍA/NO NUMÉRICA.
    }

    /**
     * OBTIENE EL NOMBRE DE LA HOJA.
     *
     * @return EL NOMBRE DE LA HOJA.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * OBTIENE EL NÚMERO TOTAL DE FILAS EN LA HOJA.
     *
     * @return EL NÚMERO DE FILAS.
     */
    public int getFilas() {
        return filas;
    }

    /**
     * OBTIENE EL NÚMERO TOTAL DE COLUMNAS EN LA HOJA.
     *
     * @return EL NÚMERO DE COLUMNAS.
     */
    public int getColumnas() {
        return columnas;
    }
}
