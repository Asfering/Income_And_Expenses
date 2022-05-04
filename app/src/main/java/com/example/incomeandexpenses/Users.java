package com.example.incomeandexpenses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Users implements Serializable {
    private int IdUser;
    private String PhoneNumber;
    private String Password;
    private String Name;


    public Users (int IdUser, String PhoneNumber, String Password, String Name){
        this.IdUser = IdUser;
        this.PhoneNumber = PhoneNumber;
        this.Password = Password;
        this.Name = Name;
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
