package com.example.incomeandexpenses.addelements;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.incomeandexpenses.R;
import com.example.incomeandexpenses.classes.Users;


/**
 * Фрагмент для добавления руками или QR-код!
 */
public class AddFragment extends Fragment {

    // Пользователь
    Users user;

    ///////////////////////////////////// Регион основной

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_add, container, false);
        GetUser();

        Button btnQrCode = RootView.findViewById(R.id.QRCodeScanBtn);
        Button btnInputByHand = RootView.findViewById(R.id.inputByHandBtn);


        btnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToScanQRCode();
            }
        });

        btnInputByHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToInputByHand();
            }
        });
        return RootView;
    }


    ///////////////////////////////////// Конец региона


    ///////////////////////////////////// Регион вспомогательный


    // Переход на фрагмент ВВОД ВРУЧНУЮ
    private void RedirectToInputByHand(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Users.class.getSimpleName(), user);      // Для передачи юзера во фрагмент
        Fragment nextFrag= new InputByHandFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFrag)
                .addToBackStack("")
                .commit();

    }

    // Переход на фрагмент СКАН КР КОДА
    private void RedirectToScanQRCode(){
        Intent intent = new Intent(getActivity(), QRCodeScanner.class);
        intent.putExtra(Users.class.getSimpleName(), user);
        startActivity(intent);
    }

    // Получаем пользователя
    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }


    ///////////////////////////////////// Конец региона
}