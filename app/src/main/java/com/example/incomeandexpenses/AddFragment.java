package com.example.incomeandexpenses;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;


public class AddFragment extends Fragment {

    Users user;

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

    private void RedirectToScanQRCode(){
        Intent intent = new Intent(getActivity(), QRCodeScanner.class);
        startActivity(intent);
    }

    private void GetUser(){
        user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
    }
}