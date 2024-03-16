package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:productos.db";

    public static void createTable() {
        try (Connection connection = DriverManager.getConnection(URL);
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS productos (" +
                    "codigo_barras TEXT PRIMARY KEY," +
                    "nombre TEXT," +
                    "precio REAL)");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla de productos: " + e.getMessage());
        }
    }
}
