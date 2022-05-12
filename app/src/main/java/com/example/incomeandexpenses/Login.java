package com.example.incomeandexpenses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class Login extends AppCompatActivity {

    Button btnLog;
    Button btnLogReg;
    Users user;

    // подумать над выходом из приложения!
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLog = (Button) findViewById(R.id.btnLog);
        btnLogReg = (Button) findViewById(R.id.btnLogReg);
        TextView phoneView = findViewById(R.id.etxtPhoneLog);
        TextView passwordView = findViewById(R.id.etxtPassLog);

        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase database = myDataBaseHelper.getWritableDatabase();

        //myDataBaseHelper.dropReceipts(database);


        btnLogReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToRegister();
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+7" + phoneView.getText().toString();
                String password = passwordView.getText().toString();

                ContentValues contentValues = new ContentValues();
                Cursor cursor;
                String sqlQuery = "Select * from users where PhoneNumber == ? and Password == ?";

                cursor = database.rawQuery(sqlQuery, new String[] {phone, password});
                cursor.moveToFirst();

                if (cursor.getCount() != 0) {
                    if (cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")).equals(phone) &&
                            cursor.getString(cursor.getColumnIndexOrThrow("Password")).equals(password)) {
                        user = new Users(cursor.getInt(cursor.getColumnIndexOrThrow("IdUser")),
                                cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")),
                                cursor.getString(cursor.getColumnIndexOrThrow("Password")),
                                cursor.getString(cursor.getColumnIndexOrThrow("Name")));
                        GotoBasic(user);
                    } else {
                        DataInCorrect();
                    }
                }else {
                    DataNull();
                }

                cursor.close();
            }
        });

    }

    void RedirectToRegister(){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    void GotoBasic(Users user){
        Intent intent = new Intent(this, Basic.class);
        intent.putExtra(Users.class.getSimpleName(), user);
        startActivity(intent);
    }

    void DataInCorrect(){
        Toast.makeText(this, "Логин или пароль введены неверно", Toast.LENGTH_SHORT).show();
    }

    void DataNull(){
        Toast.makeText(this, "Данный номер телефона не зарегистрирован", Toast.LENGTH_SHORT).show();
    }
}