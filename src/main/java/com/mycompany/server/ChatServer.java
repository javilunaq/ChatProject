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
     *
     * @param port Listening port
     */
    public ChatServer(int port) {
        this.port = port;

        loadData();

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
                System.out.println("Nueva conexión establecida.");

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
 * Serializes the user data structure and save the information in a file
 */
    public void saveData() {
        try {
            FileOutputStream myFileOutStream
                    = new FileOutputStream(
                            "data/usersData.txt");

            ObjectOutputStream myObjectOutStream
                    = new ObjectOutputStream(myFileOutStream);

            myObjectOutStream.writeObject(users);

            myObjectOutStream.close();
            myFileOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Load a file and unserialize de information to create a data structure
     */
    public void loadData() {
        try {
            FileInputStream fileInput = new FileInputStream(
                    "data/usersData.txt");

            if (fileInput.available() != 0) {
                ObjectInputStream objectInput
                        = new ObjectInputStream(fileInput);
                users = (HashMap) objectInput.readObject();
                objectInput.close();
            }

            fileInput.close();
        } catch (IOException obj1) {
            obj1.printStackTrace();

        } catch (ClassNotFoundException obj2) {
            System.out.println("Class not found");
            obj2.printStackTrace();

        }
    }

    /**
     * Send the message to all users except the sender.
     *
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
     *
     * @param userName User's username.
     */
    void connectUser(String userName) {
        onlineUsers.add(userName);
    }

    /**
     * Remove an user from the list of online users and his thread.
     *
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
     *
     * @param userName Name of the new user.
     * @param pass Password of the new user.
     */
    void registerUser(String userName, String pass) {
        users.put(userName, new User(userName, pass));
        saveData();
    }

    /**
     * Delete a user from the list of registered users.
     *
     * @param userName Name of the user to remove.
     */
    void removeUser(String userName) {
        users.remove(userName);
        System.out.println("El usuario " + userName + " se ha eliminado");
    }

    /**
     * Get the online users list
     *
     * @return Connected user String list
     */
    Set<String> getUsersOnline() {
        return this.onlineUsers;
    }

    /**
     * Get the registered users set.
     *
     * @return Registered user String set.
     */
    Set<String> getRegisteredUsers() {
        return this.users.keySet();
    }

    /**
     * Check if there are online users.
     *
     * @return True if there are users connected or False if there aren't.
     */
    boolean hasOnlineUsers() {
        return !this.onlineUsers.isEmpty();
    }

    /**
     * Check if there are users registered on the server.
     *
     * @return True if there are users registered or False if there aren't.
     */
    boolean hasRegisteredUsers() {
        return !this.users.isEmpty();
    }

    boolean authenticate(String userName, String password) {
        boolean loggedin = false;
        if (checkUserExists(userName) && users.get(userName).getPassword().equals(password)) {
            loggedin = true;
        }

        return loggedin;
    }

    boolean checkUserExists(String userName) {
        boolean exists = false;
        if (users.get(userName) != null) {
            exists = true;
        }
        return exists;
    }
}
