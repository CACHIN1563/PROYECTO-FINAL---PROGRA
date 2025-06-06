/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal_progra;

/**
 *
 * @author cachi
 */


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.Serializable; // IMPORTAR LA INTERFAZ SERIALIZABLE.

/**
 * IMPLEMENTACIÓN DE UNA TABLA HASH PERSONALIZADA CON ENCADENAMIENTO SEPARADO.
 * ALMACENA PARES CLAVE-VALOR (EN ESTE CASO, EL DATO Y SU ÍNDICE HASH).
 * CADA "CUBO" (BUCKET) DE LA TABLA ES UNA LISTA ENLAZADA DE ENTRADAS HASH.
 */
public class TablaHashPersonalizada implements Serializable {
    private static final long serialVersionUID = 1L; // PARA CONTROL DE VERSIONES DE SERIALIZACIÓN.

    private ArrayList<LinkedList<EntradaHash>> tabla; // LA TABLA HASH (ARRAY DE LISTAS ENLAZADAS).
    private int tamano; // TAMAÑO DE LA TABLA (NÚMERO DE CUBETAS/BUCKETS).

    /**
     * CLASE ANIDADA ESTÁTICA QUE REPRESENTA UNA ENTRADA EN LA TABLA HASH.
     * CONTIENE LA CLAVE (EL DATO) Y EL ÍNDICE HASH CALCULADO PARA ELLA.
     */
    static class EntradaHash implements Serializable {
        private static final long serialVersionUID = 1L; // PARA CONTROL DE VERSIONES DE SERIALIZACIÓN.
        String clave; // EL DATO ALMACENADO (EN ESTE CASO, EL VALOR DE LA CELDA A).
        int indiceHash; // EL ÍNDICE HASH CALCULADO PARA ESTA CLAVE.

        /**
         * CONSTRUCTOR PARA CREAR UNA NUEVA ENTRADA HASH.
         *
         * @param clave EL DATO A ALMACENAR.
         * @param indiceHash EL ÍNDICE EN EL QUE SE UBICARÁ ESTA ENTRADA.
         */
        public EntradaHash(String clave, int indiceHash) {
            this.clave = clave;
            this.indiceHash = indiceHash;
        }

        /**
         * OBTIENE LA CLAVE (DATO) DE ESTA ENTRADA.
         *
         * @return LA CLAVE COMO STRING.
         */
        public String getClave() {
            return clave;
        }

        /**
         * OBTIENE EL ÍNDICE HASH DE ESTA ENTRADA.
         *
         * @return EL ÍNDICE HASH.
         */
        public int getIndiceHash() {
            return indiceHash;
        }
    }

    /**
     * CONSTRUCTOR PARA LA TABLA HASH.
     * INICIALIZA LA TABLA CON EL TAMAÑO ESPECIFICADO.
     *
     * @param tamano EL TAMAÑO INICIAL DE LA TABLA HASH (NÚMERO DE CUBETAS).
     */
    public TablaHashPersonalizada(int tamano) {
        this.tamano = tamano;
        tabla = new ArrayList<>(tamano);
        for (int i = 0; i < tamano; i++) {
            tabla.add(new LinkedList<>()); // INICIALIZAR CADA CUBETA CON UNA LISTA ENLAZADA VACÍA.
        }
    }

    /**
     * FUNCIÓN DE HASH PERSONALIZADA.
     * CALCULA UN ÍNDICE HASH PARA UNA CADENA DADA.
     * UTILIZA UN ALGORITMO SIMPLE BASADO EN LA SUMA DE LOS VALORES ASCII DE LOS CARACTERES,
     * Y LUEGO EL MÓDULO DEL TAMAÑO DE LA TABLA PARA OBTENER EL ÍNDICE FINAL.
     *
     * @param dato LA CADENA DE ENTRADA PARA LA CUAL CALCULAR EL HASH.
     * @return EL ÍNDICE HASH CALCULADO.
     */
    private int funcionHash(String dato) {
        int hash = 0;
        for (char c : dato.toCharArray()) {
            hash = (hash * 31 + c) % tamano; // MULTIPLICACIÓN POR UN NÚMERO PRIMO (31) PARA MEJOR DISTRIBUCIÓN.
        }
        return Math.abs(hash); // ASEGURAR QUE EL ÍNDICE HASH SEA NO NEGATIVO.
    }

    /**
     * AGREGA UN DATO A LA TABLA HASH.
     * CALCULA SU ÍNDICE HASH Y LO INSERTA EN LA LISTA ENLAZADA CORRESPONDIENTE.
     * SI EL DATO YA EXISTE, NO LO AGREGA DE NUEVO (O PODRÍA SER MODIFICADO PARA ACTUALIZARLO).
     *
     * @param dato EL DATO (STRING) A AGREGAR.
     */
    public void agregar(String dato) {
        int indice = funcionHash(dato);
        LinkedList<EntradaHash> cubeta = tabla.get(indice);

        // OPCIONAL: VERIFICAR SI YA EXISTE PARA EVITAR DUPLICADOS.
        for (EntradaHash entrada : cubeta) {
            if (entrada.getClave().equals(dato)) {
                // SI EL DATO YA EXISTE, SE PODRÍA ACTUALIZAR O SIMPLEMENTE NO HACER NADA.
                // EN ESTE CASO, NO HAREMOS NADA (ASUMIMOS QUE YA ESTÁ).
                return;
            }
        }
        cubeta.add(new EntradaHash(dato, indice)); // AGREGAR NUEVA ENTRADA A LA CUBETA.
    }

    /**
     * BUSCA UN DATO EN LA TABLA HASH.
     *
     * @param dato EL DATO A BUSCAR.
     * @return LA ENTRADA HASH SI SE ENCUENTRA, O NULL SI NO ESTÁ PRESENTE.
     */
    public EntradaHash buscar(String dato) {
        int indice = funcionHash(dato);
        LinkedList<EntradaHash> cubeta = tabla.get(indice);
        for (EntradaHash entrada : cubeta) {
            if (entrada.getClave().equals(dato)) {
                return entrada; // ENCONTRADO.
            }
        }
        return null; // NO ENCONTRADO.
    }

    /**
     * ELIMINA UN DATO DE LA TABLA HASH.
     *
     * @param dato EL DATO A ELIMINAR.
     * @return TRUE SI SE ELIMINÓ CORRECTAMENTE, FALSE SI NO SE ENCONTRÓ.
     */
    public boolean eliminar(String dato) {
        int indice = funcionHash(dato);
        LinkedList<EntradaHash> cubeta = tabla.get(indice);
        EntradaHash aEliminar = null;
        for (EntradaHash entrada : cubeta) {
            if (entrada.getClave().equals(dato)) {
                aEliminar = entrada;
                break;
            }
        }
        if (aEliminar != null) {
            cubeta.remove(aEliminar);
            return true;
        }
        return false;
    }

    /**
     * PREPARA LOS DATOS DE LA TABLA HASH PARA SER MOSTRADOS EN UNA JTABLE.
     * GENERA UNA LISTA DE ARRAYS DE STRINGS, DONDE CADA ARRAY REPRESENTA UNA FILA
     * Y CONTIENE EL ÍNDICE HASH Y EL VALOR ASOCIADO.
     *
     * @return UNA LISTA DE STRING ARRAYS, CADA UNO CON [ÍNDICE HASH, DATO].
     */
    public List<String[]> obtenerDatosParaVista() {
        List<String[]> datosVista = new ArrayList<>();
        for (int i = 0; i < tamano; i++) {
            LinkedList<EntradaHash> cubeta = tabla.get(i);
            if (cubeta.isEmpty()) {
                datosVista.add(new String[]{String.valueOf(i), "VACÍO"}); // MOSTRAR CUBETAS VACÍAS.
            } else {
                for (EntradaHash entrada : cubeta) {
                    datosVista.add(new String[]{String.valueOf(entrada.getIndiceHash()), entrada.getClave()});
                }
            }
        }
        return datosVista;
    }

    /**
     * OBTIENE EL TAMAÑO ACTUAL DE LA TABLA HASH (NÚMERO DE CUBETAS).
     *
     * @return EL TAMAÑO DE LA TABLA HASH.
     */
    public int getTamano() {
        return tamano;
    }
}