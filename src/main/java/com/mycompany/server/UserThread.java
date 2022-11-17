package com.mycompany.server;

import java.io.*;
import java.net.*;

public class UserThread extends Thread {

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private BufferedReader reader;
    /**
     * Initializes the thread with a given socket and a reference of the server. 
     * @param socket for the comunication.
     * @param server a reference of the main server.
     */
    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }
    /**
     * Executes the main chat tasks.
     */
    public void run() {
        try {
            // Stores the input stream of the socket
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            // Stores the output stream of the socket
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            // Shows online users
            printUsers();

            // Requests an username and adds it to the list
            String userName = reader.readLine();
            server.connectUser(userName);

            // Announces that someone has just connected
            String serverMessage = "Nuevo usuario conectado: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                // Reads the message stored in the buffer and it gets send to the online users until the user types "SALIR"
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("SALIR"));

            // Deletes the user and closes the connection
            server.disconnectUser(userName, this);
            socket.close();

            // Warns the online user that someone has just disconnected
            serverMessage = userName + " se ha desconectado.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("[ERROR HILO]: " + ex.getMessage());
        }
    }

    /**
     * Print the list of connected users.
     */
    void printUsers() {
        if (server.hasOnlineUsers()) {
            writer.println("Usuarios conectados: " + server.getUsersOnline());
        } else {
            writer.println("No hay usuarios conectados.");
        }
    }

    /**
     * Print the given message.
     * @param message message to print.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
