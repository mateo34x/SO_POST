package org.example;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private static final String URL = "jdbc:sqlite:productos.db";

    public static void insertarProducto(Producto producto, JTextArea messageArea) {
        DatabaseManager.createTable();
        String sqlSelect = "SELECT COUNT(*) AS count FROM productos WHERE codigo_barras = ?";
        String sqlInsert = "INSERT INTO productos (codigo_barras, nombre, precio) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
             PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {

            selectStatement.setString(1, producto.getId());
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt("count");

            if (count > 0) {
                messageArea.setText("EL producto existe en la base de datos ");

            } else {
                insertStatement.setString(1, producto.getId());
                insertStatement.setString(2, producto.getNombre());
                insertStatement.setDouble(3, producto.getPrecio());
                insertStatement.executeUpdate();
                obtenerTodosLosProductos(messageArea);
            }


        } catch (SQLException e) {
            messageArea.setText("Error al insertar el producto: " + e.getMessage());
        }
    }

    public static List<Producto> obtenerTodosLosProductos(JTextArea messageArea) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT codigo_barras, nombre, precio FROM productos";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Producto producto = new Producto();
                producto.setId(resultSet.getString("codigo_barras"));
                producto.setNombre(resultSet.getString("nombre"));
                producto.setPrecio(resultSet.getDouble("precio"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            messageArea.setText("Error al obtener todos los productos: " + e.getMessage());
        }
        return productos;
    }

    public static Producto obtenerProductoPorId(String codigoBarras) {
        String sql = "SELECT nombre, precio FROM productos WHERE codigo_barras = ?";
        Producto producto = null;

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigoBarras.replace(" ",""));
            ResultSet resultSet = statement.executeQuery();

            // Si se encuentra un resultado, crear un objeto Producto y asignar los valores
            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                double precio = resultSet.getDouble("precio");
                producto = new Producto(codigoBarras, nombre, precio);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el producto: " + e.getMessage());
        }

        return producto;
    }

    public static void actualizarPrecioProducto(Producto producto, JTextArea messageArea) {
        String sqlUpdate = "UPDATE productos SET precio = ? WHERE codigo_barras = ?";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {

            updateStatement.setDouble(1, producto.getPrecio());
            updateStatement.setString(2, producto.getId());

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                messageArea.setText("Precio del producto actualizado correctamente.");

            } else {
                messageArea.setText("El producto a actualizar no existe.");
            }
        } catch (SQLException e) {
            messageArea.setText("Error al actualizar el producto: " + e.getMessage());
        }
    }


}
