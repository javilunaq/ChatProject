package com.mycompany.server;

import com.mycompany.connection.Connection;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Server extends Connection{

    public Server() throws IOException {
        super("server");
    } //Se usa el constructor para servidor de Conexion

    public void startServer() throws IOException {
        
        cs = ss.accept();
        
        DataInputStream din = new DataInputStream(cs.getInputStream());
        DataOutputStream dout = new DataOutputStream(cs.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str = "", str2 = "";
        while (!str.equals("stop")) {
            str = din.readUTF();
            System.out.println("client says: " + str);
            str2 = br.readLine();
            dout.writeUTF(str2);
            dout.flush();//
        }
        din.close();
        cs.close();
        ss.close();
    }
}
