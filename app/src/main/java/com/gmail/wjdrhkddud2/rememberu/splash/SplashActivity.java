package com.gmail.wjdrhkddud2.rememberu.splash;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.auth.AuthActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());

    private String[] permissions;

    private List<String> deniedPermissions = new ArrayList<>();

    private ActivityResultLauncher<String[]> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {

                            for (String permission : result.keySet()) {

                                //해당 권한이 미동의 상태라면?
                                if (ContextCompat.checkSelfPermission(SplashActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                                    shouldShowRequestPermissionRationale(permission);
                                }

                            }

                            if (deniedPermissions.isEmpty()) {
                                Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //Toast.makeText(SignActivity.this, deniedPermissions.size() + "개의 권한 미동의", Toast.LENGTH_SHORT).show();
                                for (String permission : deniedPermissions) {
                                    shouldShowRequestPermissionRationale(permission);
                                }
                            }


                        }
                    }
            );



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();

                try {

                    Thread.sleep(500);

                } catch(InterruptedException e) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                    return;

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        permissions = new String[] {
                                Manifest.permission.READ_CONTACTS
                        };
                        activityResultLauncher.launch(permissions);
                    }
                });

                Looper.loop();
            }
        };

        thread.start();

    }
}