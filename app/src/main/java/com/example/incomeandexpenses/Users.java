package com.example.incomeandexpenses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Класс пользователь
 */
public class Users implements Serializable {
    private int IdUser;
    private String PhoneNumber;
    private String Password;
    private String Name;


    public Users (int idUser, String phoneNumber, String password, String name){
        this.IdUser = idUser;
        this.PhoneNumber = phoneNumber;
        this.Password = password;
        this.Name = name;
    }

    public int getIdUser() {
        return IdUser;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setIdUser(int idUser) {
        this.IdUser = idUser;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }
}
