package com.example.incomeandexpenses;

import static android.text.InputType.TYPE_CLASS_TEXT;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

/**
 * Класс для внесения данных руками
 */
public class InputByHandFragment extends Fragment {

    // Классы
    Users user;
    Operations operations;

    // Таблица
    TableLayout tableLayout;
    TableRow tableRowZero;

    // Вьюха
    View RootView;

    // Переменные
    int counterRows;
    float finalSum;
    boolean TypeOperation;


    // БД
    MyDataBaseHelper myDataBaseHelper;
    SQLiteDatabase database;

    // Элементы формы
    EditText nameOfOperation;
    EditText day;
    EditText month;
    EditText year;
    EditText sum;
    RadioButton rbIncome;
    RadioButton rbExpenses;
    Spinner spinner;
    Button addRowToTable;
    Button sumTables;
    Button addItemToDataBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_input_by_hand, container, false);

        GetUser();
        TypeOperation = false;
        nameOfOperation = RootView.findViewById(R.id.nameOfOperation);     // Имя операции
        day = RootView.findViewById(R.id.day);                             // День
        month = RootView.findViewById(R.id.month);                         // Месяц
        year = RootView.findViewById(R.id.year);                           // Год
        sum = RootView.findViewById(R.id.finalSum);                        // Итоговая сумма
        rbIncome = RootView.findViewById(R.id.radioIncome);             // Кнопка "Доходы"
        rbExpenses = RootView.findViewById(R.id.radioExpenses);         // Кнопка "Расходы"
        spinner = RootView.findViewById(R.id.spinner);                      // Выпадающий список с категориями
        addRowToTable = RootView.findViewById(R.id.addRowToTable);           // Добавить строку в таблицу
        addItemToDataBase = RootView.findViewById(R.id.addItemToDataBase);   // Добавить операцию в БД.
        sumTables = RootView.findViewById(R.id.SumTables);                   // Кнопка для подсчёта суммы в item's ниже. Надо сделать обработчик исключений, если Item нет!

        // База данных
        myDataBaseHelper= new MyDataBaseHelper(RootView.getContext());
        database = myDataBaseHelper.getWritableDatabase();

        // Радиобатон "Доход"
        rbIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rbIncome.isChecked()){
                    adapterRadioButton(true);
                }
            }
        });

        // Радиобатон "Расход"
        rbExpenses.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rbExpenses.isChecked()){
                    adapterRadioButton(false);
                }
            }
        });

        // Получаем текущую дату
        Calendar calendar = Calendar.getInstance();

        day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        month.setText(getMonth(calendar));
        year.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        // Добавляем строку в таблице
        addRowToTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRow();
            }
        });

        // Добавляем записи в БД
        addItemToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addItemsToDB();
                }
                catch (Exception e) {
                    errorInputDatabase();
                }
            }
        });

        // Рассчитать сумму общую.
        sumTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sumData();
                }
                catch (Exception e) {
                    tableInCorrect();
                }
            }
        });


        // Объявляем таблицу
        tableLayout = RootView.findViewById(R.id.table_main);
        tableRowZero = new TableRow(RootView.getContext());
        // Строим столбцы
        createColumns();

        return RootView;
    }


    ///////////////////////// Регион работы с БД

    // Инсертим данные в таблицу Operations
    private void addOperationsToDB(String name, boolean TypeOperation, Date timestamp, float sum, String category){
        // Объявления
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Параметры
        contentValues.put("IdUser", user.getIdUser());
        contentValues.put("Name", name);
        contentValues.put("TypeOperation", TypeOperation);
        contentValues.put("TimeStamp", dateFormat.format(timestamp));
        contentValues.put("Sum", sum);
        contentValues.put("Category", category);
        // Добавляем запись в БД
        database.insert("Operations", null, contentValues);

        // Поиск в БД, ищем ID операции
        Cursor cursor;
        String sqlQuery = "Select * from Operations where IdUser == ? and Name = ? and TimeStamp = ? ";
        cursor = database.rawQuery(sqlQuery, new String[] {String.valueOf(user.getIdUser()), name, dateFormat.format(timestamp)});

        // Берем последний элемент в БД
        cursor.moveToLast();
        if (cursor.getCount() != 0) {
            // Создаем новый элемент класса Operations
            operations = new Operations(cursor.getInt(cursor.getColumnIndexOrThrow("IdOperation")), user.getIdUser(), name, TypeOperation, timestamp, sum, category);
        }
        myDataBaseHelper.logCursor(cursor);
        cursor.close();
    }

    // Инсертим данные в таблицу Items
    private void addItemsToDB(TableRow tableRow){

        try {
            EditText name = (EditText) tableRow.getChildAt(0);
            EditText count = (EditText) tableRow.getChildAt(1);
            EditText sum = (EditText) tableRow.getChildAt(2);

            ContentValues contentValues = new ContentValues();

            if(name.getText().toString().equals(""))
                throw new Exception();

            contentValues.put("IdOperations", operations.getIdOperation());
            contentValues.put("Name", name.getText().toString());
            contentValues.put("Quantity", Float.parseFloat(count.getText().toString()));
            contentValues.put("Sum", Float.parseFloat(sum.getText().toString()));

            database.insert("Items", null, contentValues);
        }catch (Exception e) {
            database.delete("Operations", "IdOperation = " + operations.getIdOperation(), null);
            tableInCorrect();
        }

    }

    ///////////////////////// Конец региона


    ///////////////////////// Регион создания таблицы элементов чека

    // Делаем строки
    public void createRow() {
        // Объявляем строку
        TableRow tbrow = new TableRow(RootView.getContext());
        // Первый столбец, наименование
        EditText t1v = new EditText(RootView.getContext());
        t1v.setInputType(InputType.TYPE_CLASS_TEXT);
        t1v.setGravity(Gravity.CENTER);
        t1v.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
        tbrow.addView(t1v);
        // Второй столбец, количество
        EditText t2v = new EditText(RootView.getContext());
        t2v.setGravity(Gravity.CENTER);
        t2v.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        t2v.setFilters(new InputFilter[] {new InputFilter.LengthFilter(8)});
        tbrow.addView(t2v);
        // Третий столбец, сумма
        EditText t3v = new EditText(RootView.getContext());
        t3v.setGravity(Gravity.CENTER);
        t3v.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        t3v.setFilters(new InputFilter[] {new InputFilter.LengthFilter(8)});
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
        tv3.setText(" Общая сумма ");
        tableRowZero.addView(tv3);
        // Запихиваем в таблицу строку.
        tableLayout.addView(tableRowZero);
        counterRows++;
    }

    ///////////////////////// Конец региона

    ///////////////////////// Регион вспомогательный

    // Функция для работы с RadioButton и Spinner adapter
    private void adapterRadioButton(boolean bool){
        ArrayAdapter<?> adapter = null;
        TypeOperation=bool;
        if(TypeOperation){

            adapter = ArrayAdapter.createFromResource(RootView.getContext(), R.array.IncomesCategory,
                    android.R.layout.simple_spinner_item);
        }else{
            adapter = ArrayAdapter.createFromResource(RootView.getContext(), R.array.ExpensesCategory,
                    android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Вызываем адаптер
        spinner.setAdapter(adapter);
    }

    // Добавляем данные в БД
    private void addItemsToDB(){
        if (checkDate(day, month, year))        // Проверяем дату
        {
            String time = year.getText().toString() + "-" + month.getText().toString() + "-" + day.getText().toString();        // Передаем дату в стринг
            Date date = Date.valueOf(time);     // Передаем стринг в дату
            String category = spinner.getSelectedItem().toString();         // выделяем категорию
            // Добавляем операцию в БД
            addOperationsToDB(nameOfOperation.getText().toString(), TypeOperation, date, Float.parseFloat(sum.getText().toString()), category);
            // Если таблица заполнена хотя бы 1 строкой
            if (counterRows > 1) {
                for (int i = 1; i < counterRows; i++) {
                    // Берем строку
                    TableRow tbRow = (TableRow) tableLayout.getChildAt(i);
                    // Добавляем строку в БД.
                    addItemsToDB(tbRow);
                }
            }
            allFine();
        } else {
            dateInCorrect();
        }
    }

    // Суммируем суммы элементов таблицы
    private void sumData(){
        if (counterRows > 1) {
            finalSum = 0;
            for (int i = 1; i < counterRows; i++) {
                TableRow tbRow = (TableRow) tableLayout.getChildAt(i);
                EditText editText = (EditText) tbRow.getChildAt(2);         // Берем сумму каждого товара
                finalSum += Float.parseFloat(editText.getText().toString());     // Складываем в общую.
            }
            sum.setText(String.valueOf(finalSum));
        } else {
            tableNotExist();
        }
    }

    // Получаем пользователя
    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
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

    // Проверяем дату, проверка данных, которые ввёл пользователь.
    private boolean checkDate(EditText day, EditText month, EditText year){

        try {
            int dayInteger = Integer.parseInt(day.getText().toString());
            int yearInteger;

            // Проверяем год.
            if (year.getText().toString().length() == 4)
                yearInteger = Integer.parseInt(year.getText().toString());
            else return false;

            if (month.getText().toString().equals("02")) {
                // Если високосный
                if (yearInteger % 4 == 0) {
                    if ((yearInteger % 100 != 0) || (yearInteger % 400 == 0)) {
                        if (dayInteger > 0 && dayInteger < 30) return true;
                    }
                    // Если невисокосный
                    else {
                        if (dayInteger > 0 && dayInteger < 29) return true;
                    }
                }
                // Если невисокосный
                else {
                    if (dayInteger > 0 && dayInteger < 29) return true;
                }
                // Берем 1-31 дни месяцев
            } else if (month.getText().toString().equals("01") || month.getText().toString().equals("03") || month.getText().toString().equals("05")
                    || month.getText().toString().equals("07") || month.getText().toString().equals("08") || month.getText().toString().equals("10")
                    || month.getText().toString().equals("12")) {
                if (dayInteger > 0 && dayInteger < 32) return true;
                // Берем 1-30 дни месяцев
            } else if (month.getText().toString().equals("04") || month.getText().toString().equals("06") || month.getText().toString().equals("09")
                    || month.getText().toString().equals("11")) {
                if (dayInteger > 0 && dayInteger < 31) return true;
            }
        }catch (Exception e) {
            dateInCorrect();
        }
        return false;
    }

    // Тосты.
    void dateInCorrect(){
        Toast.makeText(RootView.getContext(), "Неверно введена дата", Toast.LENGTH_SHORT).show();
    }
    void tableInCorrect(){
        Toast.makeText(RootView.getContext(), "Неверно введены данные подробной информации в таблице", Toast.LENGTH_SHORT).show();
    }
    private void tableNotExist(){
        Toast.makeText(RootView.getContext(), "Таблица с подробной информацией пуста", Toast.LENGTH_SHORT).show();
    }
    private void errorInputDatabase(){
        Toast.makeText(RootView.getContext(), "Проверьте содержимое, содержится ошибка!", Toast.LENGTH_SHORT).show();
    }
    private void allFine(){
        Toast.makeText(RootView.getContext(), "Запись успешно добавлена", Toast.LENGTH_SHORT).show();
    }
}