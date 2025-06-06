/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal_progra;

/**
 *
 * @author cachi
 */

import java.io.Serializable;

/**
 * Representa un libro de hojas de cálculo, que contiene múltiples hojas.
 * Las hojas se gestionan mediante una lista enlazada personalizada.
 */
public class Libro implements Serializable {
    private static final long serialVersionUID = 1L;

    private NodoHoja cabeza; // El primer nodo de la lista enlazada de hojas
    private Hoja hojaActual; // La hoja actualmente activa
    private int numeroHojas; // Contador de hojas

    /**
     * Constructor para crear un nuevo libro.
     */
    public Libro() {
        this.cabeza = null;
        this.hojaActual = null;
        this.numeroHojas = 0;
    }

    /**
     * Agrega una nueva hoja al final de la lista enlazada.
     * Si es la primera hoja, se convierte en la hoja actual.
     * @param nombre El nombre de la nueva hoja.
     * @param filas El número de filas de la hoja.
     * @param columnas El número de columnas de la hoja.
     */
    public void agregarHoja(String nombre, int filas, int columnas) {
        Hoja nuevaHoja = new Hoja(nombre, filas, columnas);
        NodoHoja nuevoNodo = new NodoHoja(nuevaHoja);

        if (cabeza == null) {
            cabeza = nuevoNodo;
            hojaActual = nuevaHoja; // La primera hoja se convierte en la actual
        } else {
            NodoHoja temp = cabeza;
            while (temp.getSiguiente() != null) {
                temp = temp.getSiguiente();
            }
            temp.setSiguiente(nuevoNodo);
        }
        numeroHojas++;
    }

    /**
     * Obtiene una hoja por su nombre.
     * @param nombre El nombre de la hoja a buscar.
     * @return La hoja encontrada, o null si no existe.
     */
    public Hoja obtenerHojaPorNombre(String nombre) {
        NodoHoja actual = cabeza;
        while (actual != null) {
            if (actual.getHoja().getNombre().equals(nombre)) {
                return actual.getHoja();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    /**
     * Elimina una hoja por su nombre de la lista enlazada.
     * Si la hoja eliminada era la hoja actual, se establece la siguiente hoja como actual,
     * o null si no quedan hojas.
     * @param nombreHoja El nombre de la hoja a eliminar.
     * @return true si la hoja fue eliminada, false en caso contrario.
     */
    public boolean eliminarHoja(String nombreHoja) {
        if (cabeza == null) {
            return false; // No hay hojas para eliminar
        }

        // Caso especial: eliminar la cabeza
        if (cabeza.getHoja().getNombre().equals(nombreHoja)) {
            cabeza = cabeza.getSiguiente();
            numeroHojas--;
            if (hojaActual != null && hojaActual.getNombre().equals(nombreHoja)) {
                hojaActual = (cabeza != null) ? cabeza.getHoja() : null; // Establecer la nueva hoja actual
            }
            return true;
        }

        NodoHoja actual = cabeza;
        while (actual.getSiguiente() != null && !actual.getSiguiente().getHoja().getNombre().equals(nombreHoja)) {
            actual = actual.getSiguiente();
        }

        if (actual.getSiguiente() != null) {
            // Se encontró el nodo a eliminar
            if (hojaActual != null && hojaActual.getNombre().equals(nombreHoja)) {
                hojaActual = actual.getSiguiente().getSiguiente() != null ? actual.getSiguiente().getSiguiente().getHoja() : actual.getHoja();
                // Si la hoja eliminada era la última, la nueva actual es la anterior,
                // si no hay anterior, la nueva actual es null.
                if (hojaActual.getNombre().equals(nombreHoja) && actual.getSiguiente().getSiguiente() == null && actual == cabeza){
                     hojaActual = null; // Si solo queda una hoja y es la que se elimina, no hay hoja actual
                } else if (hojaActual.getNombre().equals(nombreHoja) && actual.getSiguiente().getSiguiente() == null){
                    hojaActual = actual.getHoja(); // Si la hoja eliminada era la última, la nueva actual es la anterior
                } else if (hojaActual.getNombre().equals(nombreHoja) && actual.getSiguiente().getSiguiente() != null){
                    hojaActual = actual.getSiguiente().getHoja(); // Si la hoja eliminada era la última, la nueva actual es la anterior
                }
            }
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
            numeroHojas--;
            // Asegurarse de que hojaActual no sea la eliminada y se actualice si es necesario
            if (this.hojaActual == null && this.cabeza != null) {
                 this.hojaActual = this.cabeza.getHoja();
            } else if (this.hojaActual != null && this.hojaActual.getNombre().equals(nombreHoja) && this.cabeza == null) {
                this.hojaActual = null; // No quedan hojas
            } else if (this.hojaActual != null && this.hojaActual.getNombre().equals(nombreHoja)) {
                 // Si la hoja actual era la eliminada, y hay otras hojas, establece la siguiente como actual.
                 // Si no hay siguiente (era la última), la hoja actual debe ser la anterior o null si ya no hay más.
                 int tabIndex = 0; // Simulate finding next valid tab index
                 if (cabeza != null) {
                    NodoHoja temp = cabeza;
                    int i = 0;
                    while(temp != null) {
                        if (temp.getHoja().getNombre().equals(nombreHoja)) {
                            if(temp.getSiguiente() != null) {
                                hojaActual = temp.getSiguiente().getHoja();
                            } else if (i > 0) {
                                // Find previous
                                NodoHoja prev = cabeza;
                                while(prev.getSiguiente() != null && !prev.getSiguiente().getHoja().getNombre().equals(nombreHoja)) {
                                    prev = prev.getSiguiente();
                                }
                                if(prev != null) hojaActual = prev.getHoja();
                            } else {
                                hojaActual = null; // No more sheets
                            }
                            break;
                        }
                        temp = temp.getSiguiente();
                        i++;
                    }
                 } else {
                     hojaActual = null;
                 }
            }
            return true;
        }
        return false; // Hoja no encontrada
    }


    /**
     * Obtiene la hoja actualmente activa.
     * @return La Hoja activa.
     */
    public Hoja getHojaActual() {
        return hojaActual;
    }

    /**
     * Establece la hoja actual.
     * @param hojaActual La hoja a establecer como actual.
     */
    public void setHojaActual(Hoja hojaActual) {
        this.hojaActual = hojaActual;
    }

    /**
     * Obtiene el número total de hojas en el libro.
     * @return El número de hojas.
     */
    public int getNumeroHojas() {
        return numeroHojas;
    }

    /**
     * Establece la cabeza de la lista enlazada de hojas.
     * Este método es útil para cargar un Libro serializado.
     * @param cabeza El nuevo NodoHoja cabeza.
     */
    public void setCabeza(NodoHoja cabeza) {
        this.cabeza = cabeza;
        this.numeroHojas = 0; // Recalcular número de hojas
        NodoHoja actual = cabeza;
        while(actual != null) {
            this.numeroHojas++;
            actual = actual.getSiguiente();
        }
        if (this.cabeza != null) {
            this.hojaActual = this.cabeza.getHoja(); // Establecer la primera hoja como actual al cargar
        } else {
            this.hojaActual = null;
        }
    }

    /**
     * Obtiene el nodo cabeza de la lista enlazada de hojas.
     * @return El NodoHoja cabeza.
     */
    public NodoHoja getCabeza() {
        return cabeza;
    }
}