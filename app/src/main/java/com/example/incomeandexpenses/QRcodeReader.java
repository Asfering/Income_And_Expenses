package com.example.incomeandexpenses;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;

import okhttp3.*;


public class QRcodeReader {
    private String HOST = "irkkt-mobile.nalog.ru:8888";
    private String DEVICE_OS = "iOS";
    private String CLIENT_VERSION = "2.9.0";
    private String DEVICE_ID = "7C82010F-16CC-446B-8F66-FC4080C66521";
    private String ACCEPT = "*/*";
    private String USER_AGENT = "billchecker/2.9.0 (iPhone; iOS 13.6; Scale/2.00)";
    private String ACCEPT_LANGUAGE = "ru-RU;q=1, en-US;q=0.9";
    private String CLIENT_SECRET = "IyvrAbKt9h/8p6a7QPh8gpkXYQ4=";
    private String OS = "Android";
    public String sessionId = null;

    private final OkHttpClient httpClient = new OkHttpClient();

    public QRcodeReader() throws Exception {
        this.sessionId = null;
        RegNumber();
    }


    // Регаем номер
    public void RegNumber() throws Exception {
        PhoneJson phoneJson = new PhoneJson("+79097261795", "IyvrAbKt9h/8p6a7QPh8gpkXYQ4=", "Android");
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(phoneJson);


        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(phoneJson));

        Request request = new Request.Builder()
                .url("https://irkkt-mobile.nalog.ru:8888/v2/auth/phone/request")
                .addHeader("Host", HOST)
                .addHeader("Accept", ACCEPT)
                .addHeader("Device-OS", DEVICE_OS)
                .addHeader("Device-ID", DEVICE_ID)
                .addHeader("clientVersion", CLIENT_VERSION)
                .addHeader("Accept-Language", ACCEPT_LANGUAGE)
                .addHeader("User-Agent", USER_AGENT)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (Response responseBody = httpClient.newCall(request).execute()) {

                    if (!responseBody.isSuccessful())
                        throw new IOException("Unexpected code " + responseBody);

                    System.out.println(responseBody.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });

    }

    public interface ResultHandler {
        void onSuccess(JsonReaderSessionID response);

        void onSuccess(String response);

        void onFail(IOException error);
    }

    public void SmsCode(String Code, ResultHandler callback){
        SmsJson smsJson = new SmsJson("+79097261795", "IyvrAbKt9h/8p6a7QPh8gpkXYQ4=", "Android", Code);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(smsJson);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(smsJson));

        Request request = new Request.Builder()
                .url("https://irkkt-mobile.nalog.ru:8888/v2/auth/phone/verify")
                .addHeader("Host", HOST)
                .addHeader("Accept", ACCEPT)
                .addHeader("Device-OS", DEVICE_OS)
                .addHeader("Device-ID", DEVICE_ID)
                .addHeader("clientVersion", CLIENT_VERSION)
                .addHeader("Accept-Language", ACCEPT_LANGUAGE)
                .addHeader("User-Agent", USER_AGENT)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (Response responseBody = httpClient.newCall(request).execute()) {

                    if (!responseBody.isSuccessful())
                        throw new IOException("Unexpected code " + responseBody);

                    //JsonReaderSessionID jsonReaderSessionID = gson.fromJson(String.valueOf(response.body()), JsonReaderSessionID.class);
                    //callback.onSuccess(jsonReaderSessionID);
                    //String res = response.body().string();
                    //callback.onSuccess(res);
                    System.out.println(Objects.requireNonNull(responseBody.body()).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public String GetTicketID(String QR){
        TicketID ticketID = new TicketID(QR);
        String json = String.format("{\"qr\":\"%s\"}",QR);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json2 = gson.toJson(ticketID);
        String json3 = gson.toJson(json);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(ticketID));
        Request request = new Request.Builder()
                .url("https://irkkt-mobile.nalog.ru:8888/v2/ticket")
                .addHeader("Host", HOST)
                .addHeader("Accept", ACCEPT)
                .addHeader("Device-OS", DEVICE_OS)
                .addHeader("Device-ID", DEVICE_ID)
                .addHeader("clientVersion", CLIENT_VERSION)
                .addHeader("Accept-Language", ACCEPT_LANGUAGE)
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("sessionId", sessionId)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (Response responseBody = httpClient.newCall(request).execute()) {
                    if (!responseBody.isSuccessful())
                        throw new IOException("Unexpected code " + responseBody);
                    System.out.println(Objects.requireNonNull(responseBody.body()).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
        return "";
    }

    public void GetTicket(String QR){
        String ticket_id = GetTicketID(QR);
        Request request = new Request.Builder()
                .url(String.format("https://irkkt-mobile.nalog.ru:8888/v2/tickets/%s", ticket_id))
                .addHeader("Host", HOST)
                .addHeader("Accept", ACCEPT)
                .addHeader("Device-OS", DEVICE_OS)
                .addHeader("Device-ID", DEVICE_ID)
                .addHeader("clientVersion", CLIENT_VERSION)
                .addHeader("Accept-Language", ACCEPT_LANGUAGE)
                .addHeader("User-Agent", USER_AGENT)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (Response responseBody = httpClient.newCall(request).execute()) {

                    if (!responseBody.isSuccessful())
                        throw new IOException("Unexpected code " + responseBody);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
