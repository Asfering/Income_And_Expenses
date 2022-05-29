package com.example.incomeandexpenses;

import static java.util.Collections.swap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class CategoriesFragment extends Fragment {


    //////////////////////////////////////// Регион переменных


    // RootView
    View RootView;

    // Данные, пришедшие с фрагмента
    Users user;
    boolean typeOperation;
    int allCost;
    List<Operations> operations;
    String month;
    String year;

    // Работа с элементами формы
    private RecyclerView RecView;
    TextView incOrIncPerMonth;
    TextView allCostInMonth;

    // Список
    List<Categories> categories = new ArrayList<>();


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Регион основной


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Получаем RootView
        RootView = inflater.inflate(R.layout.fragment_categories, container, false);

        // Получение данных с прошлого фрагмента
        getType();          // получаем тип операции
        getUser();          // Получаем юзера
        getOperations();    // Получаем список операций
        getAllCost();       // Получаем всю сумму по доходу/расходу
        getDate();          // Получаем даты

        // Работа с формой
        RecView = RootView.findViewById(R.id.categoryView);             // RecyclerView
        incOrIncPerMonth = RootView.findViewById(R.id.incOrIncPerMonth);// TextView сверху
        allCostInMonth = RootView.findViewById(R.id.AllCostInMonth);    // TextView с суммой общей

        // Работа с заполнением формы
        fillCategories();
        setTitle();
        sortArray();
        getPercentages();

        // Адаптер RecyclerView, обработка нажатия
        CategoriesAdapter.OnCategoryClickListener onCategoryClickListener = new CategoriesAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Categories category, int position) {
                redirectToOperationsByCategory(category);
            }
        };

        // Подключаем адаптер
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(RootView.getContext(), categories, onCategoryClickListener);
        RecView.setLayoutManager(new LinearLayoutManager(RootView.getContext()));
        RecView.setAdapter(categoriesAdapter);
        return RootView;
    }


    //////////////////////////////////////// Конец региона


    //////////////////////////////////////// Регион перехода на новый фрагмент


    private void redirectToOperationsByCategory(Categories category){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Users.class.getSimpleName(), user);      // Для передачи юзера во фрагмент
        bundle.putSerializable(Operations.class.getSimpleName(), operationList(category));
        bundle.putString("categoryName", category.getName());

        Fragment nextFrag= new OperationsByCategoryFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFrag)
                .addToBackStack("")
                .commit();
    }


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Регион работы со списком категорий


    // Заполняем проценты у каждой категории от общего количества
    private void getPercentages(){
        for(int i = 0; i<categories.size(); i++){
            categories.get(i).setPercentage((categories.get(i).getSum() / allCost) * 100);
        }
    }

    // Сортировка списка, по убыванию, пузырьком.
    private void sortArray(){
        boolean needIteration = true;
        while (needIteration) {
            needIteration = false;
            for (int i = 1; i < categories.size(); i++) {
                if (categories.get(i).getSum() > categories.get(i-1).getSum()) {
                    swap(categories, i, i-1);
                    needIteration = true;
                }
            }
        }
    }

    // Заполняем список категорий
    private void fillCategories(){
        // Если список не пуст, чистим
        if(!categories.isEmpty())
            categories.clear();

        // идём по операцям
        for(int i = 0; i<operations.size(); i++){
            if(operations.get(i).getTypeOperation() == typeOperation){      // если тип операции равен нажатой кнопке (доход/расход)
                // создаем объект класса категория
                Categories category = new Categories();
                category.setName(operations.get(i).getCategory());
                category.setSum(operations.get(i).getSum());

                int temp = 0;           // Вспомогательная переменная

                // Первая категория в списке
                if(categories.size() == 0){
                    categories.add(category);
                } // Иначе идём по категорям
                else {
                    // Идём по категориям
                    for(int j = 0; j<categories.size(); j++){
                        if(categories.get(j).getName().equals(category.getName())){     // Если категория есть в списке
                            categories.get(j).setSum(categories.get(j).getSum() + category.getSum());       // Изменяем сумму
                            temp++;         // пустое знаниче +1
                            break;          // выходим из цикла
                        }
                    }
                    if(temp == 0){                      // если категории не было в списке
                        categories.add(category);       // создаем новую категорию.
                    }
                }
            }
        }
    }


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Регион вспомогательный


    // Заполняем верхушку фрагмента с суммами и доход/расход
    private void setTitle(){
        if(typeOperation)
            incOrIncPerMonth.setText("Поступления за " + month + " " +  year);
        else
            incOrIncPerMonth.setText("Расходы за " + month + " " +  year);

        allCostInMonth.setText(String.valueOf(allCost) + " ₽");
    }

    // Получаем список операций по категориям
    private ArrayList<Operations> operationList(Categories category){
        ArrayList<Operations> operationsByCategory = new ArrayList<>();

        for(int i = 0; i<operations.size(); i++){
            if(operations.get(i).getCategory().equals(category.getName())){
                operationsByCategory.add(operations.get(i));
            }
        }
        return operationsByCategory;
    }

    // Получаем тип операции
    private void getType(){
        typeOperation = (boolean) getArguments().getBoolean("typeOperation");
    }

    // Получаем общую сумму
    private void getAllCost(){
        allCost = (int) getArguments().getInt("allCost");
    }

    // Получаем дату, по которой сортировали
    private void getDate(){
        month = (String) getArguments().getString("month");
        year = (String) getArguments().getString("year");
    }

    // Получаем все операции
    private void getOperations(){
        operations = (List<Operations>) getArguments().getSerializable(Operations.class.getSimpleName());
    }

    // Получаем юзера
    private void getUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }

    //////////////////////////////////////// Конец региона
}