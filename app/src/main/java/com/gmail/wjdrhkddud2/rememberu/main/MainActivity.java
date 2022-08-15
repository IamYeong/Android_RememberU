package com.gmail.wjdrhkddud2.rememberu.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.setting.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextView nameText;
    private ImageButton settingButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = findViewById(R.id.tv_name_main);
        settingButton = findViewById(R.id.img_btn_go_to_setting);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                //overridePendingTransition(0, 0);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String accountInfo = mAuth.getCurrentUser().getDisplayName() + "(" + mAuth.getCurrentUser().getEmail() + ")";
            nameText.setText(accountInfo);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}