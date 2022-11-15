/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import java.io.*;

class MainServer {
    
    public static void main(String[] args) throws IOException
    {
        Server serv = new Server(); //Se crea el servidor

        System.out.println("Iniciando servidor\n");
        serv.startServer();
    }
}
