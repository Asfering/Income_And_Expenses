package com.example.incomeandexpenses.addelements;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.incomeandexpenses.Basic;
import com.example.incomeandexpenses.R;
import com.example.incomeandexpenses.classes.Users;
import com.example.incomeandexpenses.database.MyDataBaseHelper;
import com.google.zxing.Result;

/**
 * Сканнер QR-кодов
 */
public class QRCodeScanner extends AppCompatActivity {

    // Классы
    Users user;
    NalogAPIReader nalogAPIReader = new NalogAPIReader();

    // База данных
    MyDataBaseHelper myDataBaseHelper;
    SQLiteDatabase database;

    // Вспомогательные переменные
    private CodeScanner codeScanner;
    EditText editText;
    String qrCode = "";


    ///////////////////////////////// Регион основной


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        // Сканнируем
        CodeScannerView scannerView = findViewById(R.id.scanner_view_Scan);
        codeScanner = new CodeScanner(this, scannerView);

        // EditText
        editText = findViewById(R.id.editTextSMSCode);

        // Получаем юзера
        getUser();

        // Определяем БД
        myDataBaseHelper= new MyDataBaseHelper(this);
        database = myDataBaseHelper.getWritableDatabase();

        // Просканировали
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       try {
                           qrCode = result.getText().toString();            // достаём кр код в виде текста
                           RegNumber(nalogAPIReader);                       // регаем номер
                       }catch (Exception e){
                           errorNumber(e);
                       }
                    }
                });
            }
        });

        // Определяем кнопку
        Button btnCheckAPI = findViewById(R.id.btnCheckAPI);

        btnCheckAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    SmsCode(nalogAPIReader);            // Отправляем СМС код к АПИ
                    redirectToBasic();                  // Переходим на главную
                } catch (Exception e){
                    errorQRCode();
                }
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }


    ///////////////////////////////// Конец региона


    ///////////////////////////////// Регион вспомогательный

    // Переход на главную
    private void redirectToBasic(){
        Intent intent = new Intent(this, Basic.class);
        intent.putExtra(Users.class.getSimpleName(), user);
        startActivity(intent);
    }

    // Toast с ошибкой
    private void errorQRCode(){
        Toast.makeText(this, "Ошибка при считывании данных QR-Кода", Toast.LENGTH_SHORT).show();
    }

    // Работа с СМС кодом
    private void SmsCode(NalogAPIReader nalogAPIReader){
        nalogAPIReader.smsCode(editText.getText().toString(), new NalogAPIReader.CallBack() {
            @Override
            public void onSmsSend(String sessionId) {
                nalogAPIReader.getTicketID(qrCode, sessionId, new NalogAPIReader.Ticket() {
                    @Override
                    public void getTicket(String sessionId, String ticketId) {
                        nalogAPIReader.getTicket(qrCode, sessionId, ticketId, myDataBaseHelper, database, user);
                    }
                });
            }
        });
    }

    // получаю юзверя
    private void getUser(){
        Bundle arguments = getIntent().getExtras();
        user = (Users) arguments.getSerializable(Users.class.getSimpleName());
    }

    // Регаем номер
    private void RegNumber(NalogAPIReader nalogAPIReader){
        // Тут будет передавать номер из Fragment ADD
        nalogAPIReader.regNumber(user.getPhoneNumber());
    }

    // Ошибка
    private void errorNumber(Exception e){
        System.out.println(e.toString());
        Toast.makeText(this, "Ошибка номера" + e, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume(){
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause(){
        codeScanner.releaseResources();
        super.onPause();
    }

    ///////////////////////////////// Конец региона
}