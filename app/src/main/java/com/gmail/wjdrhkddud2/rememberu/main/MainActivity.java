package com.gmail.wjdrhkddud2.rememberu.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.add.NewPersonActivity;
import com.gmail.wjdrhkddud2.rememberu.auth.AuthActivity;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;
import com.gmail.wjdrhkddud2.rememberu.setting.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView nameText, countText, filterText, sortText;
    private ImageButton settingButton, addButton;
    private FirebaseAuth mAuth;
    private RecyclerView bookmarkRV, searchRV;
    private BookmarkPersonAdapter bookmarksAdapter;
    private ResultPersonAdapter resultsAdapter;

    private EditText searchField;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = findViewById(R.id.tv_name_main);
        countText = findViewById(R.id.tv_count_main);
        filterText = findViewById(R.id.tv_filter_main);
        sortText = findViewById(R.id.tv_sort_main);
        settingButton = findViewById(R.id.img_btn_go_to_setting);
        addButton = findViewById(R.id.img_btn_add_main);
        searchField = findViewById(R.id.et_search_main);

        bookmarkRV = findViewById(R.id.rv_bookmark_main);
        searchRV = findViewById(R.id.rv_search_main);

        bookmarksAdapter = new BookmarkPersonAdapter(MainActivity.this);
        resultsAdapter = new ResultPersonAdapter(MainActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        bookmarkRV.setLayoutManager(linearLayoutManager);
        bookmarkRV.setAdapter(bookmarksAdapter);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MainActivity.this);
        verticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchRV.setLayoutManager(verticalLayoutManager);
        searchRV.setAdapter(resultsAdapter);

        filterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sortText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                //overridePendingTransition(0, 0);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NewPersonActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            RememberUDatabase db = RememberUDatabase.getInstance(MainActivity.this);
            String uid = mAuth.getCurrentUser().getUid();
            List<Person> people = db.personDao().selectAll(uid);
            List<Memo> memo = db.memoDao().selectAll(uid);


            String accountInfo = people.size() + " " + getString(R.string.friends) + "   " + memo.size() + " " + getString(R.string.memories);
            nameText.setText( mAuth.getCurrentUser().getDisplayName());
            countText.setText(accountInfo);

            List<Person> people2 = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Person person = new Person("0123456" + i);
                person.setName("김철수");
                person.setBookmark(true);
                person.setUid(mAuth.getCurrentUser().getUid());
                person.setPhoneNumber("010-0000-0000");

                people2.add(person);
            }
            bookmarksAdapter.setPeople(people2);
            resultsAdapter.setPeople(people2);


        } else {

            /*
            Toast.makeText(this, getString(R.string.not_auth), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
            finish();

             */

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


    private List<Person> selectContacts() {

        //Cursor
        return null;
    }
}