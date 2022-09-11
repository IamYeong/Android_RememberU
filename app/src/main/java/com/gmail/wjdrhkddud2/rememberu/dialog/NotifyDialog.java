package com.gmail.wjdrhkddud2.rememberu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gmail.wjdrhkddud2.rememberu.R;

public class NotifyDialog extends Dialog {

    private TextView titleText, confirmText;
    private String title = "";

    public NotifyDialog(@NonNull Context context) {
        super(context);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCancelable(false);
        setContentView(R.layout.dialog_nofification);

        titleText = findViewById(R.id.tv_title_notification);
        confirmText = findViewById(R.id.tv_confirm_notification);

        titleText.setText(title);

        confirmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
