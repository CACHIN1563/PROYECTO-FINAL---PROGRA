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
 * Representa un nodo en la lista enlazada de hojas.
 * Cada nodo contiene una Hoja y una referencia al siguiente nodo.
 */
import java.io.Serializable; // IMPORTAR LA INTERFAZ SERIALIZABLE.

/**
 * REPRESENTA UN NODO EN LA LISTA ENLAZADA DE HOJAS.
 * CADA NODO CONTIENE UNA HOJA Y UNA REFERENCIA AL SIGUIENTE NODO.
 */
public class NodoHoja implements Serializable {
    private static final long serialVersionUID = 1L; // PARA CONTROL DE VERSIONES DE SERIALIZACIÓN.

    Hoja hoja; // LA HOJA ALMACENADA EN ESTE NODO.
    NodoHoja siguiente; // REFERENCIA AL SIGUIENTE NODO EN LA LISTA.

    /**
     * CONSTRUCTOR PARA CREAR UN NUEVO NODO CON UNA HOJA.
     * INICIALIZA LA REFERENCIA 'SIGUIENTE' COMO NULL.
     *
     * @param hoja LA HOJA A ALMACENAR EN EL NODO.
     */
    public NodoHoja(Hoja hoja) {
        this.hoja = hoja;
        this.siguiente = null; // INICIALMENTE, NO HAY SIGUIENTE NODO.
    }

    /**
     * OBTIENE LA HOJA ALMACENADA EN ESTE NODO.
     *
     * @return LA HOJA DEL NODO.
     */
    public Hoja getHoja() {
        return hoja;
    }

    /**
     * OBTIENE EL SIGUIENTE NODO EN LA LISTA ENLAZADA.
     *
     * @return EL SIGUIENTE NODOHOJA, O NULL SI ES EL ÚLTIMO NODO.
     */
    public NodoHoja getSiguiente() {
        return siguiente;
    }

    /**
     * ESTABLECE EL SIGUIENTE NODO EN LA LISTA ENLAZADA.
     *
     * @param siguiente EL NODOHOJA A ESTABLECER COMO SIGUIENTE.
     */
    public void setSiguiente(NodoHoja siguiente) {
        this.siguiente = siguiente;
    }
}