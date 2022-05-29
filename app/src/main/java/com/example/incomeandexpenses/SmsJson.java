package com.example.incomeandexpenses;


/** Вспомогательный класс для работы с NalogApiReader
 *
 */
public class SmsJson extends PhoneJson {

    private String code;

    public SmsJson(String phone, String client_secret, String os, String code) {
        super(phone, client_secret, os);
        this.code = code;
    }
}
