package com.example.incomeandexpenses;

public class JsonReaderSessionID {
    private String sessionId;
    private String refresh_token;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
