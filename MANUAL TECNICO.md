# Manual Técnico - Proyecto Final de Programación

Este documento proporciona una visión técnica detallada del sistema de hoja de cálculo desarrollado en Java.

## Objetivo del Proyecto
El objetivo principal es simular una hoja de cálculo personalizada que permita al usuario crear y modificar celdas, agrupadas en hojas, usando una estructura interna basada en tabla hash.

## Arquitectura General
Se implementa el patrón Modelo-Vista-Controlador (MVC):

- **Modelo**: Contiene las estructuras lógicas (`Libro`, `Hoja`, `Celda`, `nodos`, `tabla hash`).
- **Vista**: Interfaces gráficas desarrolladas con Swing.
- **Controlador**: Clases que gestionan la comunicación entre vista y modelo.

## Estructura de Código

```
├── Libro.java
├── Hoja.java
├── Celda.java
├── NodoHoja.java
├── NodoCelda.java
├── TablaHashPersonalizada.java
├── VistaPrincipal.java
├── VistaTablaHash.java
├── ControladorPrincipal.java
├── ControladorTablaHash.java
├── UtilidadesCelda.java
└── PROYECTOFINAL_PROGRA.java
```

## Descripción de Componentes

- **`PROYECTOFINAL_PROGRA.java`**  
  Punto de entrada de la aplicación. Inicializa la interfaz principal.

- **Modelo**  
  - `Libro`, `Hoja`, `Celda`: Representan la jerarquía lógica.
  - `NodoHoja`, `NodoCelda`: Estructuras enlazadas.
  - `TablaHashPersonalizada`: Implementación de tabla hash para almacenar celdas.

- **Vista**  
  - `VistaPrincipal`: Interfaz principal de la aplicación.
  - `VistaTablaHash`: Interfaz específica para interactuar con la tabla hash.

- **Controlador**  
  - `ControladorPrincipal`: Maneja eventos desde `VistaPrincipal`.
  - `ControladorTablaHash`: Procesa la lógica vinculada a la tabla hash.

- **Utilidades**  
  - `UtilidadesCelda`: Métodos auxiliares para operaciones con celdas.

## Flujo de Ejecución

1. El usuario ejecuta `PROYECTOFINAL_PROGRA.java`, lo que lanza la `VistaPrincipal`.
2. El controlador principal se encarga de gestionar las acciones del usuario.
3. Cuando se interactúa con hojas y celdas, se hace uso del `ControladorTablaHash`.
4. Los datos se almacenan y recuperan usando `TablaHashPersonalizada`, lo que permite eficiencia y organización.

## Diseño Interno

- Las hojas contienen celdas almacenadas mediante una tabla hash.
- Las celdas están enlazadas a través de nodos, lo que permite iteración.
- El modelo de datos permite expandirse fácilmente a otras operaciones como fórmulas o formatos.

## Recomendaciones para el Mantenimiento

- Seguir principios de programación orientada a objetos (encapsulamiento, cohesión).
- Documentar métodos críticos dentro del código fuente.
- Separar responsabilidades de validación, visualización y lógica para futuras mejoras.

## Posibles Mejoras Futuras
- Exportar/Importar hojas de cálculo a archivos.
- Soporte para operaciones matemáticas entre celdas.
- Mejoras en la interfaz (tema oscuro, validación visual de entradas).

## Créditos
Desarrollado por Ruben García y Carlos Cachin
Junio 2025
