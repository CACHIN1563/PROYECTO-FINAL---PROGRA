# 📊 Hoja de Cálculo Avanzada con Tabla Hash Personalizada

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

## 📂 Estructura del Proyecto

El proyecto está organizado según el patrón MVC:

* **Modelo:**
    * `Libro.java`: Contiene las hojas de cálculo.
    * `Hoja.java`: Representa una hoja individual con su matriz ortogonal de celdas.
    * `Celda.java`: Define las propiedades de una celda (valor, fórmula, coordenadas).
    * `NodoCelda.java`: Nodos para la matriz ortogonal.
    * `NodoHoja.java`: Nodos para la lista enlazada de hojas.
    * `TablaHashPersonalizada.java`: Implementación de la tabla hash.
* **Vista:**
    * `VistaPrincipal.java`: Interfaz gráfica principal de la hoja de cálculo.
    * `VistaTablaHash.java`: Interfaz gráfica para la funcionalidad de la tabla hash.
* **Controlador:**
    * `ControladorPrincipal.java`: Maneja las interacciones del usuario en la hoja de cálculo y la lógica de negocio.
    * `ControladorTablaHash.java`: Maneja las interacciones del usuario para la tabla hash.
* **Utilidades:**
    * `UtilidadesCelda.java`: Clase de ayuda para el manejo de referencias de celdas.
* **Principal:**
    * `PROYECTOFINAL_PROGRA.java`: Clase principal para iniciar la aplicación.

## 🛠️ Cómo Ejecutar el Proyecto

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/TuUsuario/NombreDelRepositorio.git](https://github.com/TuUsuario/NombreDelRepositorio.git)
    cd NombreDelRepositorio
    ```
2.  **Abrir en un IDE:** Importa el proyecto en tu Entorno de Desarrollo Integrado (IDE) preferido (por ejemplo, Apache NetBeans, IntelliJ IDEA, Eclipse). Asegúrate de tener configurado un JDK (Java Development Kit) compatible (preferiblemente Java 8 o superior).
3.  **Compilar y Ejecutar:** Ejecuta la clase `PROYECTOFINAL_PROGRA.java`.

    * **En NetBeans:** Haz clic derecho en el proyecto y selecciona "Run".
    * **Desde la línea de comandos (después de compilar):**
        ```bash
        # Suponiendo que los archivos .class están en una carpeta 'build/classes'
        # Y que el paquete principal es 'com.mycompany.proyectofinal_progra'
        java -cp build/classes com.mycompany.proyectofinal_progra.PROYECTOFINAL_PROGRA
        ```
        *(Nota: Las rutas exactas pueden variar dependiendo de tu entorno de compilación y empaquetado.)*

## 🧑‍💻 Autor

* [Tu Nombre/Alias] - Estudiante de [Tu Universidad/Curso]

---
**Nota:** Este proyecto fue desarrollado como parte del "Proyecto IV" del curso de Programación III en la Universidad Mariano Gálvez.
