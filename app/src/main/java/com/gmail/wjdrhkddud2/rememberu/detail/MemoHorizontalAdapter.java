package com.gmail.wjdrhkddud2.rememberu.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;

import java.util.ArrayList;
import java.util.List;

public class MemoHorizontalAdapter extends RecyclerView.Adapter<MemoHorizontalViewHolder> {

    private Context context;
    private List<Memo> memos;
    private List<Memo> results;

    private OnMemoSelectedListener mListener;

    public MemoHorizontalAdapter(Context context) {
        this.context = context;
        memos = new ArrayList<>();
        results = new ArrayList<>();
    }

    public void setSelectListener(OnMemoSelectedListener listener) {
        this.mListener = listener;
    }

    public void setMemo(List<Memo> memoList) {

        List<Memo> marked = new ArrayList<>();
        for (Memo memo : memoList) {
            if (memo.isBookmark()) marked.add(memo);
        }

        memos.clear();
        results.clear();
        memos.addAll(marked);
        results.addAll(marked);

    }

    public void sortWhat() {

    }

    @NonNull
    @Override
    public MemoHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo_mark_card, parent, false);
        return new MemoHorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoHorizontalViewHolder holder, int position) {

        Memo memo = results.get(holder.getAdapterPosition());

        holder.getTitleText().setText(memo.getTitle());
        holder.getContentText().setText(memo.getContent());

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

class MemoHorizontalViewHolder extends RecyclerView.ViewHolder {

    private ConstraintLayout layout;
    private TextView titleText, contentText;

    public MemoHorizontalViewHolder(@NonNull View itemView) {
        super(itemView);

        layout = itemView.findViewById(R.id.layout_memo_bookmark_detail);
        titleText = itemView.findViewById(R.id.tv_title_bookmark_memo_detail);
        contentText = itemView.findViewById(R.id.tv_content_bookmark_memo_detail);
    }

    public ConstraintLayout getLayout() {
        return layout;
    }

    public void setLayout(ConstraintLayout layout) {
        this.layout = layout;
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
}