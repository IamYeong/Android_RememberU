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

import java.util.ArrayList;
import java.util.List;

public class ResultPersonAdapter extends RecyclerView.Adapter<ResultPersonViewHolder> {

    private Context context;
    private List<Person> people;
    private List<Person> results;

    public ResultPersonAdapter(Context context) {
        this.context = context;
        people = new ArrayList<>();
        results = new ArrayList<>();

    }

    public void setPeople(List<Person> people) {
        this.people.clear();
        this.results.clear();
        this.people.addAll(people);
        this.results.addAll(people);
    }

    public void searchByWord(String key) {

        results.clear();

        if (key.length() == 0) {
            results.addAll(people);
        } else {

            for (Person person : people) {

                if (person.getName().contains(key)) {
                    results.add(person);
                }

            }

        }

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ResultPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_result_card_main, parent, false);
        return new ResultPersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultPersonViewHolder holder, int position) {

        Person person = results.get(holder.getAdapterPosition());

        holder.getNameText().setText(person.getName());
        holder.getContentText().setText(person.getPhoneNumber());

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

class ResultPersonViewHolder extends RecyclerView.ViewHolder {

    private ConstraintLayout cardLayout;
    private TextView nameText, contentText;

    public ResultPersonViewHolder(@NonNull View itemView) {
        super(itemView);

        cardLayout = itemView.findViewById(R.id.constraint_card_result_main);
        nameText = itemView.findViewById(R.id.tv_result_name_main);
        contentText = itemView.findViewById(R.id.tv_result_content_main);

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
}
