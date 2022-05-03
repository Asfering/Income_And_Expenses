package com.example.incomeandexpenses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "IaE";
    public static final int DATABASE_VERSION = 1;


    public MyDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createUserTable = "CREATE TABLE Users (" +
                "IdUser INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL," +
                "PhoneNumber TEXT UNIQUE NOT NULL," +
                "Password TEXT NOT NULL," +
                "Name TEXT NOT NULL);";
        sqLiteDatabase.execSQL(createUserTable);

        String createReceiptsTable = "CREATE TABLE Receipts (" +
                "IdReceipt INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                "IdUser INTEGER REFERENCES Users (IdUser) NOT NULL," +
                "TimeStamp DATETIME," +
                "QRCode TEXT);";
        sqLiteDatabase.execSQL(createReceiptsTable);

        String createItemTable = "CREATE TABLE Items (" +
                "IdItem INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL," +
                "IdReceipt INTEGER REFERENCES Receipts (IdReceipt) NOT NULL," +
                "Name TEXT NOT NULL," +
                "Nds INTEGER," +
                "NdsSum INTEGER," +
                "PaymentType INTEGER," +
                "Price INTEGER," +
                "Quantity INTEGER NOT NULL," +
                "Sum INTEGER NOT NULL," +
                "Category TEXT DEFAULT 'БезКатегории');";
        sqLiteDatabase.execSQL(createItemTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Вывод значений
    void logCursor(Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : cursor.getColumnNames()) {
                        str = str.concat(cn + " = " + cursor.getString(cursor.getColumnIndexOrThrow(cn)) + "; ");
                    }
                    Log.d("TAG", str);
                } while (cursor.moveToNext());
            } else Log.d("TAG", "Cursor incorrect");
        }

    }
}
