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
/**
 * CLASE DE UTILIDAD PARA CONVERTIR REFERENCIAS DE CELDA ESTILO EXCEL (EJ. "A1", "B5")
 * A COORDENADAS DE FILA Y COLUMNA BASADAS EN 0, Y VICEVERSA PARA NOMBRES DE COLUMNA.
 */
public class UtilidadesCelda {

    /**
     * CONVIERTE UNA REFERENCIA DE CELDA ESTILO EXCEL (EJ. "A1", "B5") A UN ARRAY DE ENTEROS
     * QUE CONTIENE [FILA, COLUMNA] (AMBOS 0-BASED).
     *
     * @param referencia LA CADENA DE REFERENCIA DE LA CELDA (EJ. "A1").
     * @return UN ARRAY INT[] DONDE [0] ES LA FILA (0-BASED) Y [1] ES LA COLUMNA (0-BASED).
     * @throws IllegalArgumentException SI LA REFERENCIA NO TIENE UN FORMATO VÁLIDO.
     */
    public static int[] parseReferenciaCelda(String referencia) throws IllegalArgumentException {
        if (referencia == null || referencia.trim().isEmpty()) {
            throw new IllegalArgumentException("LA REFERENCIA DE CELDA NO PUEDE ESTAR VACÍA.");
        }
        referencia = referencia.trim().toUpperCase(); // LIMPIAR ESPACIOS Y CONVERTIR A MAYÚSCULAS.

        int columna = 0;
        int fila = 0;
        int i = 0; // ÍNDICE PARA RECORRER LA CADENA.

        // PARSEAR LA PARTE DE LA COLUMNA (LETRAS).
        for (i = 0; i < referencia.length(); i++) {
            char c = referencia.charAt(i);
            if (Character.isLetter(c)) {
                // CALCULAR EL VALOR DE LA COLUMNA (BASE 26, COMO LOS NÚMEROS ROMANOS).
                columna = columna * 26 + (c - 'A' + 1);
            } else {
                break; // SE ENCONTRÓ UN NÚMERO, TERMINAR LA PARTE DE LAS LETRAS.
            }
        }
        columna--; // CONVERTIR A 0-BASED (A=0, B=1, etc.).

        // PARSEAR LA PARTE DE LA FILA (NÚMEROS).
        if (i < referencia.length()) {
            try {
                fila = Integer.parseInt(referencia.substring(i));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("NÚMERO DE FILA INVÁLIDO EN REFERENCIA: " + referencia.substring(i));
            }
        } else {
            throw new IllegalArgumentException("REFERENCIA DE CELDA INVÁLIDA: " + referencia + ". DEBE CONTENER AL MENOS UNA FILA NUMÉRICA.");
        }
        fila--; // CONVERTIR A 0-BASED (1=0, 2=1, etc.).

        // VALIDAR QUE SE HAYAN PARSEADO TANTO LETRAS COMO NÚMEROS.
        if (i == 0 || i == referencia.length()) {
             throw new IllegalArgumentException("FORMATO DE REFERENCIA DE CELDA INVÁLIDO: " + referencia + ". FORMATO ESPERADO: A1, B5, ETC.");
        }

        return new int[]{fila, columna};
    }

    /**
     * GENERA EL NOMBRE DE LA COLUMNA ESTILO EXCEL (A, B, ..., Z, AA, AB, ...).
     * ESTE MÉTODO ES EL QUE USA VISTAPRINCIPAL PARA LOS ENCABEZADOS DE LA TABLA.
     *
     * @param column EL ÍNDICE DE LA COLUMNA (0-BASED).
     * @return EL NOMBRE DE LA COLUMNA (EJ. "A", "B", "AA").
     */
    public static String getExcelColumnName(int column) {
        StringBuilder columnName = new StringBuilder();
        int dividend = column + 1; // CONVERTIR A 1-BASED PARA EL CÁLCULO.
        while (dividend > 0) {
            int modulo = (dividend - 1) % 26;
            columnName.append((char) ('A' + modulo));
            dividend = (dividend - modulo) / 26;
        }
        return columnName.reverse().toString(); // INVERTIR LA CADENA PARA OBTENER EL ORDEN CORRECTO.
    }
}
