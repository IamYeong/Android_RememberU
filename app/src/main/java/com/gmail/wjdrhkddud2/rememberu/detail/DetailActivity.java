package com.gmail.wjdrhkddud2.rememberu.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ImageButton backButton, bookmarkButton, writeButton;
    private TextView personNameText;
    private RecyclerView allRV;
    private MemoVerticalAdapter verticalAdapter;
    private FragmentContainerView fragmentContainerView;
    private FragmentManager fragmentManager;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backButton = findViewById(R.id.img_btn_back_detail);
        bookmarkButton = findViewById(R.id.img_btn_bookmark_detail);
        personNameText = findViewById(R.id.tv_person_name_detail);
        allRV = findViewById(R.id.rv_memo_all_detail);
        writeButton = findViewById(R.id.img_btn_add_detail);
        verticalAdapter = new MemoVerticalAdapter(DetailActivity.this);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(DetailActivity.this);
        verticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        allRV.setLayoutManager(verticalLayoutManager);
        allRV.setAdapter(verticalAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkButton.setSelected(bookmarkButton.isSelected());
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container_detail, new WriteMemoFragment());
                fragmentTransaction.commit();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

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

                selectMemo(person.getHashed());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personNameText.setText(person.getName());
                        bookmarkButton.setSelected(person.isBookmark());
                        verticalAdapter.notifyDataSetChanged();
                    }
                });

                Looper.loop();
            }
        };

        thread.start();

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

    private void selectMemo(String personHash) {

        RememberUDatabase db = RememberUDatabase.getInstance(DetailActivity.this);
        List<Memo> memos = db.memoDao().selectByUserAndPerson(SharedPreferencesManager.getUID(DetailActivity.this), SharedPreferencesManager.getPersonHash(DetailActivity.this));
        verticalAdapter.setMemo(memos);

    }

}