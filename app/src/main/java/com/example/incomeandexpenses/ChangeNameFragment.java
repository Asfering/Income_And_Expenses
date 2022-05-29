package com.example.incomeandexpenses;

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


/**
 * Фрагмент для изменения имени
 */
public class ChangeNameFragment extends Fragment {

    Users user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_change_name, container, false);

        GetUser();

        Button btnSave = (Button) RootView.findViewById(R.id.btnChangeName);
        TextView name = RootView.findViewById(R.id.changeNameToNew);
        TextView password = RootView.findViewById(R.id.usePasswordInChangingName);

        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(RootView.getContext());
        SQLiteDatabase database = myDataBaseHelper.getWritableDatabase();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = name.getText().toString();
                String pass = password.getText().toString();

                ContentValues contentValues = new ContentValues();
                Cursor cursor;
                String sqlQuery = "Select * from users where PhoneNumber == ? and Password == ?";

                cursor = database.rawQuery(sqlQuery, new String[] {user.getPhoneNumber(), pass});
                cursor.moveToFirst();

                if(cursor.getCount() != 0){         // Если пароль верный (пользователь найден)
                    contentValues.put("Name", newName);
                    database.update("users",contentValues, "users.IdUser == " + user.getIdUser(), null);
                    NameChanged(RootView);
                } else{
                    PasswordInCorrect(RootView);
                }

                cursor.close();
            }
        });
        return RootView;
    }

    private void NameChanged(View RootView){
        Toast.makeText(RootView.getContext(),"Имя успешно изменено", Toast.LENGTH_SHORT).show();
    }

    private void PasswordInCorrect(View RootView){
        Toast.makeText(RootView.getContext(),"Пароль неверный", Toast.LENGTH_SHORT).show();
    }

    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }
}