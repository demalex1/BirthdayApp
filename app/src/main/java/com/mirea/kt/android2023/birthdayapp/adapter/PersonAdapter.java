package com.mirea.kt.android2023.birthdayapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kt.android2023.birthdayapp.R;
import com.mirea.kt.android2023.birthdayapp.calculator.Calculator;
import com.mirea.kt.android2023.birthdayapp.model.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<Person> people;
    private OnPersonClickListener onPersonClickListener;

    public PersonAdapter(List<Person> people, OnPersonClickListener onPersonClickListener) {
        this.people = people;
        this.onPersonClickListener = onPersonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person person = people.get(position);

        holder.name.setText(person.getName());

        LocalDate birthday = LocalDate.of(person.getDateOfBirth().getYear(), person.getDateOfBirth().getMonth()+1, person.getDateOfBirth().getDate());
        String daysToBirthday = Calculator.calculateDaysToBirthday(birthday);

        if (daysToBirthday.equals("birthday")) {
            holder.daysToBirthday.setText("День Рождения!");
        } else {
            holder.daysToBirthday.setText("До дня Рождения: " + daysToBirthday);
        }

        holder.itemView.setOnClickListener(x -> {
            onPersonClickListener.onPersonClick(person, holder.getAdapterPosition());
        });

        holder.deleteImage.setOnClickListener(x -> {
            onPersonClickListener.onDeleteClick(person, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public interface OnPersonClickListener {
        void onPersonClick(Person person, int position);

        void onDeleteClick(Person person, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, daysToBirthday;
        private ImageView deleteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textViewPersonItemName);
            daysToBirthday = itemView.findViewById(R.id.textViewPersonItemDaysToBirthday);
            deleteImage = itemView.findViewById(R.id.imageViewDeletePersonItem);
        }
    }
}
