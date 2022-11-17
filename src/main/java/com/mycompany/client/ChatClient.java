package com.mycompany.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient {

    private String hostname;
    private int port;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Conectado al servidor");
            
            /*========== EJEMPLO COMUNICACIÓN CON EL SERVIDOR ===========*/
            
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                       
            writer.println("login");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response  = reader.readLine();
            
            if(response.equals("success")) {
                System.out.println("Logeado");
            } else {
                System.out.println("no logeado");
            }
            
            /*============= FIN EJEMPLO ================*/
            
            // Inicia el hilo de lectura (mostrar por pantalla)
            new ReadThread(socket, this).start();
            // Inicia el hilo de escritura (introducir datos)
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Servidor no encontrado: " + ex.getMessage());
        } catch (IOException ex) {
            // La clase socket obliga a tratar la excepcion
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }
    
    // Establece el nombre de usuario
    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }

    public static void main(String[] args) {
        
        // Definimos direccion IP y puerto al que conectarnos
        String hostname = "localhost"; //Luis ip: 192.168.3.152
        int port = 3337;

        // Lanzamos el cliente
        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}
