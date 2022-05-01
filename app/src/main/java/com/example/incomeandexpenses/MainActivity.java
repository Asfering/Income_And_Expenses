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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.btn2);
        btnQRCode = (Button) findViewById(R.id.qrCodeBtn);
        txtView = (EditText) findViewById(R.id.editTextNumber);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    QRcodeReader qRcodeReader = new QRcodeReader();
                    qRcodeReader.RegNumber();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showQrCode();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    QRcodeReader qRcodeReader = new QRcodeReader();
                    qRcodeReader.SmsCode(txtView.getText().toString(), new QRcodeReader.ResultHandler() {
                        @Override
                        public void onSuccess(JsonReaderSessionID response) {
                            //qRcodeReader.sessionId = response.getSessionId();
                           // qRcodeReader.sessionId =
                        }

                        @Override
                        public void onSuccess(String response) {

                        }

                        @Override
                        public void onFail(IOException error) {

                        }
                    });
                    qRcodeReader.GetTicket("t=20220426T1859&s=344.11&fn=9280440301393520&i=111896&fp=190653864&n=1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void showQrCode(){
        Intent intent = new Intent(this, QRCodeScanner.class);
        startActivity(intent);
    }
}