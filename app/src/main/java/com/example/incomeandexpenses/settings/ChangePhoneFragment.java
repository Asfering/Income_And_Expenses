package com.example.incomeandexpenses.settings;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.incomeandexpenses.R;
import com.example.incomeandexpenses.classes.Users;
import com.example.incomeandexpenses.database.MyDataBaseHelper;

/**
 * Фрагмент для изменения номера телефона
 */
public class ChangePhoneFragment extends Fragment {

    // Класс
    Users user;


    /////////////////////////////////////// Регион основной


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_change_phone, container, false);

        // Получаем юзера
        GetUser();

        // Элементы формы
        Button btnSave = (Button) RootView.findViewById(R.id.btnChangePhone);
        TextView phoneOld = RootView.findViewById(R.id.changePhoneOld);
        TextView phoneNew = RootView.findViewById(R.id.changePhoneNew);
        TextView pass = RootView.findViewById(R.id.writePasswordOnChangePhone);

        // База данных
        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(RootView.getContext());
        SQLiteDatabase database = myDataBaseHelper.getWritableDatabase();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phOld = "+7" + phoneOld.getText().toString();
                String phNew = "+7" + phoneNew.getText().toString();
                String password = pass.getText().toString();

                if(phOld.equals(user.getPhoneNumber())){         // Если номер телефона старый = номеру телефона аккаунта
                    ChangePhoneNumber(phOld, password, database, phNew, RootView);
                }
                else{           // Иначе ошибка
                    PhoneNumberIncorrect(RootView);
                }
            }
        });

        return RootView;
    }


    // Смена номера телефона
    void ChangePhoneNumber(String phOld, String password, SQLiteDatabase database, String phNew, View RootView){
        ContentValues contentValues = new ContentValues();
        Cursor cursor;
        String sqlQuery = "Select * from users where PhoneNumber == ? and Password == ?";

        cursor = database.rawQuery(sqlQuery, new String[] {phOld, password});
        cursor.moveToFirst();

        if(cursor.getCount() != 0){             // если номер телефона и пароль совпадают, то
            if (cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")).equals(phOld)){       // проверяем ещё раз, совпадают ли номер телефона и телефон с формы
                contentValues.put("PhoneNumber", phNew);
                database.update("users",contentValues, "users.IdUser == " + user.getIdUser(), null);
                PhoneChanged(RootView);
            }
        } else{
            PasswordInCorrect(RootView);
        }

        cursor.close();
    }


    /////////////////////////////////////// Конец региона


    /////////////////////////////////////// Регион вспомогательный


    void PhoneNumberIncorrect(View RootView){
        Toast.makeText(RootView.getContext(),"Номер телефона не совпадает с номером данного пользователя", Toast.LENGTH_SHORT).show();
    }

    private void PhoneChanged(View RootView){
        Toast.makeText(RootView.getContext(),"Номер телефона успешно изменён", Toast.LENGTH_SHORT).show();
    }

    private void PasswordInCorrect(View RootView){
        Toast.makeText(RootView.getContext(),"Пароль неверный", Toast.LENGTH_SHORT).show();
    }

    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }


    /////////////////////////////////////// Конец региона
}