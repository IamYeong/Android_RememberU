package com.gmail.wjdrhkddud2.rememberu.detail;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gmail.wjdrhkddud2.rememberu.HashConverter;
import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;
import com.google.firebase.auth.FirebaseAuth;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ModifyMemoFragment extends Fragment {

    private Context context;
    private ImageButton closeButton, bookmarkButton, deleteButton;
    private EditText titleField, contentField;
    private TextView dateText;
    private Button saveButton;

    private OnFragmentDetachListener detachListener;

    public ModifyMemoFragment() {
        // Required empty public constructor
    }

    public void setDetachListener(OnFragmentDetachListener listener) {
        this.detachListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_memo, container, false);

        closeButton = view.findViewById(R.id.img_btn_close_write);
        saveButton = view.findViewById(R.id.btn_save_memo);
        dateText = view.findViewById(R.id.tv_date_write_memo);
        titleField = view.findViewById(R.id.et_memo_title_write);
        contentField = view.findViewById(R.id.et_content_write_memo);
        bookmarkButton = view.findViewById(R.id.img_btn_bookmark_memo_write);
        deleteButton = view.findViewById(R.id.img_btn_delete_memo);
        deleteButton.setVisibility(View.VISIBLE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialog

                RememberUDatabase db = RememberUDatabase.getInstance(context);
                db.memoDao().delete(SharedPreferencesManager.getMemoHash(context));
                close(context);

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                close(context);

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Memo memo = isExactly();
                if (memo == null) return;

                RememberUDatabase db = RememberUDatabase.getInstance(context);
                db.memoDao().update(memo);

                close(context);

            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkButton.setSelected(!bookmarkButton.isSelected());
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();

        RememberUDatabase db = RememberUDatabase.getInstance(context);
        Memo memo = db.memoDao().select(SharedPreferencesManager.getMemoHash(context));

        titleField.setText(memo.getTitle());
        contentField.setText(memo.getContent());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        dateText.setText(simpleDateFormat.format(memo.getCreate()));
        bookmarkButton.setSelected(memo.isBookmark());

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

        RememberUDatabase db = RememberUDatabase.getInstance(context);
        Memo memo = db.memoDao().select(SharedPreferencesManager.getMemoHash(context));

        memo.setBookmark(bookmarkButton.isSelected());
        memo.setTitle(title);
        memo.setContent(content);
        memo.setUpdate(time);

        return memo;

    }

    private void close(Context context) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(ModifyMemoFragment.this);
        fragmentTransaction.commit();
        detachListener.onDetach();
    }

}