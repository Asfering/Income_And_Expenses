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

import com.example.incomeandexpenses.Basic;
import com.example.incomeandexpenses.R;
import com.example.incomeandexpenses.classes.Users;
import com.example.incomeandexpenses.database.MyDataBaseHelper;

/**
 * Класс для работы с авторизацией пользователя
 */
public class Login extends AppCompatActivity {

    // Элементы формы
    Button btnLog;
    Button btnLogReg;

    // Классы
    Users user;


    //////////////////////////////////////////// Регион основной


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Определяем элементы формы
        btnLog = (Button) findViewById(R.id.btnLog);
        btnLogReg = (Button) findViewById(R.id.btnLogReg);
        TextView phoneView = findViewById(R.id.etxtPhoneLog);
        TextView passwordView = findViewById(R.id.etxtPassLog);

        // База данных
        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase database = myDataBaseHelper.getWritableDatabase();


        // Переход на регистрацию
        btnLogReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToRegister();
            }
        });

        // Логин
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Данные по логину
                String phone = "+7" + phoneView.getText().toString();
                String password = passwordView.getText().toString();

                // Запрос
                ContentValues contentValues = new ContentValues();
                Cursor cursor;
                String sqlQuery = "Select * from users where PhoneNumber == ? and Password == ?";

                cursor = database.rawQuery(sqlQuery, new String[] {phone, password});
                cursor.moveToFirst();

                // Если элемент найден
                if (cursor.getCount() != 0) {
                    // Если всё сходится
                    if (cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")).equals(phone) &&
                            cursor.getString(cursor.getColumnIndexOrThrow("Password")).equals(password)) {
                        // Создаем юзера в приложении
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


    //////////////////////////////////////////// Конец региона


    //////////////////////////////////////////// Регион вспомогательный


    // Переход на регистрацию
    void RedirectToRegister(){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    // Переход на Basic активити
    void GotoBasic(Users user){
        Intent intent = new Intent(this, Basic.class);
        intent.putExtra(Users.class.getSimpleName(), user);
        startActivity(intent);
    }

    // Toast
    void DataInCorrect(){
        Toast.makeText(this, "Логин или пароль введены неверно", Toast.LENGTH_SHORT).show();
    }

    // Toast
    void DataNull(){
        Toast.makeText(this, "Данный номер телефона не зарегистрирован", Toast.LENGTH_SHORT).show();
    }

    // Выход из приложения
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }


    //////////////////////////////////////////// Конец региона
}