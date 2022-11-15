/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client;

import java.io.*;

class MainCliente {

    public static void main(String[] args) throws IOException
    {
        Client cli = new Client(); //Se crea el cliente

        System.out.println("Iniciando cliente\n");
        try {
            cli.startClient(); //Se inicia el cliente
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
