package com.mirea.kt.android2023.birthdayapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mirea.kt.android2023.birthdayapp.adapter.PersonAdapter;
import com.mirea.kt.android2023.birthdayapp.calculator.Calculator;
import com.mirea.kt.android2023.birthdayapp.database.DBManager;
import com.mirea.kt.android2023.birthdayapp.model.Person;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PersonActivity extends AppCompatActivity {

    private TextView textViewName, textViewToBirthday, textViewAge, textViewBirthday, textViewDateOfBirthday;
    private ActionBar actionBar;
    private DBManager dbManager;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        textViewName = findViewById(R.id.textViewPersonName);
        textViewToBirthday = findViewById(R.id.textViewPersonWhenIsBirthday);
        textViewAge = findViewById(R.id.textViewPersonAge);
        textViewBirthday = findViewById(R.id.textViewWhenIsBirthday);
        textViewDateOfBirthday = findViewById(R.id.textViewPersonDate);

        Toolbar toolbar = findViewById(R.id.toolbarPerson);
        dbManager = new DBManager();

        person = dbManager.getPerson(getIntent().getStringExtra("name"));

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(person.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Date birthday = person.getDateOfBirth();
        LocalDate localDate = LocalDate.of(birthday.getYear(), birthday.getMonth()+1, birthday.getDate());

        textViewName.setText(person.getName());

        textViewDateOfBirthday.setText(birthdayDate());

        textViewAge.setText(Calculator.calculateAge(localDate));

        String whenIsBirthday = Calculator.calculateDaysToBirthday(localDate);

        if (whenIsBirthday.equals("birthday")) {
            textViewBirthday.setText("С днем Рождения!");
        } else {
            textViewToBirthday.setText(whenIsBirthday);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            Log.i("app_tag", "refresh menu item was selected");
            refresh();

            return true;
        }

        if (id == R.id.action_update) {
            Log.i("app_tag", "update date menu item was selected");
            update();

            return true;
        }

        if (id == android.R.id.home) {
            Log.i("app_tag", "button back was pressed");
            onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        Date birthday = person.getDateOfBirth();
        LocalDate localDate = LocalDate.of(birthday.getYear(), birthday.getMonth()+1, birthday.getDate());

        textViewAge.setText(Calculator.calculateAge(localDate));

        String whenIsBirthday = Calculator.calculateDaysToBirthday(localDate);

        if (whenIsBirthday.equals("birthday")) {
            textViewBirthday.setText("С днем Рождения!");
        } else {
            textViewToBirthday.setText(whenIsBirthday);
        }
    }

    private void update() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View addDialogView = getLayoutInflater().inflate(R.layout.dialog_update_date, null);

        EditText editTextName = addDialogView.findViewById(R.id.editTextUpdateDate);
        EditText editTextYear = addDialogView.findViewById(R.id.editTextUpdateDateYear);
        EditText editTextMonth = addDialogView.findViewById(R.id.editTextUpdateDateMonth);
        EditText editTextDay = addDialogView.findViewById(R.id.editTextUpdateDateDay);
        Button button = addDialogView.findViewById(R.id.buttonUpdateDate);

        editTextName.setText(person.getName());
        editTextYear.setText("" + person.getDateOfBirth().getYear());
        editTextMonth.setText("" + person.getDateOfBirth().getMonth());
        editTextDay.setText("" + person.getDateOfBirth().getDate());

        dialogBuilder.setView(addDialogView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        button.setOnClickListener(x -> {
            Log.i("app_tag", "update date button was pressed");

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
                case 2:
                    if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                        if (day > 29) {
                            Toast.makeText(this, "В феврале 29 дней!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        if (day > 28) {
                            Toast.makeText(this, "В феврале 28 дней!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                case 3:
                    if (day > 31) {
                        Toast.makeText(this, "В марте 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                case 4:
                    if (day > 30) {
                        Toast.makeText(this, "В апреле 30 дней!", Toast.LENGTH_LONG).show();
                        return;
                    }
                case 5:
                    if (day > 31) {
                        Toast.makeText(this, "В мае 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                case 6:
                    if (day > 30) {
                        Toast.makeText(this, "В июне 30 дней!", Toast.LENGTH_LONG).show();
                        return;
                    }
                case 7:
                    if (day > 31) {
                        Toast.makeText(this, "В июле 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                case 8:
                    if (day > 31) {
                        Toast.makeText(this, "В августе 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                case 9:
                    if (day > 30) {
                        Toast.makeText(this, "В сентябре 30 дней!", Toast.LENGTH_LONG).show();
                        return;
                    }
                case 10:
                    if (day > 31) {
                        Toast.makeText(this, "В октябре 31 день!", Toast.LENGTH_LONG).show();
                        return;
                    }
                case 11:
                    if (day > 30) {
                        Toast.makeText(this, "В ноябре 30 дней!", Toast.LENGTH_LONG).show();
                        return;
                    }
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
            Person newPerson = new Person(name, birthday);

            dbManager.deletePerson(person.getName());
            dbManager.addPerson(newPerson);
            person = dbManager.getPerson(newPerson.getName());

            actionBar.setTitle(person.getName());
            textViewName.setText(person.getName());
            textViewDateOfBirthday.setText(birthdayDate());

            refresh();

            dialog.cancel();
        });
    }

    private String birthdayDate() {
        Date birthday = person.getDateOfBirth();
        LocalDate localDate = LocalDate.of(birthday.getYear(), birthday.getMonth()+1, birthday.getDate());

        String month;
        if (localDate.getMonthValue() < 10) {
            month = "0" + localDate.getMonthValue();
        } else {
            month = String.valueOf(localDate.getMonthValue());
        }

        String day;
        if (localDate.getDayOfMonth() < 10) {
            day = "0" + localDate.getDayOfMonth();
        } else {
            day = String.valueOf(localDate.getDayOfMonth());
        }

        return day + "." + month + "." + localDate.getYear();
    }
}