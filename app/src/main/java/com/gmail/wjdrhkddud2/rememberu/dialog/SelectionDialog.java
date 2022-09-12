package com.gmail.wjdrhkddud2.rememberu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gmail.wjdrhkddud2.rememberu.R;

public class SelectionDialog extends Dialog {

    private OnSelectedListener selectedListener;
    private String title = "";
    private String subtitle = "";
    private TextView titleText, subtitleText;
    private Button cancelButton, confirmButton;

    public SelectionDialog(@NonNull Context context) {
        super(context);
    }

    public void setSelectedListener(OnSelectedListener listener) {
        this.selectedListener = listener;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCancelable(false);
        setContentView(R.layout.dialog_selection);

        titleText = findViewById(R.id.tv_title_selection_dialog);
        subtitleText = findViewById(R.id.tv_subtitle_selection_dialog);
        cancelButton = findViewById(R.id.btn_cancel_selection_dialog);
        confirmButton = findViewById(R.id.btn_confirm_selection_dialog);

        titleText.setText(title);
        subtitleText.setText(subtitle);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedListener.onSelect(true);
                dismiss();
            }
        });


    }
}
