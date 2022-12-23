package com.example.incomeandexpenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.incomeandexpenses.addelements.AddFragment;
import com.example.incomeandexpenses.classes.Users;
import com.example.incomeandexpenses.incomesinsights.InsightsFragment;
import com.example.incomeandexpenses.settings.SettingsFragment;


/**
 * Базовая активити, на её основе строится всё приложение
 */
public class Basic extends AppCompatActivity {

    // Переменные
    private final int ID_INSIGHTS = 1;
    private final int ID_ADD = 2;
    private final int ID_SETTINGS = 3;

    // Класс
    private Users user;


    /////////////////////////////////////// Регион основной


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        try {
            // Создаем элемент класса
            MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

            // Добавляем элементы на бар
            bottomNavigation.add(new MeowBottomNavigation.Model(ID_INSIGHTS, R.drawable.ic_baseline_insights_24));
            bottomNavigation.add(new MeowBottomNavigation.Model(ID_ADD, R.drawable.ic_baseline_add_circle_24));
            bottomNavigation.add(new MeowBottomNavigation.Model(ID_SETTINGS, R.drawable.ic_baseline_settings_24));


            bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
                @Override
                public void onClickItem(MeowBottomNavigation.Model item) {
                    //SelectItem(item);
                }
            });

            // Получаем пользователя
           GetUser();

           // Нажатие ещё раз
           bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
               @Override
               public void onReselectItem(MeowBottomNavigation.Model item) {
                   SelectItem(item);
               }
           });


           // Нажатие
            bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
                @Override
                public void onShowItem(MeowBottomNavigation.Model item) {
                    SelectItem(item);
                }
            });



            bottomNavigation.show(ID_INSIGHTS, false);
        }catch (Exception e){
            Log.d("TAG", "error " + e);
        }


    }


    //////////////////////////////////////////// Конец региона


    //////////////////////////////////////////// Регион с выбором

    // Переходим на фрагмент
    void SelectFragment(Fragment newFragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_view_tag, newFragment);
        ft.commit();
    }

    // Выбираем фрагмент
    void SelectItem(MeowBottomNavigation.Model item){
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Users.class.getSimpleName(), user);

        switch (item.getId()) {
            case ID_INSIGHTS:
                fragment = new InsightsFragment();
                fragment.setArguments(bundle);
                SelectFragment(fragment);
                break;

            case ID_ADD:
                fragment = new AddFragment();
                fragment.setArguments(bundle);
                SelectFragment(fragment);
                break;

            case ID_SETTINGS:
                fragment = new SettingsFragment();
                fragment.setArguments(bundle);
                SelectFragment(fragment);
                break;

        }
    }


    //////////////////////////////////////////// Конец региона


    //////////////////////////////////////////// Регион вспомогательный


    void GetUser(){
        Bundle arguments = getIntent().getExtras();
        user = (Users) arguments.getSerializable(Users.class.getSimpleName());
    }


    //////////////////////////////////////////// Регион основной
}