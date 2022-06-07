package com.example.incomeandexpenses.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.incomeandexpenses.R;
import com.example.incomeandexpenses.authorization.Login;
import com.example.incomeandexpenses.database.MyDataBaseHelper;

/**
 * Класс регистрации
 */
public class Registration extends AppCompatActivity {

    // Переменные
    Button btnReg;
    String regex = "[0-9]+";


    ///////////////////////////////////// Регион основной


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Элементы формы
        btnReg = (Button) findViewById(R.id.btnReg);
        TextView nameView = findViewById(R.id.editTextTextPersonName);
        TextView phoneView = findViewById(R.id.editTextPhone);
        TextView passwordView = findViewById(R.id.editTextTextPassword);

        // База данных
        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase database = myDataBaseHelper.getWritableDatabase();

        // Кнопка
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameView.getText().toString();
                String phone = "+7" + phoneView.getText().toString();
                String password = passwordView.getText().toString();

                if (phoneView.getText().toString().matches(regex) && phoneView.getText().toString().length() == 10){
                    Register(name, phone, password, database);
                }else{
                    DataInCorrect();
                }
            }
        });
    }


    // Регистрация
    void Register(String name, String phone, String password, SQLiteDatabase database){
        // Работа с БД
        ContentValues contentValues = new ContentValues();
        Cursor cursor;
        String sqlQuery = "Select * from users where PhoneNumber == ?";

        cursor = database.rawQuery(sqlQuery, new String[] {phone});

        // Если такой номер телефона не занят
        if (cursor.getCount() == 0) {
            contentValues.put("PhoneNumber", phone);
            contentValues.put("Password", password);
            contentValues.put("Name", name);
            database.insert("users", null, contentValues);
            GotoLogin();
        } else {
            // Если занят
            PhoneNumberIsOccupied();
        }
        cursor.close();
    }


    ///////////////////////////////////// Конец региона


    ///////////////////////////////////// Регион вспомогательный


    // Переход к логину
    void GotoLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    // Toast, неверные данные
    void DataInCorrect(){
        Toast.makeText(this, "Данные введены неверно", Toast.LENGTH_SHORT).show();
    }

    // Toast, номер занят
    void PhoneNumberIsOccupied(){
        Toast.makeText(this, "Данный номер занят", Toast.LENGTH_SHORT).show();
    }


    ///////////////////////////////////// Конец региона
}