package com.gmail.wjdrhkddud2.rememberu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gmail.wjdrhkddud2.rememberu.R;


public class LoadingDialog extends Dialog {

    private String title = "";
    private float percentage = 0f;

    private TextView titleText, percentageText;
    private ImageView loadingImage;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void updateProgress(float percentage) {

        this.percentage = percentage;
        String percent = Integer.toString(((int)this.percentage)) + " %";
        percentageText.setText(percent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(R.layout.dialog_loading);

        titleText = findViewById(R.id.tv_title_loading);
        percentageText = findViewById(R.id.tv_percentage_loading);
        loadingImage = findViewById(R.id.img_loading);

        titleText.setText(title);
        //updateProgress(0f);

    }
}
