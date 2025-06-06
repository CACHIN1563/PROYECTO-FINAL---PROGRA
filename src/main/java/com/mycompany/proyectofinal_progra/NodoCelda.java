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
 * Representa un nodo en la matriz ortogonal de celdas dentro de una Hoja.
 * Cada nodo contiene una Celda y referencias a los nodos adyacentes (derecha y abajo).
 */

import java.io.Serializable; // IMPORTAR LA INTERFAZ SERIALIZABLE.

/**
 * REPRESENTA UN NODO EN LA MATRIZ ORTOGONAL DE CELDAS DENTRO DE UNA HOJA.
 * CADA NODO CONTIENE UNA CELDA Y REFERENCIAS A LOS NODOS ADYACENTES (DERECHA Y ABAJO).
 */
public class NodoCelda implements Serializable {
    private static final long serialVersionUID = 1L; // PARA CONTROL DE VERSIONES DE SERIALIZACIÓN.

    private Celda celda;       // LA CELDA DE DATOS QUE ESTE NODO CONTIENE.
    private NodoCelda derecha; // REFERENCIA AL NODO A LA DERECHA.
    private NodoCelda abajo;   // REFERENCIA AL NODO DE ABAJO.

    /**
     * CONSTRUCTOR PARA CREAR UN NUEVO NODO DE CELDA.
     * INICIALIZA LAS REFERENCIAS DERECHA Y ABAJO COMO NULL.
     *
     * @param celda EL OBJETO CELDA QUE ESTE NODO VA A ENCAPSULAR.
     */
    public NodoCelda(Celda celda) {
        this.celda = celda;
        this.derecha = null;
        this.abajo = null;
    }

    /**
     * OBTIENE EL OBJETO CELDA ENCAPSULADO EN ESTE NODO.
     *
     * @return EL OBJETO CELDA.
     */
    public Celda getCelda() {
        return celda;
    }

    /**
     * OBTIENE EL NODO ADYACENTE A LA DERECHA.
     *
     * @return EL NODOCELDA A LA DERECHA.
     */
    public NodoCelda getDerecha() {
        return derecha;
    }

    /**
     * ESTABLECE EL NODO ADYACENTE A LA DERECHA.
     *
     * @param derecha EL NODOCELDA A ESTABLECER COMO SIGUIENTE A LA DERECHA.
     */
    public void setDerecha(NodoCelda derecha) {
        this.derecha = derecha;
    }

    /**
     * OBTIENE EL NODO ADYACENTE DE ABAJO.
     *
     * @return EL NODOCELDA DE ABAJO.
     */
    public NodoCelda getAbajo() {
        return abajo;
    }

    /**
     * ESTABLECE EL NODO ADYACENTE DE ABAJO.
     *
     * @param abajo EL NODOCELDA A ESTABLECER COMO SIGUIENTE ABAJO.
     */
    public void setAbajo(NodoCelda abajo) {
        this.abajo = abajo;
    }
}