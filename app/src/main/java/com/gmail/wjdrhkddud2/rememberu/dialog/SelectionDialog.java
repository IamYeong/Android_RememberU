package com.gmail.wjdrhkddud2.rememberu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.gmail.wjdrhkddud2.rememberu.R;

public class SelectionDialog extends Dialog {

    private OnSelectedListener selectedListener;
    private String title = "";

    public SelectionDialog(@NonNull Context context) {
        super(context);
    }

    public void setSelectedListener(OnSelectedListener listener) {
        this.selectedListener = listener;
    }

    public void setText(String title) {
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCancelable(false);
        setContentView(R.layout.dialog_selection);



    }
}
