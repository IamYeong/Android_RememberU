package com.gmail.wjdrhkddud2.rememberu.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.wjdrhkddud2.rememberu.HashConverter;
import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;
import com.gmail.wjdrhkddud2.rememberu.dialog.OnSelectedListener;
import com.gmail.wjdrhkddud2.rememberu.dialog.SelectionDialog;
import com.gmail.wjdrhkddud2.rememberu.main.MainActivity;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WriteMemoActivity extends AppCompatActivity {

    private ImageButton closeButton, bookmarkButton;
    private EditText titleField, contentField;
    private TextView dateText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_write_memo);

        closeButton = findViewById(R.id.img_btn_close_write);
        saveButton = findViewById(R.id.btn_save_memo);
        dateText = findViewById(R.id.tv_date_write_memo);
        titleField = findViewById(R.id.et_memo_title_write);
        contentField = findViewById(R.id.et_content_write_memo);
        bookmarkButton = findViewById(R.id.img_btn_bookmark_memo_write);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectionDialog selectionDialog = new SelectionDialog(WriteMemoActivity.this);
                selectionDialog.setTitle(getString(R.string.title_exit_memo));
                selectionDialog.setSubtitle(getString(R.string.subtitle_exit_memo));
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
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Memo memo = isExactly();
                if (memo == null) return;

                RememberUDatabase db = RememberUDatabase.getInstance(WriteMemoActivity.this);
                db.memoDao().insert(memo);

                finish();

            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkButton.setSelected(!bookmarkButton.isSelected());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        dateText.setText(simpleDateFormat.format(Calendar.getInstance().getTime().getTime()));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        SelectionDialog selectionDialog = new SelectionDialog(WriteMemoActivity.this);
        selectionDialog.setTitle(getString(R.string.title_exit_memo));
        selectionDialog.setSubtitle(getString(R.string.subtitle_exit_memo));
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

    private Memo isExactly() {

        String title = titleField.getText().toString();
        String content = contentField.getText().toString();

        if (title.length() == 0) {

            return null;
        }

        if (content.length() == 0) {

            return null;
        }

        long time = Calendar.getInstance().getTime().getTime();
        try {

            String uid = SharedPreferencesManager.getUID(WriteMemoActivity.this);
            String personHash = SharedPreferencesManager.getPersonHash(WriteMemoActivity.this);

            Memo memo = new Memo(
                    uid,
                    personHash,
                    //누가 누구의 메모를 언제 썼다는 유니크한 키
                    HashConverter.hashingFromString(uid + personHash + time)
            );

            memo.setBookmark(bookmarkButton.isSelected());
            memo.setTitle(title);
            memo.setContent(content);
            memo.setCreate(time);
            memo.setUpdate(time);

            return memo;

        } catch (NoSuchAlgorithmException e) {
            return null;
        }

    }

}