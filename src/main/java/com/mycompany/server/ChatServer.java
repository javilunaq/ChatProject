package com.mycompany.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    // Puerto por el que escuchamos
    private int port;

    // Lista de nombres de usuarios conectados
    private Set<String> userNames = new HashSet<>();

    // Hilos para atender a los usuarios
    private Set<UserThread> userThreads = new HashSet<>();

    /**
     * Create a new server and set the listening port.
     * @param port Listening port
     */
    public ChatServer(int port) {
        this.port = port;
    }

    /**
     * Starts the server.
     */
    public void execute() {
        // El recurso se cierra al final, porque implementa java.lang.AutoCloseable
        try ( ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("El servidor esta escuchando en el puerto " + port);

            while (true) {
                // En cada iteracion escuchamos hasta que llega una peticion, y lanzamos un hilo que atienda al nuevo usuario
                Socket socket = serverSocket.accept();
                System.out.println("Nuevo usuario conectado");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("[ERROR]: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {

        // Definimos el puerto de escucha y lanzamos el servidor
        int port = 3337;

        ChatServer server = new ChatServer(port);
        server.execute();
    }
    /**
     * Send the message to all users except the sender.
     * @param message The content to be sent to all users.
     * @param excludeUser The sender thread.
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }
    /**
     * Add the new user to the list of user names.
     * @param userName User's username.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }
    /**
     * Remove an user from the list of connected users and his thread
     * @param userName User's username.
     * @param aUser User's thread.
     */
    void removeUser(String userName, UserThread aUser) {
        userNames.remove(userName);
        userThreads.remove(aUser);
        System.out.println("El usuario " + userName + " se ha desconectado");

    }

    /**
     * Get the connected users list 
     * @return Connected user String list
     */
    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Check if there are users connected to the server
     * @return True if there are users connected or False if there aren't.
     *      
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
