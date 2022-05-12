package com.example.incomeandexpenses;

import static android.text.InputType.TYPE_CLASS_TEXT;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


public class InputByHandFragment extends Fragment {

    Users user;
    TableLayout tableLayout;
    TableRow tableRowZero;
    View RootView;
    int counterRows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_input_by_hand, container, false);

        GetUser();

        EditText nameOfOperation = RootView.findViewById(R.id.nameOfOperation);     // Имя операции
        EditText day = RootView.findViewById(R.id.day);                             // День
        EditText month = RootView.findViewById(R.id.month);                         // Месяц
        EditText year = RootView.findViewById(R.id.year);                           // Год
        RadioButton rbIncome = RootView.findViewById(R.id.radioIncome);             // Кнопка "Доходы"
        RadioButton rbExpenses = RootView.findViewById(R.id.radioExpenses);         // Кнопка "Расходы"
        Spinner spinner = RootView.findViewById(R.id.spinner);                      // Выпадающий список с категориями
        Button addRowToTable = RootView.findViewById(R.id.addRowToTable);           // Добавить строку в таблицу
        Button addItemToDataBase = RootView.findViewById(R.id.addItemToDataBase);   // Добавить операцию в БД.

        rbIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Настраиваем адаптер
                ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(RootView.getContext(), R.array.ExpensesCategory,
                                android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Вызываем адаптер
                spinner.setAdapter(adapter);

            }
        });

        rbExpenses.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(RootView.getContext(), R.array.IncomesCategory,
                        android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Вызываем адаптер
                spinner.setAdapter(adapter);
            }
        });

        Calendar calendar = Calendar.getInstance();

        day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        month.setText(getMonth(calendar));
        year.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        addRowToTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRow();
            }
        });


        // Объявляем таблицу
        tableLayout = RootView.findViewById(R.id.table_main);
        tableRowZero = new TableRow(RootView.getContext());
        // Строим столбцы
        createColumns();

        return RootView;
    }

    // Метод для получения месяца из Calendar.
    private String getMonth(Calendar calendar){
        String tempReturn = "";
        int month = Integer.parseInt(String.valueOf(calendar.get(Calendar.MONTH)));
        if(month < 9){
            month++;
            tempReturn="0" + String.valueOf(month);
        } else if (month > 8 && month < 12) {
            tempReturn=String.valueOf(month++);
        }
        return tempReturn;
    }

    // Метод для передачи месяца в Calendar
    private String setMonth(String monthStr){
        String tempReturn = "";
        int month = Integer.parseInt(monthStr);
        //int month = Integer.parseInt(String.valueOf(calendar.get(Calendar.MONTH)));
        month--;
        tempReturn = String.valueOf(month);
        return tempReturn;
    }

    // Делаем строки
    public void createRow() {
        // Объявляем строку
        TableRow tbrow = new TableRow(RootView.getContext());
        // Первый столбец, наименование
        EditText t1v = new EditText(RootView.getContext());
        t1v.setInputType(InputType.TYPE_CLASS_TEXT);
        t1v.setGravity(Gravity.CENTER);
        tbrow.addView(t1v);
        // Второй столбец, количество
        EditText t2v = new EditText(RootView.getContext());
        t2v.setGravity(Gravity.CENTER);
        t2v.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tbrow.addView(t2v);
        // Третий столбец, сумма
        EditText t3v = new EditText(RootView.getContext());
        t3v.setGravity(Gravity.CENTER);
        t3v.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tbrow.addView(t3v);
        // Добавляем строку в таблицу
        tableLayout.addView(tbrow);
        counterRows++;
    }

    // Делаем наименования столбцов в таблице
    public void createColumns() {
        // Столбец 1.
        TextView tv1 = new TextView(RootView.getContext());
        tv1.setText(" Наименование ");
        tableRowZero.addView(tv1);
        // Столбец 2.
        TextView tv2 = new TextView(RootView.getContext());
        tv2.setText(" Количество ");
        tableRowZero.addView(tv2);
        // Столбец 3.
        TextView tv3 = new TextView(RootView.getContext());
        tv3.setText(" Сумма ");
        tableRowZero.addView(tv3);
        // Запихиваем в таблицу строку.
        tableLayout.addView(tableRowZero);
        counterRows++;
    }

    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }
}