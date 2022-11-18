package com.mycompany.client;

import java.io.*;
import java.net.*;

public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;
    private boolean firstTime = true;
    
    /**
     * Reads a thread.
     * @param socket
     * @param client
     */
    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            // Initialize the buffer for reading 
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("[ERROR LECTURA]: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Reads messages in buffer
                String response = reader.readLine();
                // Prints the messages
                System.out.println("\n" + response);

                // Prints the username
                if (!this.firstTime) {
                    System.out.print("[" + client.getUserName() + "]: ");
                }
                firstTime = false;
            } catch (IOException ex) {
                System.out.println("[ERROR LECTURA]: " + ex.getMessage());
                break;
            }
        }
    }
}
