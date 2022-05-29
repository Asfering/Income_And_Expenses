package com.example.incomeandexpenses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.*;

//TODO: Реализовать сканирование по QR-коду!
/**
 * Класс для считывания QR-кода чека. В РАБОТЕ!
 */
public class NalogAPIReader {
    // Описать что да как
    private final String Host = "irkkt-mobile.nalog.ru:8888";
    private final String DeviceOs = "iOS";
    private final String ClientVersion = "2.9.0";
    private final String DeviceId = "7C82010F-16CC-446B-8F66-FC4080C66521";
    private final String Accept = "*/*";
    private final String UserAgent = "billchecker/2.9.0 (iPhone; iOS 13.6; Scale/2.00)";
    private final String AcceptLanguage = "ru-RU;q=1, en-US;q=0.9";
    private final String ClientSecret = "IyvrAbKt9h/8p6a7QPh8gpkXYQ4=";
    private final String Os = "Android";
    private String SessionId = null;
    private String PhoneNumber = null;

    private final OkHttpClient httpClient = new OkHttpClient();

    public NalogAPIReader(){
        this.SessionId = null;
        this.PhoneNumber = null;
    }


    // Регаем номер
    public void regNumber(String phoneNumber){
        this.PhoneNumber = phoneNumber;
        PhoneJson phoneJson = new PhoneJson(PhoneNumber, ClientSecret, Os);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(phoneJson));

        Request request = builderPhoneSms("https://irkkt-mobile.nalog.ru:8888/v2/auth/phone/request", body);

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(responseAnswer(response)){
                    System.out.println(response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean responseAnswer(Response response) throws IOException {
        try {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response.body().string());
            } else
                return true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }


    public void smsCode(String Code, CallBack callBack){
        SmsJson smsJson = new SmsJson(PhoneNumber, ClientSecret, Os, Code);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(smsJson));

        Request request = builderPhoneSms("https://irkkt-mobile.nalog.ru:8888/v2/auth/phone/verify", body);

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (responseAnswer(response)){
                    String sessionId = response.body().string();
                    System.out.println(sessionId);
                    //JsonReaderSessionID jsonObject = gson.fromJson(response.body().string(), JsonReaderSessionID.class);
                    //jsonObject.getSessionId();
                    setSessionId(sessionId);
                    if (callBack != null) {
                        callBack.onSmsSend(sessionId);
                    }

                    System.out.println(sessionId);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    void setSessionId(String sessionId){
        this.SessionId = sessionId;
    }

    public String getTicketID(String QR, final String sessionId){
        TicketID ticketID = new TicketID(QR);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(ticketID));
        Request request = builderGetTicketId("https://irkkt-mobile.nalog.ru:8888/v2/ticket", body, sessionId);

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(responseAnswer(response)){
                    System.out.println(response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
        return "";
    }

    public void getTicket(String QR, final String sessionId){
        String ticket_id = getTicketID(QR, sessionId);
        Request request = builderGetTicket(String.format("https://irkkt-mobile.nalog.ru:8888/v2/tickets/%s", ticket_id), sessionId);

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(responseAnswer(response)){
                    System.out.println(response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    interface CallBack {
        void onSmsSend(String sessionId);
    }

    // Запрос к АПИ для телефона и СМС
    private Request builderPhoneSms(String url, RequestBody body){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Host", Host)
                .addHeader("Accept", Accept)
                .addHeader("Device-OS", DeviceOs)
                .addHeader("Device-ID", DeviceId)
                .addHeader("clientVersion", ClientVersion)
                .addHeader("Accept-Language", AcceptLanguage)
                .addHeader("User-Agent", UserAgent)
                .post(body)
                .build();
        return request;
    }

    // Запрос АПИ ФНС на получение ID чека
    private Request builderGetTicketId(String url, RequestBody body, String SessionId){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Host", Host)
                .addHeader("Accept", Accept)
                .addHeader("Device-OS", DeviceOs)
                .addHeader("Device-ID", DeviceId)
                .addHeader("clientVersion", ClientVersion)
                .addHeader("Accept-Language", AcceptLanguage)
                .addHeader("User-Agent", UserAgent)
                .addHeader("sessionId", SessionId)
                .post(body)
                .build();
        return request;
    }

    // Запрос на получение чека по ссылке с его ID
    private Request builderGetTicket(String url, String SessionId){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Host", Host)
                .addHeader("Accept", Accept)
                .addHeader("Device-OS", DeviceOs)
                .addHeader("Device-ID", DeviceId)
                .addHeader("clientVersion", ClientVersion)
                .addHeader("Accept-Language", AcceptLanguage)
                .addHeader("User-Agent", UserAgent)
                .addHeader("sessionId", SessionId)
                .build();
        return request;
    }
}
