package com.gmail.wjdrhkddud2.rememberu.auth;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.wjdrhkddud2.rememberu.FirestoreKeys;
import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.user.User;
import com.gmail.wjdrhkddud2.rememberu.dialog.NotifyDialog;
import com.gmail.wjdrhkddud2.rememberu.dialog.OnSelectedListener;
import com.gmail.wjdrhkddud2.rememberu.dialog.SelectionDialog;
import com.gmail.wjdrhkddud2.rememberu.main.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    //이메일, 비밀번호 입력받아서 링크 보내고 다이나믹 링크로 앱 복귀해서 인증완료하고
    //비밀번호 변경 기능 넣기

    private EditText emailField;
    private Button emailButton, googleButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private BeginSignInRequest beginSignInRequest;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {

                            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            GoogleSignInAccount account = null;

                            account = accountTask.getResult();

                            if (account != null) {
                                firebaseAuthWithGoogle(account.getIdToken());
                                Log.e(getClass().getSimpleName(), "COMPLETE");
                            } else {
                                Log.e(getClass().getSimpleName(), "account null");
                            }

                        }
                    }
            );

    private ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName(
                    getPackageName(),
                    false,
                    null
                    )
            .setUrl("https://rememberu.page.link/open")
            .setHandleCodeInApp(true)
            .build();

    private FirebaseDynamicLinks dynamicLinks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        emailField = findViewById(R.id.et_email_login);
        emailButton = findViewById(R.id.btn_email_login);
        googleButton = findViewById(R.id.btn_google_login);

        emailField.setText(SharedPreferencesManager.getUserEmail(AuthActivity.this));

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailField.getText().toString();

                SharedPreferencesManager.setUserEmail(AuthActivity.this, email);

                //if (checkEmailFormat(email)) signEmail(email);

                mAuth.sendSignInLinkToEmail(email, actionCodeSettings)
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
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(getClass().getSimpleName(), "SENT FAIL" + e.toString());
                            }
                        });

                /*
                dynamicLinks
                        .getDynamicLink(getIntent())
                        .addOnCompleteListener(new OnCompleteListener<PendingDynamicLinkData>() {
                            @Override
                            public void onComplete(@NonNull Task<PendingDynamicLinkData> task) {

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {


                                Uri deepLink = pendingDynamicLinkData.getLink();
                                Log.e(getClass().getSimpleName(), "DEEP LINK : " + deepLink.toString());

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                 */

            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInOptions gso = new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                googleSignInClient = GoogleSignIn.getClient(AuthActivity.this, gso);
                activityResultLauncher.launch(googleSignInClient.getSignInIntent());

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(getClass().getSimpleName(), "RESUME");

        mAuth = FirebaseAuth.getInstance();
        dynamicLinks = FirebaseDynamicLinks.getInstance();

        dynamicLinks.getDynamicLink(getIntent())
                .addOnCompleteListener(new OnCompleteListener<PendingDynamicLinkData>() {
                    @Override
                    public void onComplete(@NonNull Task<PendingDynamicLinkData> task) {

                        Log.e(getClass().getSimpleName(), "COMPLETE");

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {

                        if (pendingDynamicLinkData == null) return;

                        Uri deepLink = pendingDynamicLinkData.getLink();
                        if (deepLink == null) return;
                        Log.e(getClass().getSimpleName(), "SUCCESS " + deepLink.toString());

                        if (mAuth.isSignInWithEmailLink(deepLink.toString())) {
                            Log.e(getClass().getSimpleName(), "IS SIGN");

                            String email = emailField.getText().toString();

                            mAuth.signInWithEmailLink(email, deepLink.toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.e(getClass().getSimpleName(), "SIGN COMPLETE");
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Log.e(getClass().getSimpleName(), "SIGN SUCCESS");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(getClass().getSimpleName(), "SIGN FAIL");
                                        }
                                    });

                        } else {
                            Log.e(getClass().getSimpleName(), "IS NOT SIGN");

                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(getClass().getSimpleName(), "FAIL");
                    }
                });

        String email = emailField.getText().toString();

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(getClass().getSimpleName(), "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(getClass().getSimpleName(), "signInWithCredential:failure", task.getException());
                            //Snackbar.make(AuthActivity.this, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void updateUI(FirebaseUser user) {

        if (user == null) {
            Log.e(getClass().getSimpleName(), "null");
            return;
        }
        Log.e(getClass().getSimpleName(), "COMPLETE");

        RememberUDatabase db = RememberUDatabase.getInstance(AuthActivity.this);
        boolean isExist = db.userDao().isExist(user.getUid());

        if (!isExist) {
            db.userDao().insert(new User(user.getUid()));
            Log.e(getClass().getSimpleName(), "COMPLETE");
        }

        SharedPreferencesManager.setUserEmail(AuthActivity.this, user.getEmail());
        SharedPreferencesManager.setUserName(AuthActivity.this, user.getDisplayName());
        SharedPreferencesManager.setUID(AuthActivity.this, user.getUid());

        FirebaseFirestore store = FirebaseFirestore.getInstance();
        DocumentReference userDoc = store.collection(getApplicationContext().getPackageName())
                .document(user.getUid());

        Map<String, Object> userData = new HashMap<>();
        userData.put(FirestoreKeys.USER_UID, user.getUid());
        userData.put(FirestoreKeys.USER_PASSWORD, "");
        userData.put(FirestoreKeys.USER_UPLOAD_DATE, Calendar.getInstance().getTime().getTime());

        userDoc.set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Log.e(getClass().getSimpleName(), "COMPLETE");

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(getClass().getSimpleName(), "SUCCESS");

                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(getClass().getSimpleName(), "FAIL");

                    }
                });

    }

    private boolean checkEmailFormat(String email) {

        if (email.length() == 0) {
            Log.e(getClass().getSimpleName(), "COMPLETE");
            return false;
        }

        return true;
    }



}