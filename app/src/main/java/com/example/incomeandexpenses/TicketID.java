package com.example.incomeandexpenses;


/** Вспомогательный класс для работы с NalogApiReader
 *
 */
public class TicketID {
    private String qr;

    public TicketID(String qr){
        this.qr = qr;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }
}
