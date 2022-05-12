package com.example.incomeandexpenses;

import org.threeten.bp.LocalDateTime;

import java.util.Calendar;

public class Operations {
    private int IdOperation;
    private int IdUser;
    private String Name;
    private boolean TypeOperation;
    private Calendar TimeStamp;
    private float Sum;
    private String Category;

    public Operations(int idOperation, int idUser, String name, boolean typeOperation, Calendar timeStamp, float sum, String category){
        this.IdOperation = idOperation;
        this.IdUser = idUser;
        this.Name = name;
        this.TypeOperation = typeOperation;
        this.TimeStamp = timeStamp;
        this.Sum = sum;
        this.Category = category;
    }

    public int getIdUser() {
        return IdUser;
    }

    public Calendar getTimeStamp() {
        return TimeStamp;
    }

    public String getName() {
        return Name;
    }

    public float getSum() {
        return Sum;
    }

    public String getCategory() {
        return Category;
    }

    public int getIdOperation() {
        return IdOperation;
    }

    public boolean getTypeOperation(){
        return TypeOperation;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setTimeStamp(Calendar timeStamp) {
        TimeStamp = timeStamp;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setSum(int sum) {
        Sum = sum;
    }

    public void setIdOperation(int idOperation) {
        IdOperation = idOperation;
    }

    public void setTypeOperation(boolean typeOperation) {
        TypeOperation = typeOperation;
    }
}
