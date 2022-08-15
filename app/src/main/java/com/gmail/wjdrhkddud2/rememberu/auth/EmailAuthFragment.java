package com.gmail.wjdrhkddud2.rememberu.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.wjdrhkddud2.rememberu.R;

public class EmailAuthFragment extends Fragment {


    public EmailAuthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_auth, container, false);
    }
}