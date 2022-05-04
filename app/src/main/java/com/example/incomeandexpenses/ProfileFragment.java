package com.example.incomeandexpenses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    TextView txtView;
    Users user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Users user = (Users) getArguments().getSerializable(Users.class.getSimpleName());
        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);

        txtView = (TextView) RootView.findViewById(R.id.txtProfileFragment);
        txtView.setText(user.getName());

        return RootView;
    }

}