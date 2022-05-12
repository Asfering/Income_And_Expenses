package com.example.incomeandexpenses;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

///
/// ПОТОМ УДАЛЮ
///
public class testFragmentTableLayout extends Fragment {

    Users user;
    View RootView;
    TableLayout tableLayout;
    TableRow tableRowZero;
    int counterRows;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_test_table_layout, container, false);

        GetUser();
        Button testingBtnTable = RootView.findViewById(R.id.testingBtnTable);
        Button checkinfo = RootView.findViewById(R.id.checkinfo);
        testingBtnTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRow();
            }
        });

        checkinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 1; i<counterRows; i++){
                    TableRow tbRow = (TableRow)tableLayout.getChildAt(i);
                    for(int j =0; j<4; j++){
                        EditText ed = (EditText)tbRow.getChildAt(j);
                        System.out.println(ed.getText().toString());
                    }

                }
            }
        });
        // Объявляем таблицу
        tableLayout = RootView.findViewById(R.id.table_main1);
        tableRowZero = new TableRow(RootView.getContext());
        // Строим столбцы
        createColumns();
        return RootView;
    }

    public void createRow() {
        // Объявляем строку
        TableRow tbrow = new TableRow(RootView.getContext());
        // Первый столбец
        EditText t1v = new EditText(RootView.getContext());
        t1v.setHint("Наименование");
        t1v.setGravity(Gravity.CENTER);
        tbrow.addView(t1v);
        // Второй столбец
        EditText t2v = new EditText(RootView.getContext());
        t2v.setHint("Количество");
        t2v.setGravity(Gravity.CENTER);
        tbrow.addView(t2v);
        // Третий столбец
        EditText t3v = new EditText(RootView.getContext());
        t3v.setHint("Сумма");
        t3v.setGravity(Gravity.CENTER);
        tbrow.addView(t3v);
        // Четвертый столбец
        EditText t4v = new EditText(RootView.getContext());
        t4v.setHint("Категория");
        t4v.setGravity(Gravity.CENTER);
        tbrow.addView(t4v);
        // Добавляем строку в таблицу
        tableLayout.addView(tbrow);
        counterRows++;
    }

    public void createColumns() {
        // Столбец 0.
        TextView tv0 = new TextView(RootView.getContext());
        tv0.setText(" Наименование ");
        tableRowZero.addView(tv0);
        // Столбец 1.
        TextView tv1 = new TextView(RootView.getContext());
        tv1.setText(" Количество ");
        tableRowZero.addView(tv1);
        // Столбец 2.
        TextView tv2 = new TextView(RootView.getContext());
        tv2.setText(" Сумма ");
        tableRowZero.addView(tv2);
        // Столбец 3.
        TextView tv3 = new TextView(RootView.getContext());
        tv3.setText(" Категория ");
        tableRowZero.addView(tv3);
        // Запихиваем в таблицу строку.
        tableLayout.addView(tableRowZero);
        counterRows++;
    }

    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }
}