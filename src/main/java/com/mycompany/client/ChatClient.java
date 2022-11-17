package com.mycompany.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient {

    private String hostname;
    private int port;
    private String userName;
    Scanner sc = new Scanner(System.in);

    /**
     * Create a new chatClient.
     *
     * @param hostname hostname
     * @param port Listening port
     */
    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Starts the chat.
     */
    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Conectado al servidor");

            /*========== EJEMPLO COMUNICACIÓN CON EL SERVIDOR ===========*/
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            int selectOption;
            do {
                this.logInMenu();
                selectOption = Integer.parseInt(sc.nextLine());
                String userName;
                String psswd;
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                switch (selectOption) {
                    case 1:
                        System.out.println("Introduce tu nombre de usuario");
                        userName = sc.nextLine();
                        System.out.println("Introduce tu contraseña");
                        psswd = sc.nextLine();
                        writer.println("login " + userName + " " + psswd);

                        if (reader.readLine().equals("200")) {
                            System.out.println("Se ha logueado correctamente");
                            this.userName = userName;
                            selectOption = 0;
                        }
                        break;
                    case 2:
                        System.out.println("Introduce tu nombre de usuario");
                        userName = sc.nextLine();
                        System.out.println("Introduce tu contraseña");
                        psswd = sc.nextLine();
                        writer.println("register " + userName + " " + psswd);

                        if (reader.readLine().equals("200")) {
                            System.out.println("---------------------------\nSe ha registrado tu usuario");
                        }
                        break;
                    case 0:
                        socket.close();
                }

            } while (selectOption != 0);

            /*============= FIN EJEMPLO ================*/
            // Inicia el hilo de lectura (mostrar por pantalla)
            // Set the read thread (screen)
            new ReadThread(socket, this).start();

            // Set the write thread
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Servidor no encontrado: " + ex.getMessage());
        } catch (IOException ex) {

            // Exception
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }

    // Set the UserName
    void setUserName(String userName) {
        this.userName = userName;
    }

    // Get the UserName
    String getUserName() {
        return this.userName;
    }

    public void logInMenu() {
        System.out.println("---------------------------");
        System.out.println("""
                           QUÉ QUIERES REALIZAR EN EL SISTEMA
                           | 1. Iniciar sesión
                           | 2. Registrarse
                           | 0. CERRAR CONEXIÓN """);
        System.out.println("---------------------------");
    }

    public static void main(String[] args) {

        // We define the IP direction and the port
        String hostname = "localhost"; //Luis ip: 192.168.3.152
        int port = 3337;

        // We launch the client
        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}
