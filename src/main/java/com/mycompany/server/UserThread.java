package com.mycompany.server;

import java.io.*;
import java.net.*;

public class UserThread extends Thread {

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private BufferedReader reader;

    public UserThread(Socket socket, ChatServer server) {
        // Se almacena el socket de comunicacion y el servidor
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            // Se almacena el flujo de entrada del socket
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            // Se almacena el flujo de salida del socket
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            // Se muestran los usuarios conectados
            printUsers();

            // Se pide el nombre de usuario y se añade a la lista
            String userName = reader.readLine();
            server.addUserName(userName);

            // Se manda un mensaje al resto de usuarios para anunciar la entrada al chat
            String serverMessage = "Nuevo usuario conectado: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                // En cada iteración, se lee el mensaje almacenado en el buffer y se manda al resto de usuarios
                // hasta que el usuario introduzca la palabra "SALIR"
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("SALIR"));

            // Se saca al usuario y se cierra la conexion
            server.removeUser(userName, this);
            socket.close();

            // Se avisa al resto de usuarios de la desconexion
            serverMessage = userName + " se ha desconectado.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("[ERROR HILO]: " + ex.getMessage());
        }
    }

    // Imprime una lista de usuarios conectados
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Usuarios conectados: " + server.getUserNames());
        } else {
            writer.println("No hay usuarios conectados.");
        }
    }

    // Lo utiliza broadcast() para enviar los mensajes a los clientes
    void sendMessage(String message) {
        writer.println(message);
    }
}
