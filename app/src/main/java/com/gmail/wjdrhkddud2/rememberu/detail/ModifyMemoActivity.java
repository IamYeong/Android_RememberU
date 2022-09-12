package com.gmail.wjdrhkddud2.rememberu.detail;

import androidx.appcompat.app.AppCompatActivity;

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

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ModifyMemoActivity extends AppCompatActivity {

    private ImageButton closeButton, bookmarkButton, deleteButton;
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
        deleteButton = findViewById(R.id.img_btn_delete_memo);
        deleteButton.setVisibility(View.VISIBLE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialog
                SelectionDialog selectionDialog = new SelectionDialog(ModifyMemoActivity.this);
                selectionDialog.setTitle(getString(R.string.title_delete_memo));
                selectionDialog.setSubtitle(getString(R.string.subtitle_delete_memo));
                selectionDialog.setSelectedListener(new OnSelectedListener() {
                    @Override
                    public void onSelect(boolean selection) {

                        if (selection) {
                            RememberUDatabase db = RememberUDatabase.getInstance(ModifyMemoActivity.this);
                            db.memoDao().delete(SharedPreferencesManager.getMemoHash(ModifyMemoActivity.this));
                            finish();
                        }

                    }
                });
                selectionDialog.show();

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Memo memo = isExactly();
                if (memo == null) return;

                RememberUDatabase db = RememberUDatabase.getInstance(ModifyMemoActivity.this);
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

        RememberUDatabase db = RememberUDatabase.getInstance(ModifyMemoActivity.this);
        Memo memo = db.memoDao().select(SharedPreferencesManager.getMemoHash(ModifyMemoActivity.this));

        titleField.setText(memo.getTitle());
        contentField.setText(memo.getContent());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        dateText.setText(simpleDateFormat.format(memo.getCreate()));
        bookmarkButton.setSelected(memo.isBookmark());
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

            String uid = SharedPreferencesManager.getUID(ModifyMemoActivity.this);
            String personHash = SharedPreferencesManager.getPersonHash(ModifyMemoActivity.this);

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