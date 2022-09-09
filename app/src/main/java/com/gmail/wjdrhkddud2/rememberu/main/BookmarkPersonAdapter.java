package com.gmail.wjdrhkddud2.rememberu.main;

import android.content.Context;
import android.content.Intent;
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
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;
import com.gmail.wjdrhkddud2.rememberu.detail.DetailActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BookmarkPersonAdapter extends RecyclerView.Adapter<BookmarkPersonViewHolder> {

    private Context context;
    private List<Person> people;
    private List<Person> results;

    public BookmarkPersonAdapter(Context context) {
        this.context = context;
        people = new ArrayList<>();
        results = new ArrayList<>();
    }

    public void setPeople(List<Person> people) {
        this.people.clear();
        this.results.clear();
        this.people.addAll(people);
        this.results.addAll(people);

        notifyDataSetChanged();
        //notifyItemRangeChanged(0, people.size());
    }

    @NonNull
    @Override
    public BookmarkPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bookmark_card , parent, false);
        return new BookmarkPersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkPersonViewHolder holder, int position) {

        Person person = results.get(holder.getAdapterPosition());

        holder.getNameText().setText(person.getName());
        holder.getContentText().setText(person.getPhoneNumber());
        holder.getBookmarkButton().setSelected(person.isBookmark());


        holder.getBookmarkButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isSelected = person.isBookmark();

                person.setBookmark(!isSelected);
                RememberUDatabase db = RememberUDatabase.getInstance(context);
                db.personDao().update(person);

                holder.getBookmarkButton().setSelected(!isSelected);

            }
        });

        holder.getCardLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferencesManager.setPersonHash(context, person.getHashed());

                Intent intent = new Intent(context, DetailActivity.class);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return (results != null ? results.size() : 0);
    }
}

class BookmarkPersonViewHolder extends RecyclerView.ViewHolder {

    private ConstraintLayout cardLayout;
    private TextView nameText, contentText;
    private ImageButton bookmarkButton;

    public BookmarkPersonViewHolder(@NonNull View itemView) {
        super(itemView);

        cardLayout = itemView.findViewById(R.id.constraint_card_bookmark_main);
        nameText = itemView.findViewById(R.id.tv_bookmark_name_main);
        contentText = itemView.findViewById(R.id.tv_bookmark_content_main);
        bookmarkButton = itemView.findViewById(R.id.img_btn_bookmark_main_bookmark_list);

    }

    public ConstraintLayout getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(ConstraintLayout cardLayout) {
        this.cardLayout = cardLayout;
    }

    public TextView getNameText() {
        return nameText;
    }

    public void setNameText(TextView nameText) {
        this.nameText = nameText;
    }

    public TextView getContentText() {
        return contentText;
    }

    public void setContentText(TextView contentText) {
        this.contentText = contentText;
    }

    public ImageButton getBookmarkButton() {
        return bookmarkButton;
    }

    public void setBookmarkButton(ImageButton bookmarkButton) {
        this.bookmarkButton = bookmarkButton;
    }
}
