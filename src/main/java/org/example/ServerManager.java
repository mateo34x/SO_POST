package org.example;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


import static org.example.Main.messageArea;


public class ServerManager {

    public static final int SERVER_PORT = 8080;

    public static void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public  void startServer(JTextField Fild, JTextField Delete, JTextField Price, String codigoP)  {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            appendMessage("Servidor iniciado. Esperando conexiones...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                appendMessage("Cliente conectado desde " + clientSocket.getInetAddress());


                    try {
                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        String message;
                        while ((message = inputReader.readLine()) != null) {
                            appendMessage("Mensaje recibido desde el cliente: " + message);

                            codigoP = message.trim();


                            String finalCodigoP = codigoP;
                            SwingUtilities.invokeLater(() -> {
                                Fild.setText(finalCodigoP);
                                Delete.setText(finalCodigoP);
                                Price.setText(finalCodigoP);
                            });

                        }
                    } catch (IOException e) {
                        appendMessage("Error al leer mensaje del cliente: " + e.getMessage());
                    }

            }
        } catch (IOException e) {
            appendMessage("Error en el servidor: " + e.getMessage());
        }
    }

}
