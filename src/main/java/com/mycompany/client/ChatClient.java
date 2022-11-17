package com.mycompany.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ChatClient {

    private String hostname;
    private int port;
    private String userName = "Luis";
    private String password = "Rosell";
    private static Logger logger = Logger.getLogger("myLog");
    private FileHandler fileHandler;

    /**
     * Create a new chatClient.
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
        try{
            fileHandler = new FileHandler("src/main/resources/MyLogFile.txt");
            logger.addHandler(fileHandler);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
        }catch (SecurityException e) {
            logger.info("Exception:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("IO Exception:" + e.getMessage());
            e.printStackTrace();
        }
        
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Conectado al servidor");
            
            /*========== EJEMPLO COMUNICACIÓN CON EL SERVIDOR ===========*/
            
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                       
            writer.println("login "+userName+" "+password);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response  = reader.readLine();
            
            String[]resp = response.split(" ");
            switch(resp[0]){
                case "200" -> logger.info(resp[1]);
                case "400" -> logger.warning(resp[1]);
                case "401" -> logger.warning(resp[1]);                
            }
            
            if(response.equals("success")) {
                System.out.println("Logeado");
            } else {
                System.out.println("no logeado");
            }
            
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

    public static void main(String[] args) {

        // We define the IP direction and the port
        String hostname = "localhost"; //Luis ip: 192.168.3.152
        int port = 3337;

        // We launch the client
        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}
