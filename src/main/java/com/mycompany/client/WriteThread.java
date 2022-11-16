package com.mycompany.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
 

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;
 
    public WriteThread(Socket socket, ChatClient client) {
        // Almacenamos socket de conexion y cliente
        this.socket = socket;
        this.client = client;
 
        try {
            // Inicializamos el buffer de escritura
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("[ERROR ESCRITURA]: " + ex.getMessage());
        }
    }
 
    @Override
    public void run() {
 
        
        // Leemos el nombre de usuario
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce tu nombre de usuario: ");
        String userName = scanner.nextLine();
        
        // Guardamos el nombre en el cliente
        client.setUserName(userName);
        writer.println(userName);
 
        String text;
 
        do {
            // Mientras el mensaje no sea 'SALIR' escribimos mensajes
            System.out.print("[" + userName + "]: ");
            text = scanner.nextLine();
            writer.println(text);
 
        } while (!text.equals("SALIR"));
 
        try {
            // Cerramos el socket
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("[ERROR ESCRITURA]: " + ex.getMessage());
        }
    }
}