/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal_progra;

/**
 *
 * @author cachi
 */

    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;
    import javax.swing.table.DefaultTableCellRenderer;
    import java.awt.*;
    import java.util.HashMap;
    import java.util.Map;
    import javax.swing.table.JTableHeader;
    import javax.swing.table.TableColumnModel;
    import javax.swing.ListSelectionModel;

    /**
    * Representa la interfaz de usuario principal de la hoja de cálculo.
    * Contiene la tabla, la barra de fórmulas y los elementos de menú.
    */
    public class VistaPrincipal extends JFrame {
        public JTabbedPane tabbedPane;
        public Map<String, DefaultTableModel> modelosTablaPorHoja;
        public JTextField campoFormula;
        public JButton aplicarFormulaBoton;
        public JMenuBar menuBar;
        
        // Declaración de los elementos de menú para que sean accesibles desde el controlador
        public JMenuItem itemNuevaHoja;
        public JMenuItem itemEliminarHoja; // NUEVO: para eliminar hojas
        public JMenuItem itemGuardar;
        public JMenuItem itemAbrir;
        public JMenuItem itemTablaHash; 
        public JMenuItem itemDesarrolladoPor; // NUEVO: para la opción "Desarrollado por"

        public VistaPrincipal() {
            setTitle("Hoja de Cálculo");
            setSize(800, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null); // Centrar la ventana

            modelosTablaPorHoja = new HashMap<>();

            // Configuración de la barra de fórmulas
            campoFormula = new JTextField("Ingrese aquí la fórmula para la celda actual o un valor");
            aplicarFormulaBoton = new JButton("APLICAR");

            JPanel formulaPanel = new JPanel(new BorderLayout());
            formulaPanel.add(campoFormula, BorderLayout.CENTER);
            formulaPanel.add(aplicarFormulaBoton, BorderLayout.EAST);

            // Configuración del JTabbedPane para las hojas
            tabbedPane = new JTabbedPane();
            
            // Configuración de la barra de menú
            menuBar = new JMenuBar();
            
            // Crear el menú "Archivo"
            JMenu menuArchivo = new JMenu("Archivo");
            
            // Inicializar los JMenuItem
            itemNuevaHoja = new JMenuItem("Nueva Hoja");
            itemEliminarHoja = new JMenuItem("Eliminar Hoja"); // Inicializar el nuevo item
            itemGuardar = new JMenuItem("Guardar");
            itemAbrir = new JMenuItem("Abrir");
            
            // Añadir los ítems al menú "Archivo"
            menuArchivo.add(itemNuevaHoja);
            menuArchivo.add(itemEliminarHoja); // Añadir el nuevo item
            menuArchivo.addSeparator(); // Separador visual
            menuArchivo.add(itemGuardar);
            menuArchivo.add(itemAbrir);

            // Añadir el menú "Archivo" a la barra de menú principal
            menuBar.add(menuArchivo);
            
            // Añadir el menú "Tabla Hash" directamente a la barra de menú
            itemTablaHash = new JMenuItem("Tabla Hash");
            menuBar.add(itemTablaHash); 

            // Crear el menú "VER"
            JMenu menuVer = new JMenu("Ver");
            itemDesarrolladoPor = new JMenuItem("Desarrollado por Carlos Cachin y Ruben Garcia");
            // Añadir un ActionListener para mostrar el mensaje al hacer clic
            itemDesarrolladoPor.addActionListener(e -> 
                JOptionPane.showMessageDialog(this, "Desarrollado por Carlos Cachin y Ruben Garcia", "Información", JOptionPane.INFORMATION_MESSAGE)
            );
            menuVer.add(itemDesarrolladoPor);
            menuBar.add(menuVer); // Añadir el menú "Ver" a la barra de menú principal
                                  // Se coloca después de "Tabla Hash" para que aparezca a su derecha.
            
            setJMenuBar(menuBar); // Establecer la barra de menú en el JFrame

            // Layout principal
            add(formulaPanel, BorderLayout.NORTH);
            add(tabbedPane, BorderLayout.CENTER);
        }

        /**
        * Configura la JTable con estilos y renderizadores.
        * @param tabla La JTable a configurar.
        */
        public void configurarTabla(JTable tabla) {
            tabla.setCellSelectionEnabled(true);
            tabla.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // Permite selección de bloques
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Desactiva el auto-ajuste de columnas

            // Configurar el renderizador para la columna de números de fila
            tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(new Color(220, 220, 220)); // Color de fondo gris claro
                    c.setFont(c.getFont().deriveFont(Font.BOLD)); // Negrita
                    setHorizontalAlignment(SwingConstants.CENTER); // Centrar el texto
                    return c;
                }
            });
            tabla.getColumnModel().getColumn(0).setPreferredWidth(50); // Ancho fijo para la columna #

            // Renderizador para las celdas de datos (alineación a la izquierda por defecto)
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
            cellRenderer.setHorizontalAlignment(SwingConstants.LEFT);
            for (int i = 1; i < tabla.getColumnModel().getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                tabla.getColumnModel().getColumn(i).setPreferredWidth(100); // Ancho predeterminado para columnas de datos
            }

            // Configurar el cabecero de la tabla (columnas A, B, C...)
            JTableHeader header = tabla.getTableHeader();
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(new Color(230, 230, 230)); // Color de fondo gris más claro
                    c.setFont(c.getFont().deriveFont(Font.BOLD)); // Negrita
                    setHorizontalAlignment(SwingConstants.CENTER); // Centrar el texto
                    return c;
                }
            });
        }

        /**
        * Obtiene la JTable actualmente visible en el JTabbedPane.
        * @return La JTable activa, o null si no hay ninguna.
        */
        public JTable getTablaActual() {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                JScrollPane scrollPane = (JScrollPane) tabbedPane.getComponentAt(selectedIndex);
                return (JTable) scrollPane.getViewport().getView();
            }
            return null;
        }

        /**
        * Obtiene el índice de fila de la celda actualmente seleccionada en la JTable (0-based, lógico).
        * @return El índice de fila seleccionado, o -1 si no hay selección.
        */
        public int getFilaSeleccionada() {
            JTable tablaActual = getTablaActual();
            return (tablaActual != null) ? tablaActual.getSelectedRow() : -1;
        }

        /**
        * Obtiene el índice de columna de la celda actualmente seleccionada en la JTable (0-based, lógico).
        * Convierte el índice de la vista a un índice lógico (ignorando la columna #).
        * @return El índice de columna seleccionado, o -1 si no hay selección o la columna # está seleccionada.
        */
        public int getColumnaSeleccionada() {
            JTable tablaActual = getTablaActual();
            int colEnVista = (tablaActual != null) ? tablaActual.getSelectedColumn() : -1;
            return (colEnVista > 0) ? colEnVista - 1 : -1; // -1 para convertir de índice de vista a índice lógico (0-based)
        }

        /**
        * Obtiene el nombre de la hoja actualmente seleccionada en el JTabbedPane.
        * @return El nombre de la hoja seleccionada, o null si no hay ninguna seleccionada.
        */
        public String getNombreHojaSeleccionada() {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                return tabbedPane.getTitleAt(selectedIndex);
            }
            return null;
        }
    }