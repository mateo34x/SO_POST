package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static org.example.Main.messageArea;
public class UI_Products {

    static void mostrarPanelCrearProducto(JTextField codigoBarrasField,String CDP ) {
        JFrame crearProductoFrame = new JFrame("Crear Producto");
        crearProductoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        crearProductoFrame.setSize(300, 200);

        JPanel panelCrearProducto = new JPanel();
        panelCrearProducto.setLayout(new GridLayout(3, 2));

        JLabel codigoBarrasLabel = new JLabel("Código de Barras:");
        codigoBarrasField.setText(CDP);
        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreField = new JTextField();
        JLabel precioLabel = new JLabel("Precio:");
        JTextField precioField = new JTextField();

        panelCrearProducto.add(codigoBarrasLabel);
        panelCrearProducto.add(codigoBarrasField);
        panelCrearProducto.add(nombreLabel);
        panelCrearProducto.add(nombreField);
        panelCrearProducto.add(precioLabel);
        panelCrearProducto.add(precioField);

        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigoBarras = codigoBarrasField.getText();
                String nombre = nombreField.getText();
                double precio = Double.parseDouble(precioField.getText());

                Producto producto = new Producto();
                producto.setId(codigoBarras);
                producto.setNombre(nombre);
                producto.setPrecio(precio);

                ProductoDAO.insertarProducto(producto,messageArea);

                if (!messageArea.getText().equals("EL producto existe en la base de datos ")){
                    mostrarTodosLosProductos();
                    SwingUtilities.invokeLater(() -> {
                        codigoBarrasField.setText("");
                        nombreField.setText("");
                        precioField.setText("");
                    });
                }else{
                    SwingUtilities.invokeLater(() -> {
                        codigoBarrasField.setText("");
                        nombreField.setText("");
                        precioField.setText("");
                    });

                }



            }
        });

        crearProductoFrame.add(panelCrearProducto, BorderLayout.CENTER);
        crearProductoFrame.add(guardarButton, BorderLayout.SOUTH);

        crearProductoFrame.setVisible(true);
    }



    static void mostrarTodosLosProductos() {
        List<Producto> productoList =  ProductoDAO.obtenerTodosLosProductos(messageArea);
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Todos los Productos:\n");
        for (Producto producto : productoList ) {
            mensaje.append("Código de Barras: ").append(producto.getId()).append("\n");
            mensaje.append("Nombre: ").append(producto.getNombre()).append("\n");
            mensaje.append("Precio: ").append(producto.getPrecio()).append("\n\n");
        }
        messageArea.setText(mensaje.toString());
    }

}
