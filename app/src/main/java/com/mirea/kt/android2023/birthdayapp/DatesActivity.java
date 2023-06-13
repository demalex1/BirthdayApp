package com.mirea.kt.android2023.birthdayapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mirea.kt.android2023.birthdayapp.adapter.PersonAdapter;
import com.mirea.kt.android2023.birthdayapp.database.DBManager;
import com.mirea.kt.android2023.birthdayapp.model.Person;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class DatesActivity extends AppCompatActivity implements PersonAdapter.OnPersonClickListener {

    private RecyclerView recyclerView;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);

        Realm.init(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Toolbar toolbar = findViewById(R.id.toolbarDates);
        dbManager = new DBManager();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Даты");
        }

        List<Person> people = dbManager.getAllPersons();
        recyclerView.setAdapter(new PersonAdapter(people, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_date) {
            Log.i("app_tag", "add date menu item was selected");
            showAddDateDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPersonClick(Person person, int position) {
        Intent intent = new Intent(DatesActivity.this, PersonActivity.class);
        intent.putExtra("name", person.getName());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Person person, int position) {
        dbManager.deletePerson(person.getName());

        recyclerView.setAdapter(new PersonAdapter(dbManager.getAllPersons(), this));
    }

    @Override
    protected void onResume() {
        recyclerView.setAdapter(new PersonAdapter(dbManager.getAllPersons(), this));

        super.onResume();
    }

    private void showAddDateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View addDialogView = getLayoutInflater().inflate(R.layout.dialog_add_date, null);

        EditText editTextName = addDialogView.findViewById(R.id.editTextAddDate);
        EditText editTextYear = addDialogView.findViewById(R.id.editTextAddDateYear);
        EditText editTextMonth = addDialogView.findViewById(R.id.editTextAddDateMonth);
        EditText editTextDay = addDialogView.findViewById(R.id.editTextAddDateDay);
        Button button = addDialogView.findViewById(R.id.buttonAddDate);

        dialogBuilder.setView(addDialogView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        button.setOnClickListener(x -> {
            Log.i("app_tag", "add date button was pressed");

            String name = editTextName.getText().toString();
            String yearStr = editTextYear.getText().toString();
            String monthStr = editTextMonth.getText().toString();
            String dayStr = editTextDay.getText().toString();

            if (name.isEmpty() || yearStr.isEmpty() || monthStr.isEmpty() || dayStr.isEmpty()) {
                Toast.makeText(this, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show();
                return;
            }

            LocalDate date = LocalDate.now();
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);

            if (year < 1) {
                Toast.makeText(this, "Год должен быть больше нуля!", Toast.LENGTH_LONG).show();
                return;
            }
            if (year > date.getYear()) {
                Toast.makeText(this, "Этот год еще не наступил!", Toast.LENGTH_LONG).show();
                return;
            }

            if (month < 1 || month > 12) {
                Toast.makeText(this, "В году всего 12 месяцев!", Toast.LENGTH_LONG).show();
                return;
            }
            if (month > date.getMonthValue() && year == date.getYear()) {
                Toast.makeText(this, "Этот месяц еще не наступил!", Toast.LENGTH_LONG).show();
                return;
            }

            if (day < 1) {
                Toast.makeText(this, "День должен быть больше нуля!", Toast.LENGTH_LONG).show();
                return;
            }

            switch (month) {
                case 1:
                    if (day > 31) {
                        Toast.makeText(this, "В январе 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 2:
                    if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                        if (day > 29) {
                            Toast.makeText(this, "В феврале 29 дней!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    } else {
                        if (day > 28) {
                            Toast.makeText(this, "В феврале 28 дней!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    }
                case 3:
                    if (day > 31) {
                        Toast.makeText(this, "В марте 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 4:
                    if (day > 30) {
                        Toast.makeText(this, "В апреле 30 дней!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 5:
                    if (day > 31) {
                        Toast.makeText(this, "В мае 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 6:
                    if (day > 30) {
                        Toast.makeText(this, "В июне 30 дней!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 7:
                    if (day > 31) {
                        Toast.makeText(this, "В июле 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 8:
                    if (day > 31) {
                        Toast.makeText(this, "В августе 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 9:
                    if (day > 30) {
                        Toast.makeText(this, "В сентябре 30 дней!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 10:
                    if (day > 31) {
                        Toast.makeText(this, "В октябре 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 11:
                    if (day > 30) {
                        Toast.makeText(this, "В ноябре 30 дней!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 12:
                    if (day > 31) {
                        Toast.makeText(this, "В декабре 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
            }

            if (day > date.getDayOfMonth() && year == date.getYear()) {
                Toast.makeText(this, "Этот день еще не наступил!", Toast.LENGTH_LONG).show();
                return;
            }

            Date birthday = new Date(year, month-1, day);
            Person person = new Person(name, birthday);
            dbManager.addPerson(person);

            recyclerView.setAdapter(new PersonAdapter(dbManager.getAllPersons(), this));

            dialog.cancel();
        });
    }
}