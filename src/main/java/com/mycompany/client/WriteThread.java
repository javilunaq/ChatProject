package com.mycompany.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
 

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;
    
    /**
     * Writes a thread.
     * @param socket socket
     * @param client client
     */
    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
 
        try {
            // Initialize the buffer for writing
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("[ERROR ESCRITURA]: " + ex.getMessage());
        }
    }
 
    @Override
    public void run() {
 
        // Reads username
        Scanner scanner = new Scanner(System.in);
        
        // Saves username in client
        client.setUserName(client.getUserName());
        writer.println(client.getUserName());
 
        String text;
 
        do {
            System.out.print("[" + client.getUserName() + "]: ");
            text = scanner.nextLine();
            writer.println(text);
 
        } while (!text.equals("SALIR"));
 
        try {
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("[ERROR ESCRITURA]: " + ex.getMessage());
        }
    }
}