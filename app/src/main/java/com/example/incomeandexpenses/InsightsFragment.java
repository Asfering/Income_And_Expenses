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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Timestamp;
import org.threeten.bp.LocalDateTime;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Стартовая страница, доходы и расходы тут
 */
public class InsightsFragment extends Fragment {


    //////////////////////////////////////// Регион переменных

    // Списки
    private RecyclerView RecView;
    private ArrayList<Operations> operations = new ArrayList<>();
    private ArrayList<Operations> operationsFiltering = new ArrayList<>();

    // Переменные
    private int insightsSum;    // расходы, сумма общая
    private int incomesSum;     // доходы, сумма общая

    // Данные, пришедшие с прошлого фрагмента
    Users user;

    // База данных
    MyDataBaseHelper myDataBaseHelper;
    SQLiteDatabase database;

    // Дата для фильтра
    EditText editMonth;
    EditText editYear;

    // Расходы и доходы общие
    TextView insightsTextView;
    TextView incomesTextView;

    // RootView
    View RootView;


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Регион основной


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_insights, container, false);

        GetUser();

        Button editBtn = RootView.findViewById(R.id.editBtn);

        // База данных
        myDataBaseHelper= new MyDataBaseHelper(RootView.getContext());
        database = myDataBaseHelper.getWritableDatabase();

        // Месяц, год. Для фильтрации.
        editMonth = RootView.findViewById(R.id.monthData);
        editYear = RootView.findViewById(R.id.yearData);

        // Расходы и доходы общие
        insightsTextView = RootView.findViewById(R.id.insightsTextView);
        incomesTextView = RootView.findViewById(R.id.incomesTextView);

        Button insBtn = RootView.findViewById(R.id.insightsBtn);
        Button incBtn = RootView.findViewById(R.id.incomesBtn);

        // TODO: Тут баг. При возвращении с нового афрагмента месяц автоматом грузится текущий, а не тот, который был введен ранее!
        // Получаем текущую дату

        Calendar calendar = Calendar.getInstance();

        editMonth.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        editYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        RecView = RootView.findViewById(R.id.recView);
        // Загружаем данные из БД
        if(operations.size() == 0){
            initializeData();
        }

        // Фильтруем данные
        getDataByFilters();

        // Кнопка расходы
        insBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToCategories(false, insightsSum, getStringMonth(editMonth), editYear.getText().toString());
            }
        });

        // Кнопка доходы
        incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToCategories(true, incomesSum, getStringMonth(editMonth), editYear.getText().toString());
            }
        });

        // Кнопка ИЗМ.
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataByFilters();
            }
        });


        // Inflate the layout for this fragment
        return RootView;
    }


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Регион работы с данными


    // Фильтруем данные по месяцам и годам
    private void getDataByFilters(){
        String strDate;
        String strMonth;
        String strYear;
        operationsFiltering.clear();
        for(int id = 0; id<operations.size(); id++){
            strDate = String.valueOf(operations.get(id).getTimeStamp());
            strMonth = strDate.substring(5,7);
            strYear = strDate.substring(0,4);

            if(Integer.parseInt(String.valueOf(editYear.getText())) == Integer.parseInt(strYear)){
                if(Integer.parseInt(String.valueOf(editMonth.getText())) == Integer.parseInt(strMonth)){
                    operationsFiltering.add(operations.get(id));
                }
            }
        }

        OperationsAdapter.OnOperationClickListener operationClickListener = new OperationsAdapter.OnOperationClickListener() {
            @Override
            public void onOperationClick(Operations operation, int position) {
                RedirectToEdit(operation);
            }
        };


        OperationsAdapter operationsAdapter = new OperationsAdapter(RootView.getContext(), operationsFiltering, operationClickListener);
        RecView.setLayoutManager(new LinearLayoutManager(RootView.getContext()));
        RecView.setAdapter(operationsAdapter);

        sumAllData();
    }

    // Получаем весь список операций
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


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Регион переходов


    // Новый фрагмент на изменение
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

    // Переходим в расходы/доходы по категориям
    private void RedirectToCategories(boolean type, int allCost, String month, String year){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Users.class.getSimpleName(), user);      // Для передачи юзера во фрагмент
        bundle.putSerializable(Operations.class.getSimpleName(), operationsFiltering);
        bundle.putBoolean("typeOperation", type);
        bundle.putInt("allCost", allCost);
        bundle.putString("month", month);
        bundle.putString("year", year);
        Fragment nextFrag= new CategoriesFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFrag)
                .addToBackStack("")
                .commit();
    }


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Вспомогательный регион


    // Получение месяца
    private String getStringMonth(EditText month){
        int monthInteger = Integer.parseInt(String.valueOf(month.getText()));
        switch(monthInteger){
            case 1: return "январь";
            case 2: return "февраль";
            case 3: return "март";
            case 4: return "апрель";
            case 5: return "май";
            case 6: return "июнь";
            case 7: return "июль";
            case 8: return "август";
            case 9: return "сентябрь";
            case 10: return "октябрь";
            case 11: return "ноябрь";
            case 12: return "декабрь";
        }
        return "месяц";
    }

    // Получаем юзера
    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }

    // Функция подсчёта всех расходов и доходов в месяце
    private void sumAllData(){
        insightsSum = 0;
        incomesSum = 0;

        // Считаем!
        for(int id = 0; id<operationsFiltering.size(); id++){
            // Если расход
            if(operationsFiltering.get(id).getTypeOperation() == false){        // Если расход
                insightsSum += operationsFiltering.get(id).getSum();
            }else if (operationsFiltering.get(id).getTypeOperation()){          // Если доход
                incomesSum += operationsFiltering.get(id).getSum();
            }
        }

        // Расходы
        if(insightsSum == 0){
            insightsTextView.setText("0 ₽");
        }else{
            insightsTextView.setText(String.valueOf(insightsSum) + " ₽");
        }

        // Доходы
        if(insightsSum == 0){
            incomesTextView.setText("0 ₽");
        }else{
            incomesTextView.setText(String.valueOf(incomesSum) + " ₽");
        }
    }

    //////////////////////////////////////// Конец региона
}