package com.example.incomeandexpenses;

public class JsonReaderSessionID {
    private String sessionId;
    private String refresh_token;
    private String phone;

    public JsonReaderSessionID(String phone, String refreshToken, String sessionId){
        this.sessionId = sessionId;
        this.refresh_token = refreshToken;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRefreshToken(String refreshToken) {
        this.refresh_token = refreshToken;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
