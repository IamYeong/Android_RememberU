package com.gmail.wjdrhkddud2.rememberu.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.gmail.wjdrhkddud2.rememberu.R;

public class SettingActivity extends AppCompatActivity {

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backButton = findViewById(R.id.img_btn_back_setting);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //overridePendingTransition(0, 0);
            }
        });
    }
}