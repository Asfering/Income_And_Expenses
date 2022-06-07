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
 * Фрагмент для изменения пароля
 */
public class ChangePassFragment extends Fragment {

    // Класс
    Users user;


    /////////////////////////////////////// Регион основной


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Получаем пользователя
        GetUser();

        View RootView = inflater.inflate(R.layout.fragment_change_pass, container, false);

        // Элементы формы
        Button btnSave = (Button) RootView.findViewById(R.id.btnChangePassword);
        TextView passwordOld = RootView.findViewById(R.id.changePasswordOld);
        TextView passwordNew = RootView.findViewById(R.id.changePasswordNew);

        // База данных
        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(RootView.getContext());
        SQLiteDatabase database = myDataBaseHelper.getWritableDatabase();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String psNew = passwordNew.getText().toString();
                String psOld = passwordOld.getText().toString();

                ContentValues contentValues = new ContentValues();
                Cursor cursor;
                String sqlQuery = "Select * from users where PhoneNumber == ? and Password == ?";

                cursor = database.rawQuery(sqlQuery, new String[] {user.getPhoneNumber(), psOld});
                cursor.moveToFirst();

                if(cursor.getCount() != 0){
                    if (cursor.getString(cursor.getColumnIndexOrThrow("Password")).equals(psOld)){
                        contentValues.put("Password", psNew);
                        database.update("users",contentValues, "users.IdUser == " + user.getIdUser(), null);
                        PasswordChanged(RootView);
                    }
                } else{
                    PasswordInCorrect(RootView);
                }

                cursor.close();
            }
        });

        return RootView;
    }


    /////////////////////////////////////// Конец региона


    /////////////////////////////////////// Регион вспомогательный


    private void PasswordChanged(View RootView){
        Toast.makeText(RootView.getContext(),"Пароль успешно изменён", Toast.LENGTH_SHORT).show();
    }

    private void PasswordInCorrect(View RootView){
        Toast.makeText(RootView.getContext(),"Пароль данного аккаунта неверный", Toast.LENGTH_SHORT).show();
    }

    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }


    /////////////////////////////////////// Конец региона
}