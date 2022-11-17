/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client;

/**
 *
 * @author lrosellg
 */
public class Login {
    
     private static boolean success;
    
    public static boolean successLogin(boolean success){
        Login.success = success;  
        
        return success;
    }
    
    public String[]  authenticate(String userName, String password){
        String[] register = new String[2];
        register[0] = userName;
        register[1] = password;
        return register;
    }
    
}
