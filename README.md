# 📊 Hoja de Cálculo

Este proyecto es una implementación de una **hoja de cálculo tipo Excel** desarrollada en Java, que incluye funcionalidades avanzadas como el uso de una matriz ortogonal para la gestión de celdas, soporte para múltiples hojas de cálculo y la capacidad de realizar operaciones básicas como suma y multiplicación. Además, como característica destacada, integra una **tabla hash personalizada** para la generación y visualización de índices de datos.

El programa sigue el patrón de diseño **Modelo-Vista-Controlador (MVC)**, asegurando una separación clara de las responsabilidades y facilitando la modularidad y el mantenimiento del código.

## ✨ Características Principales

* **Hoja de Cálculo Completa:** Interfaz de usuario intuitiva similar a la de programas de hoja de cálculo populares.
* **Matriz Ortogonal:** Almacenamiento eficiente de datos de celdas utilizando una estructura de matriz ortogonal con nodos personalizados (`NodoCelda`).
* **Patrón MVC:** Implementación robusta del patrón Modelo-Vista-Controlador para una arquitectura escalable y mantenible.
* **Operaciones Básicas:** Soporte para fórmulas de `SUMA` y `MULTIPLICAR` sobre rangos de celdas.
* **Múltiples Hojas:** Posibilidad de crear y gestionar múltiples hojas de cálculo (`Hoja`) dentro de un mismo "libro" (`Libro`), organizadas mediante una lista enlazada personalizada.
* **Persistencia de Datos (Opcional):** Funcionalidad para guardar y cargar el estado completo del libro de cálculo en el disco duro, permitiendo reanudar el trabajo.
* **Tabla Hash Personalizada:**
    * Generación y visualización de tablas hash a partir de datos ingresados.
    * Implementación de un algoritmo hash personalizado (no se utilizan las clases `HashMap` estándar de Java, sino una lógica de hash y manejo de colisiones propia).
    * Interfaz dedicada para la gestión y visualización de la tabla hash, accesible desde el menú principal.

## 🚀 Tecnologías Utilizadas

* **Java:** Lenguaje de programación principal.
* **Swing:** Framework para la construcción de la interfaz gráfica de usuario (GUI).


