package com.gmail.wjdrhkddud2.rememberu.detail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.wjdrhkddud2.rememberu.R;

public class WriteMemoFragment extends Fragment {

    private Context context;
    private ImageButton closeButton;
    private EditText titleField, contentField;
    private TextView dateText;
    private Button saveButton;

    public WriteMemoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_memo, container, false);

        closeButton = view.findViewById(R.id.img_btn_close_write);
        saveButton = view.findViewById(R.id.btn_save_memo);
        dateText = view.findViewById(R.id.tv_date_write_memo);
        titleField = view.findViewById(R.id.et_memo_title_write);
        contentField = view.findViewById(R.id.et_content_write_memo);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(WriteMemoFragment.this);
                fragmentTransaction.commit();


            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}