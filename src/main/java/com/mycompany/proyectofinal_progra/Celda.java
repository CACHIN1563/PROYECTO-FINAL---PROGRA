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
/**
 * Representa una celda individual en la hoja de cálculo.
 * Almacena el valor calculado (o literal) y la fórmula original si existe.
 */

import java.io.Serializable; // IMPORTAR LA INTERFAZ SERIALIZABLE.

/**
 * REPRESENTA UNA CELDA INDIVIDUAL EN LA HOJA DE CÁLCULO.
 * ALMACENA EL VALOR MOSTRADO, LA FÓRMULA ORIGINAL Y SUS COORDENADAS.
 */
public class Celda implements Serializable {
    private static final long serialVersionUID = 1L; // PARA CONTROL DE VERSIONES DE SERIALIZACIÓN.

    private String valor;    // ALMACENA EL VALOR CALCULADO O TEXTO LITERAL PARA MOSTRAR.
    private String formula;  // ALMACENA LA CADENA DE LA FÓRMULA ORIGINAL (EJ. "f(x):=SUMA((1,1),(2,2))").
    private int fila, columna; // ÍNDICES DE FILA Y COLUMNA DE LA CELDA (0-BASED).

    /**
     * CONSTRUCTOR PARA CREAR UNA NUEVA CELDA CON SUS COORDENADAS.
     * INICIALIZA EL VALOR Y LA FÓRMULA COMO CADENAS VACÍAS.
     *
     * @param fila EL ÍNDICE DE FILA DE LA CELDA (0-BASED).
     * @param columna EL ÍNDICE DE COLUMNA DE LA CELDA (0-BASED).
     */
    public Celda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.valor = "";    // VALOR PREDETERMINADO VACÍO.
        this.formula = ""; // FÓRMULA PREDETERMINADA VACÍA.
    }

    /**
     * OBTIENE EL VALOR ACTUAL DE LA CELDA (EL VALOR CALCULADO O TEXTO LITERAL).
     *
     * @return EL VALOR DE LA CELDA COMO STRING.
     */
    public String getValor() {
        return valor;
    }

    /**
     * ESTABLECE EL VALOR MOSTRADO DE LA CELDA.
     *
     * @param valor EL NUEVO VALOR A ESTABLECER.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * OBTIENE LA FÓRMULA ORIGINAL ALMACENADA EN LA CELDA.
     *
     * @return LA FÓRMULA COMO STRING, O UNA CADENA VACÍA SI NO HAY FÓRMULA PRESENTE.
     */
    public String getFormula() {
        return formula;
    }

    /**
     * ESTABLECE LA FÓRMULA ORIGINAL PARA LA CELDA.
     *
     * @param formula LA FÓRMULA A ALMACENAR.
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * OBTIENE EL ÍNDICE DE FILA DE LA CELDA.
     *
     * @return EL NÚMERO DE FILA (ÍNDICE 0-BASED).
     */
    public int getFila() {
        return fila;
    }

    /**
     * OBTIENE EL ÍNDICE DE COLUMNA DE LA CELDA.
     *
     * @return EL NÚMERO DE COLUMNA (ÍNDICE 0-BASED).
     */
    public int getColumna() {
        return columna;
    }

    /**
     * OBTIENE EL VALOR NUMÉRICO DE LA CELDA.
     * INTENTA PARSEAR EL VALOR A UN DOUBLE. SI NO ES UN NÚMERO VÁLIDO, DEVUELVE 0.0.
     *
     * @return EL VALOR NUMÉRICO DE LA CELDA.
     */
    public double getValorNumerico() {
        try {
            // INTENTA PARSEAR EL VALOR ACTUAL DE LA CELDA COMO UN NÚMERO.
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            // SI NO ES UN NÚMERO VÁLIDO, DEVUELVE 0.0 Y PUEDE IMPRIMIR UN MENSAJE DE ADVERTENCIA.
            // SYSTEM.ERR.PRINTLN("ADVERTENCIA: VALOR DE CELDA NO NUMÉRICO ('" + valor + "') EN (" + (fila + 1) + "," + (columna + 1) + "). RETORNANDO 0.0.");
            return 0.0;
        }
    }
}