package com.example.incomeandexpenses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.security.Timestamp;
import org.threeten.bp.LocalDateTime;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class InsightsFragment extends Fragment {

    private RecyclerView RecView;
    private ArrayList<Operations> operations = new ArrayList<>();
    private Timestamp timestamp;
    Users user;
    MyDataBaseHelper myDataBaseHelper;
    SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_insights, container, false);

        GetUser();

        // База данных
        myDataBaseHelper= new MyDataBaseHelper(RootView.getContext());
        database = myDataBaseHelper.getWritableDatabase();

        RecView = RootView.findViewById(R.id.recView);
        // Загружаем данные из БД
        initializeData();

        OperationsAdapter.OnOperationClickListener operationClickListener = new OperationsAdapter.OnOperationClickListener() {
            @Override
            public void onOperationClick(Operations operation, int position) {
                RedirectToEdit(operation);
                //Toast.makeText(RootView.getContext(), "Был выбран пункт " + operation.getName(),
                //        Toast.LENGTH_SHORT).show();
            }
        };


        OperationsAdapter operationsAdapter = new OperationsAdapter(RootView.getContext(), operations, operationClickListener);
        RecView.setLayoutManager(new LinearLayoutManager(RootView.getContext()));
        RecView.setAdapter(operationsAdapter);
        // Inflate the layout for this fragment
        return RootView;
    }

    // Новый фрагмент на смену имени
    private void RedirectToEdit(Operations operation){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Users.class.getSimpleName(), user);      // Для передачи юзера во фрагмент
        bundle.putSerializable(Operations.class.getSimpleName(), operation);
        Fragment nextFrag= new ChangingDataFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFrag)
                .addToBackStack("")
                .commit();
    }

    private void initializeData(){
        Cursor cursor;
        String sqlQuery = "Select * from Operations where IdUser == ? ORDER BY TimeStamp DESC";
        cursor = database.rawQuery(sqlQuery, new String[] {String.valueOf(user.getIdUser())});



        if(cursor != null){
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                boolean value = cursor.getInt(cursor.getColumnIndexOrThrow("TypeOperation")) != 0;
                String val = cursor.getString(cursor.getColumnIndexOrThrow("TimeStamp"));
                Date sqlDate1 = Date.valueOf(val);
                Operations operation = new Operations(cursor.getInt(cursor.getColumnIndexOrThrow("IdOperation")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("IdUser")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        value,
                        sqlDate1,
                        cursor.getFloat(cursor.getColumnIndexOrThrow("Sum")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Category")));
                operations.add(operation);
                cursor.moveToNext();
            }
            }
        }


    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }
}