package com.example.incomeandexpenses.categories;

/**
 * Класс для RecyclerView категорий в расходах / доходах
 */
public class Categories {
    private String Name;
    private float Sum;
    private float Percentage;

    public String getName() {
        return Name;
    }

    public float getSum() {
        return Sum;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setSum(float sum) {
        Sum = sum;
    }

    public float getPercentage() {
        return Percentage;
    }

    public void setPercentage(float percentage) {
        Percentage = percentage;
    }
}
