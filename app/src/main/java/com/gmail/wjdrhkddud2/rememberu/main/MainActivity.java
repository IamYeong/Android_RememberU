package com.gmail.wjdrhkddud2.rememberu.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.add.NewPersonActivity;
import com.gmail.wjdrhkddud2.rememberu.auth.AuthActivity;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;
import com.gmail.wjdrhkddud2.rememberu.dialog.OnSelectedListener;
import com.gmail.wjdrhkddud2.rememberu.dialog.SelectionDialog;
import com.gmail.wjdrhkddud2.rememberu.setting.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView nameText, countText, sortText;
    private ImageButton settingButton, addButton;
    private RecyclerView bookmarkRV, searchRV;
    private BookmarkPersonAdapter bookmarksAdapter;
    private ResultPersonAdapter resultsAdapter;
    private Button memoExistFilteringButton, maleFilteringButton, femaleFilteringButton, unknownFilteringButton;

    private EditText searchField;
    private int sortCursor = 0;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = findViewById(R.id.tv_name_main);
        countText = findViewById(R.id.tv_count_main);

        sortText = findViewById(R.id.tv_sort_main);
        settingButton = findViewById(R.id.img_btn_go_to_setting);
        addButton = findViewById(R.id.img_btn_add_main);
        searchField = findViewById(R.id.et_search_main);

        memoExistFilteringButton = findViewById(R.id.btn_filter_exist_memo_main);
        maleFilteringButton = findViewById(R.id.btn_filter_male_main);
        femaleFilteringButton = findViewById(R.id.btn_filter_female_main);
        unknownFilteringButton = findViewById(R.id.btn_filter_unknown_main);

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

        sortText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sortCursor++;
                if (sortCursor > 1) sortCursor = 0;

                switch (sortCursor) {

                    case 0 :
                        resultsAdapter.sortASC();
                        sortText.setText(getString(R.string.sort_name));
                        break;

                    case 1 :
                        resultsAdapter.sortDESC();
                        sortText.setText(getString(R.string.sort_name_desc));
                        break;

                }

                resultsAdapter.notifyDataSetChanged();

            }
        });

        memoExistFilteringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()) {
                    resultsAdapter.removeFilter();
                    resultsAdapter.notifyDataSetChanged();
                    v.setSelected(false);
                    return;
                }

                resultsAdapter.filteringByExistMemo();
                resultsAdapter.notifyDataSetChanged();

                maleFilteringButton.setSelected(false);
                femaleFilteringButton.setSelected(false);
                unknownFilteringButton.setSelected(false);
                memoExistFilteringButton.setSelected(true);
            }
        });

        maleFilteringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()) {
                    resultsAdapter.removeFilter();
                    resultsAdapter.notifyDataSetChanged();
                    v.setSelected(false);
                    return;
                }

                resultsAdapter.filteringByGender('m');
                resultsAdapter.notifyDataSetChanged();

                maleFilteringButton.setSelected(true);
                femaleFilteringButton.setSelected(false);
                unknownFilteringButton.setSelected(false);
                memoExistFilteringButton.setSelected(false);
            }
        });

        femaleFilteringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()) {
                    resultsAdapter.removeFilter();
                    resultsAdapter.notifyDataSetChanged();
                    v.setSelected(false);
                    return;
                }

                resultsAdapter.filteringByGender('f');
                resultsAdapter.notifyDataSetChanged();

                maleFilteringButton.setSelected(false);
                femaleFilteringButton.setSelected(true);
                unknownFilteringButton.setSelected(false);
                memoExistFilteringButton.setSelected(false);

            }
        });

        unknownFilteringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()) {
                    resultsAdapter.removeFilter();
                    resultsAdapter.notifyDataSetChanged();
                    v.setSelected(false);
                    return;
                }

                resultsAdapter.filteringByGender('u');
                resultsAdapter.notifyDataSetChanged();

                maleFilteringButton.setSelected(false);
                femaleFilteringButton.setSelected(false);
                unknownFilteringButton.setSelected(true);
                memoExistFilteringButton.setSelected(false);
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

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                resultsAdapter.searchByWord(s.toString());
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        SelectionDialog selectionDialog = new SelectionDialog(MainActivity.this);
        selectionDialog.setTitle(getString(R.string.title_app_terminate));
        selectionDialog.setSubtitle(getString(R.string.subtitle_app_terminate));
        selectionDialog.setSelectedListener(new OnSelectedListener() {
            @Override
            public void onSelect(boolean selection) {

                if (selection) {
                    finish();
                }
            }
        });

        selectionDialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();



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

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();

                RememberUDatabase db = RememberUDatabase.getInstance(MainActivity.this);
                String uid = SharedPreferencesManager.getUID(MainActivity.this);
                List<Person> people = db.personDao().selectAll(uid);
                List<Memo> memo = db.memoDao().selectAll(uid);

                String accountInfo = people.size() + " " + getString(R.string.friends) + "   " + memo.size() + " " + getString(R.string.memories);

                bookmarksAdapter.setPeople(people);
                resultsAdapter.setPeople(people);

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        nameText.setText(SharedPreferencesManager.getUserName(MainActivity.this));
                        countText.setText(accountInfo);

                        bookmarksAdapter.notifyDataSetChanged();
                        resultsAdapter.notifyDataSetChanged();
                    }
                });

                Looper.loop();
            }
        };
        thread.start();
    }


    private List<Person> selectContacts() {

        //Cursor
        return null;
    }
}