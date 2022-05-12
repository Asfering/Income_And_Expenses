package com.example.incomeandexpenses;

public class Items {
    private int IdItem;
    private int IdOperation;
    private String Name;
    private int Nds;
    private float NdsSum;
    private int PaymentType;
    private float Price;
    private float Quantity;
    private float Sum;
    private String Category;

    public Items(int idItem, int idOperation, String name, int nds, float ndsSum, int paymentType, float price, float quantity, float sum, String category){
        this.IdItem = idItem;
        this.IdOperation = idOperation;
        this.Name = name;
        this.Nds = nds;
        this.NdsSum = ndsSum;
        this.PaymentType = paymentType;
        this.Price = price;
        this.Quantity = quantity;
        this.Sum = sum;
        this.Category = category;
    }

    public Items(int idItem, int idOperation, String name, float quantity, float sum, String category){
        this.IdItem = idItem;
        this.IdOperation = idOperation;
        this.Name = name;
        this.Quantity = quantity;
        this.Sum = sum;
        this.Category = category;
    }

    public int getIdOperation() {
        return IdOperation;
    }

    public String getName() {
        return Name;
    }

    public int getIdItem() {
        return IdItem;
    }

    public int getNds() {
        return Nds;
    }

    public float getNdsSum() {
        return NdsSum;
    }

    public int getPaymentType() {
        return PaymentType;
    }

    public float getPrice() {
        return Price;
    }

    public float getQuantity() {
        return Quantity;
    }

    public float getSum() {
        return Sum;
    }

    public String getCategory() {
        return Category;
    }

    public void setIdOperation(int idOperation) {
        IdOperation = idOperation;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setIdItem(int idItem) {
        IdItem = idItem;
    }

    public void setNds(int nds) {
        Nds = nds;
    }

    public void setNdsSum(int ndsSum) {
        NdsSum = ndsSum;
    }

    public void setPaymentType(int paymentType) {
        PaymentType = paymentType;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setSum(int sum) {
        Sum = sum;
    }
}
