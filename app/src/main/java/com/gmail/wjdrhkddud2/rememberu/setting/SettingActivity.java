package com.gmail.wjdrhkddud2.rememberu.setting;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.wjdrhkddud2.rememberu.HashConverter;
import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.auth.AuthActivity;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;
import com.gmail.wjdrhkddud2.rememberu.splash.SplashActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.NoSuchAlgorithmException;
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
                   selectContacts();

               } else {
                   ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 200);

               }

               //


            }
        });

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

        accountNameText.setText(SharedPreferencesManager.getUserName(SettingActivity.this));
        accountEmailText.setText(SharedPreferencesManager.getUserEmail(SettingActivity.this));

    }

    private void selectContacts() {

        Log.e(getClass().getSimpleName(), "SELECT CONTACTS");

        RememberUDatabase db = RememberUDatabase.getInstance(SettingActivity.this);

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = getContentResolver().query(
                uri, null, null, null, sort
        );

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                Uri phoneURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";

                Cursor phoneCursor = getContentResolver().query(
                        phoneURI, null, selection, new String[]{id}, null
                );

                if (phoneCursor.moveToNext()) {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    Log.e(getClass().getSimpleName(), "\n name : " + name + "\n number : " + number);


                    try {

                        String hash = HashConverter.hashingFromString(
                                SharedPreferencesManager.getUID(SettingActivity.this)
                                + name
                                + number
                        );

                        if (db.personDao().isExist(hash)) return;
                        Person person = new Person(hash);
                        person.setUid(SharedPreferencesManager.getUID(SettingActivity.this));
                        person.setName(name);
                        person.setPhoneNumber(number);
                        person.setBookmark(false);
                        person.setGender('u');
                        person.setBirth(0);
                        person.setDescription("");

                        db.personDao().insert(person);


                    } catch (NoSuchAlgorithmException e) {

                    }

                }

                phoneCursor.close();

            }

        }


        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //제대로 왔는지 확인
        if (requestCode == 200) {

            for (int i = 0; i < permissions.length; i++) {

                String permission = permissions[i];

                if (permission.equals(Manifest.permission.READ_CONTACTS)) {

                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        selectContacts();
                    } else {
                        Log.e(getClass().getSimpleName(), "DENIED PERMISSION");
                    }

                }
                //...다른 권한

            }

        }

    }
}