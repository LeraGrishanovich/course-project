package com.example.cinema_client.Model.entity;


import lombok.Data;



@Data
public class User {

    private int idUser;

    private String login;

    private int password;

    private boolean isAdmin;


    public static int RSHash(String str) {
        int b = 378551;
        int a = 63689;
        int hash = 0;

        for(int i = 0; i < str.length(); ++i) {
            hash = hash * a + str.charAt(i);
            a *= b;
        }
        return hash;
    }
}
