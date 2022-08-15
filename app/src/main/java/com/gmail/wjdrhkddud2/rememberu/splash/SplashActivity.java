package com.gmail.wjdrhkddud2.rememberu.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.auth.AuthActivity;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());

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
                            Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    return;

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                Looper.loop();
            }
        };

        thread.start();

    }
}