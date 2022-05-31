package com.example.incomeandexpenses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.textclassifier.ConversationAction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

//TODO: Реализовать сканирование по QR-коду!
/**
 * Класс для считывания QR-кода чека. В РАБОТЕ!
 */
public class NalogAPIReader implements Serializable {
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
    private String TicketId = null;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
            .readTimeout(5, TimeUnit.MINUTES) // read timeout
            .build();


    /////////////////////////////////////// Конструктор


    public NalogAPIReader(){
        this.SessionId = null;
        this.PhoneNumber = null;
        this.TicketId = null;
    }


    /////////////////////////////////////// Конец региона


    /////////////////////////////////////// Регион интерфейсов


    interface CallBack {
        void onSmsSend(String sessionId);
    }

    interface Ticket {
        void getTicket(String sessionId, String ticketId);
    }

    void setSessionId(String sessionId){
        this.SessionId = sessionId;
    }

    void setTicketId(String ticketId){
        this.TicketId = ticketId;
    }


    /////////////////////////////////////// Конец региона


    /////////////////////////////////////// Регион работы с АПИ


    // Регаем номер
    public void regNumber(String phoneNumber){
        this.PhoneNumber = phoneNumber;
        PhoneJson phoneJson = new PhoneJson(PhoneNumber, ClientSecret, Os);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        // BODY запроса
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(phoneJson));

        // ЗАПРОС
        Request request = builderPhoneSms("https://irkkt-mobile.nalog.ru:8888/v2/auth/phone/request", body);

        // АСИНК запрос
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

    // ПОДТВЕРЖДЕНИЕ СМС
    public void smsCode(String Code, CallBack callBack){
        SmsJson smsJson = new SmsJson(PhoneNumber, ClientSecret, Os, Code);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        // BODY запроса
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(smsJson));

        // ЗАПРОС
        Request request = builderPhoneSms("https://irkkt-mobile.nalog.ru:8888/v2/auth/phone/verify", body);

        // АСИНК запрос
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (responseAnswer(response)){

                    // JSON
                    JsonElement element = JsonParser.parseString(response.body().string());
                    JsonObject jsonObject = element.getAsJsonObject();

                    // Досатем ID сессии
                    String sessionId = jsonObject.get("sessionId").getAsString();

                    // Устанавливаем ID сессии
                    setSessionId(sessionId);

                    // Юзаем интерфейс
                    if (callBack != null) {
                        callBack.onSmsSend(sessionId);
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }


    // Получаем ID чека по АПИ
    public void getTicketID(String QR, final String sessionId, Ticket ticket){
        TicketID ticketID = new TicketID(QR);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        // BODY
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(ticketID));

        // ЗАПРОС
        Request request = builderGetTicketId("https://irkkt-mobile.nalog.ru:8888/v2/ticket", body, sessionId);

        // АСИНК запрос
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(responseAnswer(response)){

                    // JSON
                    JsonElement element = JsonParser.parseString(response.body().string());
                    JsonObject jsonObject = element.getAsJsonObject();

                    // Устанавливаем тикет ID
                    String ticketId = jsonObject.get("id").getAsString();

                    // Устанавливаем тикет ID
                    setTicketId(ticketId);

                    // Юзаем интерфейс
                    if (ticket != null) {
                        ticket.getTicket(sessionId, ticketId);
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Получаем чек
    public void getTicket(String QR, final String sessionId, final String ticketId, MyDataBaseHelper myDataBaseHelper, SQLiteDatabase database, Users user){

        // Запрос
        Request request = builderGetTicket(String.format("https://irkkt-mobile.nalog.ru:8888/v2/tickets/%s", ticketId), sessionId);

        // АСИНК Запрос
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(responseAnswer(response)){

                    int idOperation = 0;

                    // Парсим JSON
                    JsonElement element = JsonParser.parseString(response.body().string());
                    JsonObject jsonObject = element.getAsJsonObject();

                    // Данные об операции
                    JsonObject operationDataJson = jsonObject.get("operation").getAsJsonObject();

                    // Достаем данные по операции операцию.
                    String date = operationDataJson.get("date").getAsString();              // Дата чека
                    Double sum = operationDataJson.get("sum").getAsDouble();                // Сумма чека

                    // Название операции
                    JsonObject nameOrganizationJson = jsonObject.get("organization").getAsJsonObject();

                    // Наименование организации
                    String nameOrg = nameOrganizationJson.get("name").getAsString();        // Наименование организации

                    // Изменяем дату
                    String dateSubString = date.substring(0, 10);

                    // Запихиваем ContentValues
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("IdUser", user.getIdUser());
                    contentValues.put("Name", nameOrg);
                    contentValues.put("TypeOperation", false);
                    contentValues.put("Sum", sum / 100);
                    contentValues.put("TimeStamp", dateSubString);
                    contentValues.put("Category", "БезКатегории");

                    // Инсертим в БД!
                    database.insert("Operations", null, contentValues);

                    // Поиск в БД, ищем ID операции
                    Cursor cursor;
                    String sqlQuery = "Select * from Operations where IdUser == ? and Name = ? ";
                    cursor = database.rawQuery(sqlQuery, new String[] {String.valueOf(user.getIdUser()), nameOrg});

                    // Достаем ID последней операции!
                    cursor.moveToLast();
                    if (cursor.getCount() != 0) {
                        idOperation = cursor.getInt(cursor.getColumnIndexOrThrow("IdOperation"));
                    }


                    // Создали Операцию. Теперь идем по items.


                    try {
                        // Парсим JSON для Items
                        JsonObject itemObjectsJson = jsonObject.get("ticket").getAsJsonObject().get("document").getAsJsonObject().get("receipt").getAsJsonObject();
                        JsonArray jsonArray = itemObjectsJson.get("items").getAsJsonArray();
                        // Идём по элементам
                        for (JsonElement item : jsonArray) {
                            // итератор
                            JsonObject iterator = item.getAsJsonObject();

                            // Новый контентвалуес
                            contentValues = new ContentValues();

                            // Достаем из JSON данные
                            String name = iterator.get("name").getAsString();
                            Float quantity = iterator.get("quantity").getAsFloat();
                            Float sumItem = iterator.get("sum").getAsFloat();

                            contentValues.put("IdOperations", idOperation);
                            contentValues.put("Name", name);
                            contentValues.put("Quantity", quantity);
                            contentValues.put("Sum", sumItem / 100);
                            contentValues.put("Category", "БезКатегории");

                            // Инсертим данные в БД!
                            database.insert("Items", null, contentValues);
                        }
                    }
                    catch (Exception e){
                        database.delete("Operations", "IdOperation = " + idOperation, null);
                        database.delete("Items", "IdOperation = " + idOperation, null);
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /////////////////////////////////////// Конец региона


    /////////////////////////////////////// Регион вспомогательный


    // Обработчик ошибок
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

    /////////////////////////////////////// Конец региона
}
