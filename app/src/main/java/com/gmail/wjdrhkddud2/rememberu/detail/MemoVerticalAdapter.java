package com.gmail.wjdrhkddud2.rememberu.detail;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MemoVerticalAdapter extends RecyclerView.Adapter<MemoVerticalViewHolder> {

    private Context context;
    private List<Memo> memos;
    private List<Memo> results;
    private OnMemoSelectedListener mListener;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public MemoVerticalAdapter(Context context) {
        this.context = context;
        memos = new ArrayList<>();
        results = new ArrayList<>();
    }

    public void setSelectListener(OnMemoSelectedListener listener) {
        this.mListener = listener;
    }

    public void setMemo(List<Memo> memoList) {
        this.memos.clear();
        this.results.clear();

        this.memos.addAll(memoList);
        this.results.addAll(memoList);
    }

    public void searchByWord(String key) {

        results.clear();

        if (key.length() == 0) {
            results.addAll(memos);
        } else {

            for (Memo memo : memos) {

                if (memo.getTitle().contains(key)) {
                    results.add(memo);
                }

            }

        }

        notifyDataSetChanged();


    }

    public void sortASC() {

        for (int i = results.size() - 1; i >= 0; i--) {

            for (int j = 1; j <= i; j++) {

                Memo memo = results.get(j);

                //j 번째 의 글자가 더 클 때 바꿔주는 것이므로 오름차순임
                if (results.get(j - 1).getTitle().compareTo(memo.getTitle()) > 0) {
                    results.set(j, results.get(j - 1));
                    results.set(j - 1, memo);
                }

            }

        }

    }

    public void sortDESC() {

        for (int i = results.size() - 1; i >= 0; i--) {

            for (int j = 1; j <= i; j++) {

                Memo memo = results.get(j);

                //j 번째 의 글자가 더 클 때 바꿔주는 것이므로 오름차순임
                if (results.get(j - 1).getTitle().compareTo(memo.getTitle()) <= 0) {
                    results.set(j, results.get(j - 1));
                    results.set(j - 1, memo);
                }

            }

        }

    }

    public void sortByDateASC() {

        for (int i = results.size() - 1; i >= 0; i--) {

            for (int j = 1; j <= i; j++) {

                Memo memo = results.get(j);

                //j 번째 의 글자가 더 클 때 바꿔주는 것이므로 오름차순임
                if (results.get(j - 1).getCreate() > memo.getCreate()) {
                    results.set(j, results.get(j - 1));
                    results.set(j - 1, memo);
                }

            }

        }

    }

    public void sortByDateDESC() {

        for (int i = results.size() - 1; i >= 0; i--) {

            for (int j = 1; j <= i; j++) {

                Memo memo = results.get(j);

                //j 번째 의 글자가 더 클 때 바꿔주는 것이므로 오름차순임
                if (results.get(j - 1).getCreate() < memo.getCreate()) {
                    results.set(j, results.get(j - 1));
                    results.set(j - 1, memo);
                }

            }

        }

    }

    public void filteringWhat() {

        results.clear();

        for (Memo memo : memos) {

            if (true) {
                results.add(memo);
            }

        }

    }


    @NonNull
    @Override
    public MemoVerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo_card, parent, false);
        return new MemoVerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoVerticalViewHolder holder, int position) {

        Memo memo = results.get(holder.getAdapterPosition());

        holder.getTitleText().setText(memo.getTitle());
        //holder.getContentText().setText(memo.getContent().substring(0, (Math.min(memo.getContent().length(), 20))));
        holder.getContentText().setText(memo.getContent());
        holder.getCreateDateText().setText(simpleDateFormat.format(memo.getCreate()));

        holder.getLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferencesManager.setMemoHash(context, memo.getHashed());
                mListener.onSelect(memo.getHashed());

            }
        });

    }

    @Override
    public int getItemCount() {
        return (results == null ? 0 : results.size());
    }
}

class MemoVerticalViewHolder extends RecyclerView.ViewHolder {

    private TextView titleText, contentText, createDateText;
    private ConstraintLayout layout;

    public MemoVerticalViewHolder(@NonNull View itemView) {
        super(itemView);

        titleText = itemView.findViewById(R.id.tv_memo_title);
        contentText = itemView.findViewById(R.id.tv_memo_content);
        layout = itemView.findViewById(R.id.constraint_vertical_memo);
        createDateText = itemView.findViewById(R.id.tv_create_date_detail);

    }

    public TextView getCreateDateText() {
        return createDateText;
    }

    public void setCreateDateText(TextView createDateText) {
        this.createDateText = createDateText;
    }

    public TextView getTitleText() {
        return titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public TextView getContentText() {
        return contentText;
    }

    public void setContentText(TextView contentText) {
        this.contentText = contentText;
    }

    public ConstraintLayout getLayout() {
        return layout;
    }

    public void setLayout(ConstraintLayout layout) {
        this.layout = layout;
    }
}