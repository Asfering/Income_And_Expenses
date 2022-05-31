package com.example.incomeandexpenses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.io.Serializable;

/**
 * Сканнер QR-кодов
 */
public class QRCodeScanner extends AppCompatActivity {
    private CodeScanner codeScanner;
    NalogAPIReader nalogAPIReader = new NalogAPIReader();
    EditText editText;
    String qrCode = "";
    Users user;

    MyDataBaseHelper myDataBaseHelper;
    SQLiteDatabase database;


    //todo: тут допиилить осталось малость

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        CodeScannerView scannerView = findViewById(R.id.scanner_view_Scan);
        codeScanner = new CodeScanner(this, scannerView);

        editText = findViewById(R.id.editTextSMSCode);

        getUser();

        myDataBaseHelper= new MyDataBaseHelper(this);
        database = myDataBaseHelper.getWritableDatabase();

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // todo: исправить, баг с редиректом, ему не нравится nalogApiReader
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

        Button btnCheckAPI = findViewById(R.id.btnCheckAPI);

        btnCheckAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    SmsCode(nalogAPIReader);
                    redirectToBasic();
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

    // Переход на главную
    private void redirectToBasic(){
        Intent intent = new Intent(this, Basic.class);
        intent.putExtra(Users.class.getSimpleName(), user);
        startActivity(intent);
    }

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
}