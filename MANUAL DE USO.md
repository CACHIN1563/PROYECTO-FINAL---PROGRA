# Proyecto Final - Programación Orientada a Objetos

Aplicación de escritorio desarrollada en Java que simula una hoja de cálculo personalizada utilizando una estructura de almacenamiento basada en tablas hash. Está construida con arquitectura MVC, permitiendo una clara separación entre lógica, interfaz y controladores.

## Características
- Interfaz gráfica intuitiva desarrollada con Swing
- Gestión de múltiples hojas y celdas
- Estructura de almacenamiento eficiente usando tabla hash
- Patrón de arquitectura Modelo-Vista-Controlador (MVC)

## Requisitos
- Java Development Kit (JDK) 8 o superior
- IDE recomendado: IntelliJ IDEA o NetBeans
- Sistema operativo: Windows, Linux o macOS

## Instrucciones de Ejecución
1. Clona el repositorio:
   ```bash
   git clone https://github.com/usuario/proyectofinal_progra.git
   ```
2. Abre el proyecto en tu IDE de preferencia.
3. Ejecuta el archivo `PROYECTOFINAL_PROGRA.java`.

## Estructura General
El sistema está organizado en tres capas principales: modelo, vista y controlador.

```
├── Modelo:
│   ├── Libro.java
│   ├── Hoja.java
│   ├── Celda.java
│   ├── NodoHoja.java
│   └── NodoCelda.java
├── Vista:
│   ├── VistaPrincipal.java
│   └── VistaTablaHash.java
├── Controlador:
│   ├── ControladorPrincipal.java
│   └── ControladorTablaHash.java
├── Estructura:
│   └── TablaHashPersonalizada.java
├── Utilidades:
│   └── UtilidadesCelda.java
└── Main:
    └── PROYECTOFINAL_PROGRA.java
```

## Autores
Carlos Cachin
Ruben Gerardo García
Junio 2025

## Licencia
Proyecto académico. Su uso y modificación están permitidos con la debida atribución.
