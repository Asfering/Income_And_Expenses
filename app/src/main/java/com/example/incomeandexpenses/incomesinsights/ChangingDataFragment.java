package com.example.incomeandexpenses.incomesinsights;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
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

import com.example.incomeandexpenses.Basic;
import com.example.incomeandexpenses.R;
import com.example.incomeandexpenses.classes.Items;
import com.example.incomeandexpenses.classes.Operations;
import com.example.incomeandexpenses.classes.Users;
import com.example.incomeandexpenses.database.MyDataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Класс изменения данных с формы
 */
public class ChangingDataFragment extends Fragment {

    ///////////////////////// Регион объявления

    // Классы
    Users user;
    Operations operation;

    // Таблица
    TableLayout tableLayout;
    TableRow tableRowZero;
    ArrayList<Items> items = new ArrayList<>();

    // Вьюха
    View RootView;

    // Переменные
    int counterRows; // Общее количество строк
    int counterUpdatedRows;     // Строки добавлвенные
    int counterBeforeRows;      // Строки из БД
    boolean TypeOperation;
    float finalSum;

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
    ImageButton btnEdit;
    ImageButton btnSave;
    ImageButton btnDelete;

    ///////////////////////// Конец региона


    ///////////////////////// Регион основной

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_changing_data, container, false);

        getUser();
        getOperation();

        // Объявления
        nameOfOperation = RootView.findViewById(R.id.nameOfOperationEdit);     // Имя операции
        day = RootView.findViewById(R.id.dayEdit);                             // День
        month = RootView.findViewById(R.id.monthEdit);                         // Месяц
        year = RootView.findViewById(R.id.yearEdit);                           // Год
        sum = RootView.findViewById(R.id.finalSumEdit);                        // Итоговая сумма
        rbIncome = RootView.findViewById(R.id.radioIncomeEdit);                 // Кнопка "Доходы"
        rbExpenses = RootView.findViewById(R.id.radioExpensesEdit);             // Кнопка "Расходы"
        spinner = RootView.findViewById(R.id.spinnerEdit);                      // Выпадающий список с категориями
        addRowToTable = RootView.findViewById(R.id.addRowToTableEdit);           // Добавить строку в таблицу
        addRowToTable.setEnabled(false);
        sumTables = RootView.findViewById(R.id.SumTablesEdit);                   // Кнопка для подсчёта суммы в item's ниже. Надо сделать обработчик исключений, если Item нет!
        sumTables.setEnabled(false);
        btnEdit = RootView.findViewById(R.id.editEditButton);                    // Кнопка "ИЗменить"
        btnSave = RootView.findViewById(R.id.saveEditButton);                    // Кнопка "Сохранить"
        btnSave.setEnabled(false);
        btnDelete = RootView.findViewById(R.id.deleteEditButton);                // Кнопка "Удалить"
        TypeOperation = false;

        // Считываем данные по операции
        getData();

        // База данных
        myDataBaseHelper= new MyDataBaseHelper(RootView.getContext());
        database = myDataBaseHelper.getWritableDatabase();

        // Объявляем таблицу
        tableLayout = RootView.findViewById(R.id.table_main_edit);
        tableRowZero = new TableRow(RootView.getContext());
        // Строим столбцы
        createColumns();
        // Получаем данные в таблицу
        getTableData();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editData();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveData();
            }
        });

        addRowToTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewRow();
            }
        });

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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOperation();
            }
        });

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

        return RootView;
    }


    ///////////////////////// Конец региона


    // Функция для работы с RadioButton и Spinner adapter



    ///////////////////////// Регион Удаления

    // Alert!
    private void deleteOperation(){
        AlertDialog.Builder alert = new AlertDialog.Builder(RootView.getContext());
        alert.setTitle("Удаление операции");
        alert.setMessage("Вы действительно хотите удалить операцию?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {       // ОК
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteData();
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {        // ОТМЕНА
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert.show();
    }

    // Удаление данных из БД
    private void deleteData(){
        database.delete("Items", "IdOperations = " + operation.getIdOperation(), null);
        database.delete("Operations", "IdOperation = " + operation.getIdOperation(), null);
        Toast.makeText(RootView.getContext(), "Операция успешно удалена", Toast.LENGTH_SHORT).show();
    }


    ///////////////////////// Конец региона



    ///////////////////////// Регион изменения данных

    // Кнопка СОХРАНИТЬ
    private void saveData(){
        if (checkDate(day, month, year))        // Проверяем дату
        {
            try{

            String time = year.getText().toString() + "-" + month.getText().toString() + "-" + day.getText().toString();        // Передаем дату в стринг
            java.sql.Date date = java.sql.Date.valueOf(time);     // Передаем стринг в дату
            String category = spinner.getSelectedItem().toString();         // выделяем категорию
            // Изменяем операцию в БД
            updateOperationInDB(nameOfOperation.getText().toString(), TypeOperation, date, Float.parseFloat(sum.getText().toString()), category);

            // Если таблица заполнена хотя бы 1 строкой
            if (counterRows > 1) {
                int idRow = 0;
                for (int i = 1; i < counterBeforeRows; i++) {
                    // Берем строку
                    TableRow tbRow = (TableRow) tableLayout.getChildAt(i);
                    updateItemsToDB(tbRow, items.get(i-1));
                    idRow = i;
                    // Изменяем данные
                }
                if(counterUpdatedRows > 0){
                    idRow++;
                    for(int i = idRow; i<counterRows;i++){
                        TableRow tbRow = (TableRow) tableLayout.getChildAt(i);
                        addItemsToDB(tbRow);
                    }
                }
            }
            allFine();
            redirectToBasic();
            }
        catch (Exception e){
            Toast.makeText(RootView.getContext(), "Ошибка при изменении!", Toast.LENGTH_SHORT).show();
        }
        } else {
            dateInCorrect();
        }
    }

    // Инсертим НОВЫЕ данные в таблицу Items
    private void addItemsToDB(TableRow tableRow){
        try {
            database.insert("Items", null, contentDBValues(tableRow));
        }catch (Exception e) {
            tableInCorrect();
        }
    }

    // Апдейтим данные в таблицу Items
    private void updateItemsToDB(TableRow tableRow, Items item){
        try {
            database.update("Items", contentDBValues(tableRow), "IdItem = ?", new String[] {String.valueOf(item.getIdItem())});
        }catch (Exception e) {
            tableInCorrect();
        }
    }

    // Апдейтим данные в таблицу Operations
    private void updateOperationInDB(String name, boolean TypeOperation, java.sql.Date timestamp, float sum, String category){
        // Объявления
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Параметры
        contentValues.put("IdUser", user.getIdUser());
        contentValues.put("Name", name);
        contentValues.put("TypeOperation", TypeOperation);
        contentValues.put("TimeStamp", dateFormat.format(timestamp));
        contentValues.put("Sum", sumFinalData());
        contentValues.put("Category", category);
        // Изменяем запись запись в БД
        database.update("Operations", contentValues, "IdOperation = ?", new String[] {String.valueOf(operation.getIdOperation())});
    }

    // Режим редактирования, кнопка ИЗМЕНИТЬ
    private void editData(){
        Toast.makeText(RootView.getContext(), "Включен режим редактирования",
               Toast.LENGTH_SHORT).show();

        nameOfOperation.setEnabled(true);
        day.setEnabled(true);
        month.setEnabled(true);
        year.setEnabled(true);
        sum.setEnabled(true);
        rbIncome.setEnabled(true);
        rbExpenses.setEnabled(true);
        spinner.setEnabled(true);
        btnSave.setEnabled(true);
        sumTables.setEnabled(true);
        addRowToTable.setEnabled(true);
        if(counterRows > 1){
            for (int i = 1; i < counterRows; i++) {
                TableRow tbRow = (TableRow) tableLayout.getChildAt(i);
                for(int j = 0; j<tbRow.getChildCount(); j++){
                    EditText editText0 = (EditText) tbRow.getChildAt(j);
                    editText0.setEnabled(true);
                }
            }
        }
    }

    ///////////////////////// Конец региона



    ///////////////////////// Регион получения данных из БД

    // Получаем данные по операции
    private void getData(){
        Date date = operation.getTimeStamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        nameOfOperation.setText(operation.getName());
        day.setText(getDay(calendar));
        month.setText(getMonth(calendar));
        year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        sum.setText(String.valueOf(operation.getSum()));
        spinner.setEnabled(false);

        // Тип операции
        if(operation.getTypeOperation()){
            rbIncome.setChecked(true);
            adapterRadioButton(true);

        }else{
            rbExpenses.setChecked(true);
            adapterRadioButton(false);
        }

        // показываем текущую категорию операции, значение взято из БД!
        spinner.setSelection(((ArrayAdapter)spinner.getAdapter()).getPosition(operation.getCategory()));
    }


    // Получаем данные из таблиц Items
    private void getTableData(){
        // Поиск в БД, ищем ID операции
        Cursor cursor;
        String sqlQuery = "Select * from Items where IdOperations == ? ";
        cursor = database.rawQuery(sqlQuery, new String[] {String.valueOf(operation.getIdOperation())});

        if(cursor != null){
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false){
                Items item = new Items(cursor.getInt(cursor.getColumnIndexOrThrow("IdItem")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("IdOperations")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("Nds")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("NdsSum")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("PaymentType")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("Price")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("Quantity")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("Sum")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Category")));

                items.add(item);
                cursor.moveToNext();
            }
        }

        cursor.close();

        for(int i = 0; i<items.size();i++){
            createRow(items.get(i));
        }
    }

    ///////////////////////// Конец региона



    ///////////////////////// Регион работы с таблицей Items. Создание таблицы, заполнение данные и т.д.

    // Делаем новые строки
    public void createNewRow() {
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
        counterUpdatedRows++;
    }

    // Достаем существующие строки из таблицы
    public void createRow(Items item) {
        // Объявляем строку
        TableRow tbrow = new TableRow(RootView.getContext());
        // Первый столбец, наименование
        EditText t1v = new EditText(RootView.getContext());
        t1v.setInputType(InputType.TYPE_CLASS_TEXT);
        t1v.setGravity(Gravity.CENTER);
        t1v.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
        t1v.setEnabled(false);
        t1v.setText(String.valueOf(item.getName()));
        tbrow.addView(t1v);
        // Второй столбец, количество
        EditText t2v = new EditText(RootView.getContext());
        t2v.setGravity(Gravity.CENTER);
        t2v.setEnabled(false);
        t2v.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        t2v.setFilters(new InputFilter[] {new InputFilter.LengthFilter(8)});
        t2v.setText(String.valueOf(item.getQuantity()));
        tbrow.addView(t2v);
        // Третий столбец, сумма
        EditText t3v = new EditText(RootView.getContext());
        t3v.setGravity(Gravity.CENTER);
        t3v.setEnabled(false);
        t3v.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        t3v.setFilters(new InputFilter[] {new InputFilter.LengthFilter(8)});
        t3v.setText(String.valueOf(item.getSum()));
        tbrow.addView(t3v);
        // Добавляем строку в таблицу
        tableLayout.addView(tbrow);
        counterRows++;
        counterBeforeRows++;
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
        counterBeforeRows++;
    }

    ///////////////////////// Конец региона



    ///////////////////////// Регион вспомогательный


    private float sumFinalData(){
        if (counterRows > 1) {
            finalSum = 0;
            for (int i = 1; i < counterRows; i++) {
                TableRow tbRow = (TableRow) tableLayout.getChildAt(i);
                EditText editText = (EditText) tbRow.getChildAt(2);         // Берем сумму каждого товара
                finalSum += Float.parseFloat(editText.getText().toString());     // Складываем в общую.
            }
            return finalSum;
        }
        return Float.parseFloat(sum.getText().toString());
    }


    // Получаем выбранную операцию.
    private void getOperation(){
        operation = (Operations) getArguments().getSerializable(Operations.class.getSimpleName());
    }

    // Получаем юзера
    private void getUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }

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
        Toast.makeText(RootView.getContext(), "Запись успешно изменена", Toast.LENGTH_SHORT).show();
    }

    private ContentValues contentDBValues(TableRow tableRow){
        ContentValues contentValues = new ContentValues();
        try {
            EditText name = (EditText) tableRow.getChildAt(0);
            EditText count = (EditText) tableRow.getChildAt(1);
            EditText sum = (EditText) tableRow.getChildAt(2);

            if (name.getText().toString().equals(""))
                throw new Exception();

            contentValues.put("IdOperations", operation.getIdOperation());
            contentValues.put("Name", name.getText().toString());
            contentValues.put("Quantity", Float.parseFloat(count.getText().toString()));
            contentValues.put("Sum", Float.parseFloat(sum.getText().toString()));

        }catch (Exception e){
            tableInCorrect();
        }
        return contentValues;
    }

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

    // Функия для вызова спиннера в зависимости от радиобаттон
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

    // Получаем месяц
    private String getMonth(Calendar calendar){
        String tempReturn = "";
        int month = Integer.parseInt (String.valueOf(calendar.get(Calendar.MONTH)));
        if(month < 9){
            month++;
            tempReturn="0" + String.valueOf(month);
        } else if (month < 12) {
            month++;
            tempReturn=String.valueOf(month);
        }
        return tempReturn;
    }

    // Получаем день
    private String getDay(Calendar calendar){
        String tempReturn = "";
        int month = Integer.parseInt (String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        if(month < 10) {
            tempReturn="0" + month;
        }
        else tempReturn = String.valueOf(month);
        return tempReturn;
    }


    //TODO: сделать проверку даты, чтобы дата чека не была позже текущей. ТО ЖЕ самое при добавлении вручную.

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

    // Переход на главную
    private void redirectToBasic(){
        Intent intent = new Intent(RootView.getContext(), Basic.class);
        intent.putExtra(Users.class.getSimpleName(), user);
        startActivity(intent);
    }
}