package com.example.incomeandexpenses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button btnLog;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLog = (Button) findViewById(R.id.btnLog);
        TextView phoneView = findViewById(R.id.etxtPhoneLog);
        TextView passwordView = findViewById(R.id.etxtPassLog);

        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase database = myDataBaseHelper.getWritableDatabase();

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
                        // переход на главную
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

    void DataInCorrect(){
        Toast.makeText(this, "Логин или пароль введены неверно", Toast.LENGTH_SHORT).show();
    }

    void DataNull(){
        Toast.makeText(this, "Данный номер телефона не зарегистрирован", Toast.LENGTH_SHORT).show();
    }
}