package com.gmail.wjdrhkddud2.rememberu.setting;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.auth.AuthActivity;
import com.gmail.wjdrhkddud2.rememberu.splash.SplashActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView accountNameText, accountEmailText;
    private ConstraintLayout settingLayout, readContactLayout;


    private Handler handler = new Handler(Looper.getMainLooper());

    private ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            Log.e(getClass().getSimpleName(), result + "");

                            if (!result) shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backButton = findViewById(R.id.img_btn_back_setting);
        accountNameText = findViewById(R.id.tv_name_setting);
        accountEmailText = findViewById(R.id.tv_email_setting);
        readContactLayout = findViewById(R.id.layout_read_contacts_setting);
        settingLayout = findViewById(R.id.layout_setting);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //overridePendingTransition(0, 0);
            }
        });

        readContactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                   selectContacts(handler, new Runnable() {
                       @Override
                       public void run() {
                           Snackbar snackbar = Snackbar.make(SettingActivity.this, settingLayout, getString(R.string.all), Snackbar.LENGTH_LONG);
                           snackbar.show();
                       }
                   });
               } else {

                   permissionLauncher.launch(Manifest.permission.READ_CONTACTS);
                   //boolean a = shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS);

               }

               //


            }
        });

    }


    private void selectContacts(Handler handler, Runnable runnable) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();




                handler.post(runnable);

                Looper.loop();
            }
        };

        thread.start();

    }

}