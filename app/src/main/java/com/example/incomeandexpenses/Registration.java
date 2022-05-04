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

public class Registration extends AppCompatActivity {

    Button btnReg;
    String regex = "[0-9]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnReg = (Button) findViewById(R.id.btnReg);
        TextView nameView = findViewById(R.id.editTextTextPersonName);
        TextView phoneView = findViewById(R.id.editTextPhone);
        TextView passwordView = findViewById(R.id.editTextTextPassword);
        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase database = myDataBaseHelper.getWritableDatabase();

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
                //Log.d("TAG", "---Table Users---");
                //cursor = database.query("users", null, null, null, null, null, null);
                //logCursor(cursor);
                //cursor.close();
            }
        });
    }

    void Register(String name, String phone, String password, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        Cursor cursor;
        String sqlQuery = "Select * from users where PhoneNumber == ?";

        cursor = database.rawQuery(sqlQuery, new String[] {phone});

        if (cursor.getCount() == 0) {
            contentValues.put("PhoneNumber", phone);
            contentValues.put("Password", password);
            contentValues.put("Name", name);
            database.insert("users", null, contentValues);
            GotoLogin();
        } else {
            MakeText();
        }
        cursor.close();

    }

    void GotoLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    void DataInCorrect(){
        Toast.makeText(this, "Данные введены неверно", Toast.LENGTH_SHORT).show();
    }

    void MakeText(){
        Toast.makeText(this, "Данный номер занят", Toast.LENGTH_SHORT).show();
    }


}