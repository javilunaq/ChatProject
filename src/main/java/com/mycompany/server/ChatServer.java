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

    public ChatServer(int port) {
        this.port = port;
    }

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

    // Manda un mensaje a todos los usuarios, menos al emisor
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    // Añade el nombre de usuario a la lista de usuarios conectados
    void addUserName(String userName) {
        userNames.add(userName);
    }

    // Elimina a un usuario de la lista de usuarios conectados y de la lista de hilos
    void removeUser(String userName, UserThread aUser) {
        userNames.remove(userName);
        userThreads.remove(aUser);
        System.out.println("El usuario " + userName + " se ha desconectado");

    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    // Comprueba si hay usuarios conectados
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
