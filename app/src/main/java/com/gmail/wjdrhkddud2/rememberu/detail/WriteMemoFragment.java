package com.gmail.wjdrhkddud2.rememberu.detail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class WriteMemoFragment extends Fragment {

    private Context context;
    private ImageButton closeButton, bookmarkButton;
    private EditText titleField, contentField;
    private TextView dateText;
    private Button saveButton;

    private OnFragmentDetachListener detachListener;

    public WriteMemoFragment() {
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
                db.memoDao().insert(memo);

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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        dateText.setText(simpleDateFormat.format(Calendar.getInstance().getTime().getTime()));

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
        try {

            String uid = SharedPreferencesManager.getUID(context);
            String personHash = SharedPreferencesManager.getPersonHash(context);

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

    private void close(Context context) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(WriteMemoFragment.this);
        fragmentTransaction.commit();
        detachListener.onDetach();

    }

}