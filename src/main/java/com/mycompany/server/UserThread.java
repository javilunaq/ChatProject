package com.mycompany.server;

import java.io.*;
import java.net.*;

public class UserThread extends Thread {

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private BufferedReader reader;

    private boolean successful = false;
    private String clientOrder = null;
    private String username = null;
    private String password = null;

    /**
     * Initializes the thread with a given socket and a reference of the server.
     *
     * @param socket for the comunication.
     * @param server a reference of the main server.
     */
    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * Executes the user thread.
     */
    public void run() {
        try {
            // Stores the input stream of the socket
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            // Stores the output stream of the socket
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            String userName = askOrder();
            String serverMessage;
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

    String askOrder() {
        do {
            successful = false;
            clientOrder = null;
            username = null;
            password = null;
            //printCommands(); BORRAR
            try {
                clientOrder = reader.readLine();
            } catch (IOException ex) {
                System.out.println("[ERROR HILO]: " + ex.getMessage());
            }

            String[] orderArguments = clientOrder.split(" ");

            if (orderArguments.length != 3) {
                writer.println("400_NUMERO DE ARGUMENTOS INCORRECTO");
            } else {
                String option = orderArguments[0];
                if(option.equals("login")){
                    login(orderArguments);
                }else if(option.equals("register")){
                    register(orderArguments);
                }else{
                    writer.println("400_ORDEN NULA");
                }
                
            }
        } while (!successful);
        return username;
    }

    /**
     *
     * @param orderArguments
     */
    private void login(String[] orderArguments) {
        username = orderArguments[1];
        password = orderArguments[2];
        int autenticated = server.authenticate(username, password);


        if (autenticated == 0) {

            writer.println("200_AUTENTIFICACION CORRECTA");
            successful = true;

            printUsers();

            server.connectUser(username);

            // Announces that someone has just connected
            String serverMessage = "Nuevo usuario conectado: " + username;
            server.broadcast(serverMessage, this);


        } else if (autenticated == 1){
            writer.println("401_AUTENTIFICACION FALLIDA");
        } else
            writer.println("403_USUARIO YA CONECTADO");

    }

    /**
     *
     * @param orderArguments
     */
    private void register(String[] orderArguments) {
        username = orderArguments[1];
        boolean userExists = server.checkUserExists(username);

        if (userExists) {
            writer.println("403_USUARIO YA EXISTE");
        } else {
            writer.println("201_USUARIO CREADO");

            password = orderArguments[2];
            server.registerUser(username, password);
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

//    void printCommands() {
//        
//        String options = """
//                         Comandos disponibles:
//                            login       <user>  <password>
//                            register    <user>  <password>
//                         """;
//        writer.println(options);
//    }
    /**
     * Print the given message.
     *
     * @param message message to print.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
