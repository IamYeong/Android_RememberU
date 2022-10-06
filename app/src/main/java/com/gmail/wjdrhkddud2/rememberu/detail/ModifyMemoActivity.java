package com.gmail.wjdrhkddud2.rememberu.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private boolean isEdit = false;

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

        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEdit = true;
            }
        });

        contentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEdit = true;
            }
        });

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

                if (!isEdit) {
                    finish();
                    return;
                }

                SelectionDialog selectionDialog = new SelectionDialog(ModifyMemoActivity.this);
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

                RememberUDatabase db = RememberUDatabase.getInstance(ModifyMemoActivity.this);
                db.memoDao().update(memo);

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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (!isEdit) {
            finish();
            return;
        }

        SelectionDialog selectionDialog = new SelectionDialog(ModifyMemoActivity.this);
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
        //try {

            RememberUDatabase db = RememberUDatabase.getInstance(ModifyMemoActivity.this);
            Memo memo = db.memoDao().select(SharedPreferencesManager.getMemoHash(ModifyMemoActivity.this));

            memo.setBookmark(bookmarkButton.isSelected());
            memo.setTitle(title);
            memo.setContent(content);
            memo.setUpdate(time);

            return memo;

       // } catch (NoSuchAlgorithmException e) {
       //     return null;
       // }

    }
}