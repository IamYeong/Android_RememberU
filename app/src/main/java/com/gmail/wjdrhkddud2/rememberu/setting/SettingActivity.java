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

import com.gmail.wjdrhkddud2.rememberu.FirestoreKeys;
import com.gmail.wjdrhkddud2.rememberu.HashConverter;
import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.auth.AuthActivity;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;
import com.gmail.wjdrhkddud2.rememberu.dialog.LoadingDialog;
import com.gmail.wjdrhkddud2.rememberu.dialog.NotifyDialog;
import com.gmail.wjdrhkddud2.rememberu.splash.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView accountNameText, accountEmailText;
    private ConstraintLayout settingLayout, readContactLayout, uploadLayout, downloadLayout;

    private Handler handler = new Handler(Looper.getMainLooper());
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backButton = findViewById(R.id.img_btn_back_setting);
        accountNameText = findViewById(R.id.tv_name_setting);
        accountEmailText = findViewById(R.id.tv_email_setting);
        readContactLayout = findViewById(R.id.layout_read_contacts_setting);
        settingLayout = findViewById(R.id.layout_setting);
        uploadLayout = findViewById(R.id.layout_upload_setting);
        downloadLayout = findViewById(R.id.layout_download_setting);

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

        uploadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload();
            }
        });

        downloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                download();
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

            /*
            loadingDialog = new LoadingDialog(SettingActivity.this);
            loadingDialog.setTitle(getString(R.string.read_contacts));
            loadingDialog.show();

             */

            while (cursor.moveToNext()) {

                /*
                Log.e(getClass().getSimpleName(), "POSITION : " + cursor.getPosition() + "\nCOUNT : " + cursor.getCount());
                float percent = ((float)cursor.getPosition() / (float)cursor.getCount());
                loadingDialog.updateProgress(percent);
                Log.e(getClass().getSimpleName(), "PROGRESS : " + (int)(percent * 100f) + " %");

                 */

                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                Uri phoneURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";

                Cursor phoneCursor = getContentResolver().query(
                        phoneURI, null, selection, new String[]{id}, null
                );

                if (phoneCursor.moveToNext()) {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    Log.e(getClass().getSimpleName(), "\nname : " + name + "\nnumber : " + number);

                    try {

                        String hash = HashConverter.hashingFromString(
                                SharedPreferencesManager.getUID(SettingActivity.this)
                                        + name
                                        + number
                        );

                        if (db.personDao().isExist(hash)) continue;
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

        //loadingDialog.dismiss();

        NotifyDialog dialog = new NotifyDialog(SettingActivity.this);
        dialog.setTitle(getString(R.string.notify_title_complete_read_contacts));
        dialog.setSubtitle(getString(R.string.notify_subtitle_complete_read_contacts));
        dialog.show();

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

    /*
    collection --rememberu

doc - user
doc - user2
...

collection -- people

doc - person
doc - person2
...

collection -- memo

doc - memo
doc - memo2
...
     */

    private void upload() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();

                RememberUDatabase db = RememberUDatabase.getInstance(SettingActivity.this);

                String uid = SharedPreferencesManager.getUID(SettingActivity.this);
                List<Person> people = db.personDao().selectAll(uid);
                List<Memo> memo = db.memoDao().selectAll(uid);
                //사람의 해시로 메모리스트에서 부분리스트를 찾아 서버에 보내면 된다.

                FirebaseFirestore store = FirebaseFirestore.getInstance();

                //어떤 문서에 데이터 집합을 업데이트,
                //어떤 문서를 삭제,
                //어떤 문서의 어떤 필드를 업데이트
                //위와 같은 트랜잭션을 한 번에 수행 후 보낼 수 있음.
                WriteBatch batch = store.batch();


                //1. user 정보 업데이트
                DocumentReference userDoc = store.collection(getApplicationContext().getPackageName())
                        .document(uid);

                Map<String, Object> userData = new HashMap<>();
                userData.put(FirestoreKeys.USER_UID, uid);
                userData.put(FirestoreKeys.USER_PASSWORD, "");

                batch.set(userDoc, userData);

                for (Person person : people) {

                    DocumentReference personDoc = userDoc.collection(FirestoreKeys.PEOPLE).document();
                    Map<String, Object> personData = new HashMap<>();
                    personData.put(FirestoreKeys.PERSON_HASHED, person.getHashed());
                    personData.put(FirestoreKeys.PERSON_UID, person.getUid());
                    personData.put(FirestoreKeys.PERSON_NAME, person.getName());
                    personData.put(FirestoreKeys.PERSON_PHONE, person.getPhoneNumber());
                    personData.put(FirestoreKeys.PERSON_GENDER, person.getGender() + "");
                    personData.put(FirestoreKeys.PERSON_BIRTH, person.getBirth());
                    personData.put(FirestoreKeys.PERSON_DESCRIPTION, person.getDescription());
                    personData.put(FirestoreKeys.PERSON_IS_BOOKMARK, person.isBookmark());

                    batch.set(personDoc, personData);

                    for (Memo m : memo) {

                        if (m.getPersonHashed().equals(person.getHashed())) {

                            DocumentReference memoDoc = personDoc.collection(FirestoreKeys.MEMO).document();
                            Map<String, Object> memoData = new HashMap<>();
                            memoData.put(FirestoreKeys.MEMO_HASHED, m.getHashed());
                            memoData.put(FirestoreKeys.MEMO_UID, m.getUid());
                            memoData.put(FirestoreKeys.MEMO_PERSON_HASHED, m.getPersonHashed());
                            memoData.put(FirestoreKeys.MEMO_TITLE, m.getTitle());
                            memoData.put(FirestoreKeys.MEMO_CONTENT, m.getContent());
                            memoData.put(FirestoreKeys.MEMO_CREATE_TIME, m.getCreate());
                            memoData.put(FirestoreKeys.MEMO_UPDATE_TIME, m.getUpdate());
                            memoData.put(FirestoreKeys.MEMO_IS_BOOKMARK, m.isBookmark());

                            batch.set(memoDoc, memoData);

                        }

                    }

                }


                batch.commit()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Log.e(getClass().getSimpleName(), "COMPLETE : ");

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e(getClass().getSimpleName(), "SUCCESS : ");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(getClass().getSimpleName(), "FAILURE : ");
                            }
                        });

                Looper.loop();
            }
        };

        thread.start();

    }

    private void download() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();

                RememberUDatabase db = RememberUDatabase.getInstance(SettingActivity.this);
                FirebaseFirestore store = FirebaseFirestore.getInstance();
                store.collection(getApplicationContext().getPackageName())
                        .document(SharedPreferencesManager.getUID(SettingActivity.this));

                Looper.loop();
            }
        };

        thread.start();
    }

}