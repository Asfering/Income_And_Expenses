package com.example.incomeandexpenses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class OperationsByCategoryFragment extends Fragment {


    //////////////////////////////////////// Регион переменных

    // RootView
    View RootView;

    // Данные, пришедшие с прошлого фрагмента
    List<Operations> operations;
    String categoryName;
    Users user;

    // Работа с формой
    private RecyclerView RecView;   // RecyclerView
    TextView nameCategoryItems;     // Имя категории


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Регион Основной


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_operations_by_category, container, false);

        // Получение данных с фрагмента прошлого
        getUser();              // пользователь
        getOperations();        // операции
        getCategoryName();      // имя категории

        // Работа с формой
        RecView = RootView.findViewById(R.id.operationsByCategory);             // RecyclerView
        nameCategoryItems = RootView.findViewById(R.id.nameCategoryItems);      // Имя категории
        nameCategoryItems.setText(categoryName);                                // Называем категорию

        // Работа с адаптером, обработка нажатия
        OperationsAdapter.OnOperationClickListener operationClickListener = new OperationsAdapter.OnOperationClickListener() {
            @Override
            public void onOperationClick(Operations operation, int position) {
                RedirectToEdit(operation);
            }
        };

        // Подключаем адаптер
        OperationsAdapter operationsAdapter = new OperationsAdapter(RootView.getContext(), operations, operationClickListener);
        RecView.setLayoutManager(new LinearLayoutManager(RootView.getContext()));
        RecView.setAdapter(operationsAdapter);
        return RootView;
    }


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Регион перехода на новый фрагмент


    // Новый фрагмент на изменение
    private void RedirectToEdit(Operations operation){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Users.class.getSimpleName(), user);      // Для передачи юзера во фрагмент
        bundle.putSerializable(Operations.class.getSimpleName(), operation);    // Передача операции во фрагмент

        Fragment nextFrag= new ChangingDataFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFrag)
                .addToBackStack("")
                .commit();
    }


    //////////////////////////////////////// Конец региона



    //////////////////////////////////////// Регион вспомогательный


    // Получаем все операции
    private void getOperations(){
        operations = (List<Operations>) getArguments().getSerializable(Operations.class.getSimpleName());
    }

    // Получаем наименование категории
    private void getCategoryName(){
        categoryName = (String) getArguments().getSerializable("categoryName");
    }

    // Получаем юзера
    private void getUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }


    //////////////////////////////////////// Конец региона
}