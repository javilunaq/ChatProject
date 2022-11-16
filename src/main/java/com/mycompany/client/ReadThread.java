package com.mycompany.client;

import java.io.*;
import java.net.*;

public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        // Almacenamos socket de conexion y cliente
        this.socket = socket;
        this.client = client;

        try {
            // Inicializamos el buffer de lectura
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("[ERROR LECTURA]: " + ex.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                // Lee mensajes del buffer
                String response = reader.readLine();
                // Imprime el mensaje por consola
                System.out.println("\n" + response);

                // Imprime el nombre de usuario despues del mensaje
                if (client.getUserName() != null) {
                    System.out.print("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                System.out.println("[ERROR LECTURA]: " + ex.getMessage());
                break;
            }
        }
    }
}
