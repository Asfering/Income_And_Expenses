package com.example.incomeandexpenses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.Timestamp;
import org.threeten.bp.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class InsightsFragment extends Fragment {

    private RecyclerView RecView;
    private ArrayList<Operations> operations = new ArrayList<>();
    private Timestamp timestamp;
    Users user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_insights, container, false);

        GetUser();
        RecView = RootView.findViewById(R.id.recView);
        initializeData();
        OperationsAdapter operationsAdapter = new OperationsAdapter(RootView.getContext(), operations);
        RecView.setLayoutManager(new LinearLayoutManager(RootView.getContext()));
        RecView.setAdapter(operationsAdapter);
        // Inflate the layout for this fragment
        return RootView;
    }

    private void initializeData(){
        Calendar calendar = new GregorianCalendar(2017, 0 , 25);
        operations.add(new Operations(1,3,"Работа в кафе", true, calendar, 300, "Зарплата"));
        operations.add(new Operations(2,3,"Пятерочка", false, calendar, 100, "Магазины"));
    }

    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }
}