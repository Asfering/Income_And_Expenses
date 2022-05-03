package com.example.incomeandexpenses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    EditText txtView;
    Button btn;
    Button btn2;
    Button btnQRCode;
    Button regbtn;
    Button logBtn;
    QRcodeReader qRcodeReader = new QRcodeReader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.btn2);
        btnQRCode = (Button) findViewById(R.id.qrCodeBtn);
        txtView = (EditText) findViewById(R.id.editTextNumber);
        regbtn = (Button) findViewById(R.id.registerAcc);
        logBtn = (Button) findViewById(R.id.LogAcc);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    RegNumber(qRcodeReader);
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowLogin();
            }
        });

        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowQrCode();
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ShowRegistration();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    SmsCode(qRcodeReader);
                    qRcodeReader.GetTicket("t=20220426T1859&s=344.11&fn=9280440301393520&i=111896&fp=190653864&n=1");
            }
        });
    }

    private void SmsCode(QRcodeReader qRcodeReader){
        qRcodeReader.SmsCode(txtView.getText().toString());
    }

    private void RegNumber(QRcodeReader qRcodeReader){
        qRcodeReader.RegNumber();
    }

    private void ShowRegistration(){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    private void ShowLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void ShowQrCode(){
        Intent intent = new Intent(this, QRCodeScanner.class);
        startActivity(intent);
    }
}