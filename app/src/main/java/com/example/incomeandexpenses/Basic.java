package com.example.incomeandexpenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.io.Serializable;

public class Basic extends AppCompatActivity {

    private final int ID_INSIGHTS = 1;
    private final int ID_ADD = 2;
    private final int ID_PROFILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        //TextView selected_page = findViewById(R.id.selected_page);
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(ID_INSIGHTS,R.drawable.ic_baseline_insights_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ADD,R.drawable.ic_baseline_add_circle_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE,R.drawable.ic_baseline_person_24));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                MakeText(item);
           }
        });

        Bundle arguments = getIntent().getExtras();
        Users user = (Users) arguments.getSerializable(Users.class.getSimpleName());

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                Fragment fragment = null;

                String name;
                switch (item.getId()){

                    case ID_INSIGHTS: name = "INSIGHTS";
                        fragment = new InsightsFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container_view_tag, fragment);
                        ft.commit();
                    break;

                    case ID_ADD: name="ADD";
                        fragment = new AddFragment();
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.fragment_container_view_tag, fragment);
                        ft2.commit();
                    break;

                    case ID_PROFILE: name = "PROFILE";
                        fragment = new ProfileFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Users.class.getSimpleName(), user);
                        fragment.setArguments(bundle);
                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.fragment_container_view_tag, fragment);
                        ft3.commit();
                    break;

                    default: name="";



                        //FragmentManager fn = new getSupportFragmentManager();
                        //FragmentTransaction ft = fn.beginTransaction();
                        //ft.replace(R.id.fragment_container_view_tag, fragment);
                        //ft.commit();

                }
                //selected_page.setText(getString(R.string.main_page_selected, name));
            }
        });

        bottomNavigation.show(ID_INSIGHTS, false);


    }

    void MakeText(MeowBottomNavigation.Model item){
        Toast.makeText(this,"clicked item: " + item.getId(), Toast.LENGTH_SHORT).show();
    }
}