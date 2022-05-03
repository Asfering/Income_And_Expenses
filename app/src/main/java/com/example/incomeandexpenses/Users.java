package com.example.incomeandexpenses;

public class Users {
    private int IdUser;
    private String PhoneNumber;
    private String Password;
    private String Name;

    public Users(){

    }

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
        IdUser = idUser;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
