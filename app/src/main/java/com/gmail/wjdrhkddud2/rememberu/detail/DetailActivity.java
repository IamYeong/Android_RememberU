package com.gmail.wjdrhkddud2.rememberu.detail;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.add.ModifyPersonActivity;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ImageButton backButton, bookmarkButton, writeButton;
    private TextView personNameText, sortText, personContentText;
    private RecyclerView allRV,markRV;
    private MemoVerticalAdapter verticalAdapter;
    private MemoHorizontalAdapter horizontalAdapter;
    private FragmentContainerView fragmentContainerView;
    private FragmentManager fragmentManager;
    private EditText searchField;
    private ConstraintLayout infoLayout;

    private int sortCursor = 0;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backButton = findViewById(R.id.img_btn_back_detail);
        bookmarkButton = findViewById(R.id.img_btn_bookmark_detail);
        personNameText = findViewById(R.id.tv_person_name_detail);
        allRV = findViewById(R.id.rv_memo_all_detail);
        markRV = findViewById(R.id.rv_memo_bookmark_detail);
        writeButton = findViewById(R.id.img_btn_add_detail);
        searchField = findViewById(R.id.et_search_detail);
        sortText = findViewById(R.id.tv_sort_detail);
        personContentText = findViewById(R.id.tv_person_content);
        infoLayout = findViewById(R.id.constraint_person_info);

        verticalAdapter = new MemoVerticalAdapter(DetailActivity.this);
        horizontalAdapter = new MemoHorizontalAdapter(DetailActivity.this);

        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailActivity.this, ModifyPersonActivity.class);
                startActivity(intent);

            }
        });

        verticalAdapter.setSelectListener(new OnMemoSelectedListener() {
            @Override
            public void onSelect(String hash) {
                ModifyMemoFragment modifyMemoFragment = new ModifyMemoFragment();
                modifyMemoFragment.setDetachListener(new OnFragmentDetachListener() {
                    @Override
                    public void onDetach() {
                        selectMemo();
                    }
                });
                openFragment(modifyMemoFragment);
            }
        });

        horizontalAdapter.setSelectListener(new OnMemoSelectedListener() {
            @Override
            public void onSelect(String hash) {
                ModifyMemoFragment modifyMemoFragment = new ModifyMemoFragment();
                modifyMemoFragment.setDetachListener(new OnFragmentDetachListener() {
                    @Override
                    public void onDetach() {
                        selectMemo();
                    }
                });
                openFragment(modifyMemoFragment);
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
                verticalAdapter.searchByWord(s.toString());
            }
        });

        sortText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCursor++;
                if (sortCursor > 3) sortCursor = 0;

                switch (sortCursor) {

                    case 0 :
                        verticalAdapter.sortByDateDESC();
                        sortText.setText(getString(R.string.sort_date_desc));
                        break;

                    case 1 :
                        verticalAdapter.sortByDateASC();
                        sortText.setText(getString(R.string.sort_date_asc));
                        break;

                    case 2 :
                        verticalAdapter.sortASC();
                        sortText.setText(getString(R.string.sort_title_asc));
                        break;

                    case 3 :
                        verticalAdapter.sortDESC();
                        sortText.setText(getString(R.string.sort_title_desc));
                        break;

                }

                verticalAdapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(DetailActivity.this);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(DetailActivity.this);
        verticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        allRV.setLayoutManager(verticalLayoutManager);
        allRV.setAdapter(verticalAdapter);
        markRV.setLayoutManager(horizontalLayoutManager);
        markRV.setAdapter(horizontalAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookmarkButton.setClickable(false);
                updateBookmark(!bookmarkButton.isSelected());

            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WriteMemoFragment writeMemoFragment = new WriteMemoFragment();
                writeMemoFragment.setDetachListener(new OnFragmentDetachListener() {
                    @Override
                    public void onDetach() {
                        selectMemo();
                    }
                });
                openFragment(writeMemoFragment);
            }
        });

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

        selectMemo();

    }

    private void updateBookmark(boolean bookmark) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();

                RememberUDatabase db = RememberUDatabase.getInstance(DetailActivity.this);
                Person person = db.personDao().select(
                        SharedPreferencesManager.getUID(DetailActivity.this),
                        SharedPreferencesManager.getPersonHash(DetailActivity.this)
                );
                person.setBookmark(bookmark);
                db.personDao().update(person);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        bookmarkButton.setSelected(bookmark);
                        bookmarkButton.setClickable(true);
                    }
                });

                Looper.loop();
            }
        };
        thread.start();

    }

    private void selectMemo() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();

                RememberUDatabase db = RememberUDatabase.getInstance(DetailActivity.this);
                String uid = SharedPreferencesManager.getUID(DetailActivity.this);
                String personHash = SharedPreferencesManager.getPersonHash(DetailActivity.this);
                Person person = db.personDao().select(
                        uid,
                        personHash
                );

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        String content =
                                getString(R.string.person_gender) + " : " + (person.getGender() == 'm' ? getString(R.string.male) : getString(R.string.female))
                                + "\n"
                                + getString(R.string.person_birth) + " : " + (person.getBirth() != 0 ? simpleDateFormat.format(person.getBirth()) : "NON")
                                + "\n"
                                + getString(R.string.person_description) + " : " + (person.getDescription() != null ? person.getDescription() : "NON")
                                        + "\n"
                                        + getString(R.string.person_phone) + " : " + (person.getPhoneNumber() != null ? person.getPhoneNumber() : "NON");

                        personContentText.setText(content);

                    }
                });

                List<Memo> memos = db.memoDao().selectByUserAndPerson(uid, personHash);
                verticalAdapter.setMemo(memos);
                verticalAdapter.sortByDateDESC();
                horizontalAdapter.setMemo(memos);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personNameText.setText(person.getName());
                        bookmarkButton.setSelected(person.isBookmark());
                        verticalAdapter.notifyDataSetChanged();
                        horizontalAdapter.notifyDataSetChanged();
                    }
                });

                Looper.loop();
            }
        };

        thread.start();

    }

    private void openFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_detail, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}