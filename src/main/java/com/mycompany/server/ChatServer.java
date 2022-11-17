package com.mycompany.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    // Puerto por el que escuchamos
    private int port;

    // Lista de nombres de usuarios conectados
    private Set<String> onlineUsers = new HashSet<>();
    private HashMap<String, User> users = new HashMap<>();

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
     * Add a new user to the list of online users.
     * @param userName User's username.
     */
    void connectUser(String userName) {
        onlineUsers.add(userName);
    }
    /**
     * Remove an user from the list of online users and his thread.
     * @param userName User's username.
     * @param aUser User's thread.
     */
    void disconnectUser(String userName, UserThread aUser) {
        onlineUsers.remove(userName);
        userThreads.remove(aUser);
        System.out.println("El usuario " + userName + " se ha desconectado");
    }
    /**
     * Add a new user to the list of registered users.
     * @param userName Name of the new user.
     * @param pass Password of the new user.
     */
    void registerUser(String userName, String pass) {
        users.put(userName, new User(userName, pass));
    }
    /**
     * Delete a user from the list of registered users.
     * @param userName Name of the user to remove.
     */
    void removeUser(String userName) {
        users.remove(userName);
        System.out.println("El usuario " + userName + " se eliminado");
    }
    /**
     * Get the online users list 
     * @return Connected user String list
     */
    Set<String> getUsersOnline() {
        return this.onlineUsers;
    }
    /**
     * Get the registered users set.
     * @return Registered user String set.
     */
    Set<String> getRegisteredUsers() {
        return this.users.keySet();
    }
    /**
     * Check if there are online users.
     * @return True if there are users connected or False if there aren't.    
     */
    boolean hasOnlineUsers() {
        return !this.onlineUsers.isEmpty();
    }
    /**
     * Check if there are users registered on the server.
     * @return True if there are users registered or False if there aren't.    
     */
    boolean hasRegisteredUsers() {
        return !this.users.isEmpty();
    }
}
