package com.example.incomeandexpenses;


/** Вспомогательный класс для работы с NalogApiReader
 *
 */
public class PhoneJson {
    private String phone;
    private String client_secret;
    private String os;

    public PhoneJson(String phone, String client_secret, String os){
        this.phone = phone;
        this.client_secret = client_secret;
        this.os = os;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getOs() {
        return os;
    }

    public String getPhone() {
        return phone;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
