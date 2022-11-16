/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author sdiazram
 */
public class Connection {


    private final int PORT = 3337; //Puerto para la conexión
    private final String HOST = "localhost"; //Host para la conexión
    

    protected String serverMessage; //Mensajes entrantes (recibidos) en el servidor
    
    protected ServerSocket serverSocket; //Socket del servidor
    protected Socket clientSocket; //Socket del cliente
    
    protected DataOutputStream outputServer, outputClient; //Flujo de datos de salida

    public Connection(String type) throws IOException 
    {
        if (type.equalsIgnoreCase("server")) {
            serverSocket = new ServerSocket(PORT);//Se crea el socket para el servidor en puerto 1234
            clientSocket = new Socket(); //Socket para el cliente
        } else {
            clientSocket = new Socket(HOST, PORT); //Socket para el cliente en localhost en puerto 1234
        }
    }
}
