package com.gmail.wjdrhkddud2.rememberu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.gmail.wjdrhkddud2.rememberu.R;

public class SelectionDialog extends Dialog {



    public SelectionDialog(@NonNull Context context) {
        super(context);
    }

    public void setText(String title) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCancelable(false);
        //setContentView(R.layout.dialog_nofification);




    }
}
