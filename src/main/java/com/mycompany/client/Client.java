/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client;
import java.io.DataOutputStream;
import java.io.IOException;
import com.mycompany.connection.Connection;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
/**
 *
 * @author sdiazram
 */
public class Client extends Connection {
    
    public Client() throws IOException{super("client");} //Se usa el constructor para cliente de Conexion
    
    public void startClient() throws Exception {
        DataInputStream din = new DataInputStream(cs.getInputStream());
        DataOutputStream dout = new DataOutputStream(cs.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str = "", str2 = "";
        while (!str.equals("stop")) {
            str = br.readLine();
            dout.writeUTF(str);
            dout.flush();//
            str2 = din.readUTF();
            System.out.println("Server says: " + str2);
        }

        dout.close();
        cs.close();
    }
}
