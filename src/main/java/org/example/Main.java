package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.example.UI_NewSold.mostrarPanelVender;
import static org.example.UI_Products.mostrarPanelCrearProducto;
import static org.example.UI_Products.mostrarTodosLosProductos;

public class Main {

    public static JTextArea messageArea;

    private static String  codigo_producto = "0000000000000";

    private static JTextField codigoBarrasField = new JTextField();
    private static JTextField codigoBarrasDelete = new JTextField();


    public static double totalVenta = 0.0;


    private static JTextField codigoBarrasUpdatePrice;











    public static void main(String[] args) {
        JFrame frame = new JFrame("Programa de la Computadora");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        messageArea = new JTextArea();
        messageArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        JButton crearProductoButton = new JButton("Crear Producto");
        JButton vender = new JButton("Nueva Venta");
        JButton editarproducto = new JButton("Editar Producto");
        JButton MostrarAllProducts = new JButton("Todos los productos");
        crearProductoButton.addActionListener(actionEvent -> mostrarPanelCrearProducto(codigoBarrasField,codigo_producto));

        vender.addActionListener(actionEvent -> mostrarPanelVender(codigoBarrasDelete));
        editarproducto.addActionListener(actionEvent -> actualizarprecio());

        MostrarAllProducts.addActionListener(actionEvent -> mostrarTodosLosProductos());

        buttonPanel.add(crearProductoButton, BorderLayout.NORTH);
        buttonPanel.add(vender, BorderLayout.NORTH);
        buttonPanel.add(editarproducto, BorderLayout.NORTH);
        buttonPanel.add(MostrarAllProducts, BorderLayout.NORTH);

        frame.add(buttonPanel,BorderLayout.SOUTH);

        frame.setVisible(true);

        new ServerManager().startServer(
                codigoBarrasField,
                codigoBarrasDelete,
                codigoBarrasUpdatePrice,
                codigo_producto);



    }












    private static void actualizarprecio(){

         JLabel codigoBarrasLabel;
         JLabel nuevoPrecioLabel;
         JTextField nuevoPrecioField;
         JButton actualizarButton;

         JFrame frameEditar = new JFrame();

        frameEditar.setTitle("Actualizar Precio del Producto");
        frameEditar.setSize(300, 200);
        frameEditar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        codigoBarrasLabel = new JLabel("Código de Barras:");
        codigoBarrasUpdatePrice = new JTextField(15);

        nuevoPrecioLabel = new JLabel("Nuevo Precio:");
        nuevoPrecioField = new JTextField(15);

        actualizarButton = new JButton("Actualizar Precio");
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí puedes escribir la lógica para actualizar el precio del producto
                String codigoBarras = codigoBarrasUpdatePrice.getText();
                double nuevoPrecio = Double.parseDouble(nuevoPrecioField.getText());
                Producto producto = new Producto();

                producto.setId(codigoBarras);
                producto.setPrecio(nuevoPrecio);
                ProductoDAO.actualizarPrecioProducto(producto,messageArea);
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(codigoBarrasLabel);
        panel.add(codigoBarrasField);
        panel.add(nuevoPrecioLabel);
        panel.add(nuevoPrecioField);
        panel.add(actualizarButton);

        frameEditar.add(panel);

        frameEditar.setVisible(true);


    }











}