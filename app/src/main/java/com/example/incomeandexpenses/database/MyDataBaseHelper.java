package com.example.incomeandexpenses.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Класс для работы с БД
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {

    // Переменные
    public static final String DATABASE_NAME = "IaE";           // Имя бд
    public static final int DATABASE_VERSION = 1;               // Версия бд


    ////////////////////////////////////////// Регион основной


    // Конструктор
    public MyDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Создание БД
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createUserTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "IdUser INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL," +
                "PhoneNumber TEXT UNIQUE NOT NULL," +
                "Password TEXT NOT NULL," +
                "Name TEXT NOT NULL);";
        sqLiteDatabase.execSQL(createUserTable);

        String createOperationsTable = "CREATE TABLE IF NOT EXISTS Operations (" +
                "IdOperation INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                "IdUser INTEGER REFERENCES Users (IdUser) NOT NULL," +
                "Name TEXT NOT NULL," +
                "TypeOperation BOOLEAN NOT NULL,"+
                "TimeStamp DATETIME NOT NULL," +
                "Sum REAL NOT NULL," +
                "Category TEXT NOT NULL);";
        sqLiteDatabase.execSQL(createOperationsTable);

        String createItemTable = "CREATE TABLE IF NOT EXISTS Items  (" +
                "IdItem INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL," +
                "IdOperations INTEGER REFERENCES Operations (IdOperation) NOT NULL," +
                "Name TEXT NOT NULL," +
                "Nds INTEGER," +
                "NdsSum REAL," +
                "PaymentType INTEGER," +
                "Price REAL," +
                "Quantity REAL NOT NULL," +
                "Sum REAL NOT NULL," +
                "Category TEXT DEFAULT 'БезКатегории');";
        sqLiteDatabase.execSQL(createItemTable);
    }


    // Апргрейд БД
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    ////////////////////////////////////////// Конец региона


    ////////////////////////////////////////// Регион вспомогательный


    // Вывод значений
    public void logCursor(Cursor cursor) {
        // Если курсор не пустой
        if (cursor != null) {
            // Береём 1 элемент
            if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    // Идём по столбцам в курсоре
                    for (String cn : cursor.getColumnNames()) {
                        // Записываем в строчку
                        str = str.concat(cn + " = " + cursor.getString(cursor.getColumnIndexOrThrow(cn)) + "; ");
                    }
                    // Выводим
                    Log.d("TAG", str);
                } while (cursor.moveToNext());
            } else Log.d("TAG", "Cursor incorrect");
        }
    }


    ////////////////////////////////////////// Конец региона
}
