package com.example.incomeandexpenses;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Фрагмент настроек
 */
public class SettingsFragment extends Fragment {

    TextView txtView;
    Users user;
    Button btnChangePassword;
    Button btnChangePhoneNumber;
    Button btnChangeName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Получаем пользователя
        GetUser();

        View RootView = inflater.inflate(R.layout.fragment_settings, container, false);
        // Переменный с формы
        btnChangePassword = (Button) RootView.findViewById(R.id.changePassword);
        btnChangePhoneNumber = (Button) RootView.findViewById(R.id.changePhoneNumber);
        btnChangeName = (Button) RootView.findViewById(R.id.changeName);

        txtView = (TextView) RootView.findViewById(R.id.helloProfile);

        //
        // тут не работает смена имени при изменении "Сменить имя". Обновляется только при перезапуске приложения. ПОДУМАТЬ как исправить!
        //
        txtView.setText("Здравствуйте, " + user.getName());

        // Обрабатываем кнопки

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordRedirect();
            }
        });

        btnChangePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePhoneNumberRedirect();
            }
        });

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeNameRedirect();
            }
        });

        return RootView;
    }

    private void Exit(){
        user = null;
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }

    // Новый фрагмент на смену имени
    private void ChangeNameRedirect(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Users.class.getSimpleName(), user);      // Для передачи юзера во фрагмент
        Fragment nextFrag= new ChangeNameFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFrag)
                .addToBackStack("")
                .commit();
    }

    // Новый фрагмент на смену номера телефона
    private void ChangePhoneNumberRedirect(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Users.class.getSimpleName(), user);      // Для передачи юзера во фрагмент
        Fragment nextFrag= new ChangePhoneFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFrag)
                .addToBackStack("")
                .commit();
    }

    // Новый фрагмент на смену пароля
    private void ChangePasswordRedirect(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Users.class.getSimpleName(), user);      // Для передачи юзера во фрагмент
        Fragment nextFrag= new ChangePassFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFrag)
                .addToBackStack("")
                .commit();
    }


    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }

}