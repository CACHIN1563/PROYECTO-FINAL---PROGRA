/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectofinal_progra;

/**
 *
 * @author cachi
 */

/**
 * Clase principal que inicia la aplicación de hoja de cálculo.
 * Configura el modelo de datos (Libro), la vista (VistaPrincipal)
 * y el controlador (ControladorPrincipal) utilizando el patrón MVC.
 */

import javax.swing.SwingUtilities; // IMPORTAR LA CLASE SWINGUTILITIES PARA MANEJO DE HILOS DE UI.

/**
 * CLASE PRINCIPAL QUE INICIA LA APLICACIÓN DE HOJA DE CÁLCULO.
 * CONFIGURA EL MODELO DE DATOS (LIBRO), LA VISTA (VISTAPRINCIPAL)
 * Y EL CONTROLADOR (CONTROLADORPRINCIPAL) UTILIZANDO EL PATRÓN MVC.
 */
public class PROYECTOFINAL_PROGRA {
    /**
     * MÉTODO PRINCIPAL (MAIN) QUE SIRVE COMO PUNTO DE ENTRADA DE LA APLICACIÓN.
     * INICIA LA APLICACIÓN EN EL HILO DE DESPACHO DE EVENTOS DE SWING PARA GARANTIZAR
     * LA SEGURIDAD DEL HILO EN LAS OPERACIONES DE UI.
     *
     * @param args ARGUMENTOS DE LÍNEA DE COMANDOS (NO UTILIZADOS EN ESTA APLICACIÓN).
     */
    public static void main(String[] args) {
        // ASEGURAR QUE LA CREACIÓN Y MANIPULACIÓN DE LA UI SE HAGA EN EL HILO DE EVENTOS DE SWING.
        SwingUtilities.invokeLater(() -> {
            System.out.println("INICIO DE LA APLICACIÓN SWING."); // PRIMER PUNTO DE CONTROL.
            final int NUM_FILAS = 1000;    // NÚMERO PREDETERMINADO DE FILAS PARA LAS NUEVAS HOJAS.
            final int NUM_COLUMNAS = 100; // NÚMERO PREDETERMINADO DE COLUMNAS PARA LAS NUEVAS HOJAS.

            Libro libro = new Libro(); // CREAR UNA NUEVA INSTANCIA DEL MODELO (LIBRO).
            System.out.println("LIBRO INSTANCIADO."); // SEGUNDO PUNTO DE CONTROL.
            libro.agregarHoja("Hoja 1", NUM_FILAS, NUM_COLUMNAS); // AGREGAR UNA HOJA INICIAL.
            System.out.println("HOJA 1 AGREGADA AL LIBRO."); // TERCER PUNTO DE CONTROL.

            VistaPrincipal vista = new VistaPrincipal(); // CREAR UNA NUEVA INSTANCIA DE LA VISTA.
            System.out.println("VISTA INSTANCIADA."); // CUARTO PUNTO DE CONTROL.
            ControladorPrincipal controlador = new ControladorPrincipal(libro, vista); // CREAR Y VINCULAR EL CONTROLADOR.
            System.out.println("CONTROLADOR INSTANCIADO Y VINCULADO."); // QUINTO PUNTO DE CONTROL.
            vista.setVisible(true); // HACER VISIBLE LA VENTANA PRINCIPAL DE LA APLICACIÓN.
            System.out.println("VISTA HECHA VISIBLE. SI NO APARECE, EL PROBLEMA PUEDE SER DE RENDERING."); // SEXTO PUNTO DE CONTROL.
        });
    }
}

    
