package com.gmail.wjdrhkddud2.rememberu.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.dialog.NotifyDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ImageButton backButton;
    private EditText emailField;
    private Button sendVerificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        backButton = findViewById(R.id.img_btn_back_change_password);
        emailField = findViewById(R.id.et_email_change);
        sendVerificationButton = findViewById(R.id.btn_send_verification_for_change_password);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //이 이메일이 기존에 있는 이메일인지 확인 불가

                String email = emailField.getText().toString();
                if (checkEmailFormat(email)) {

                    sendPasswordResetEmail(email);

                }

            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean checkEmailFormat(String email) {

        if (email.length() == 0) {
            return false;
        }

        return true;

    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Log.e(getClass().getSimpleName(), "SENT COMPLETE");


                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.e(getClass().getSimpleName(), "SENT SUCCESS");


                        NotifyDialog dialog = new NotifyDialog(ChangePasswordActivity.this);
                        dialog.setTitle(getString(R.string.notify));
                        dialog.setSubtitle(getString(R.string.sent_verification_mail));
                        dialog.show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(getClass().getSimpleName(), "SENT FAIL");

                    }
                });
    }



}