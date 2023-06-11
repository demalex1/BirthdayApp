package com.mirea.kt.android2023.birthdayapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mirea.kt.android2023.birthdayapp.http.HTTPRunnable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private EditText editTextLogin, editTextPassword;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        button = findViewById(R.id.buttonLogin);

        button.setOnClickListener(x -> {
            Log.i("app_tag", "login button was pressed");

            String login = editTextLogin.getText().toString();
            String password = editTextPassword.getText().toString();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show();
                return;
            }

            String server = "https://android-for-students.ru";
            String serverPath = "/coursework/login.php";
            HashMap<String, String> map = new HashMap<>();
            map.put("lgn", login);
            map.put("pwd", password);
            map.put("g", "RIBO-01-21");
            HTTPRunnable httpRunnable = new HTTPRunnable(server + serverPath, map);
            Thread th = new Thread(httpRunnable);
            th.start();
            try {
                th.join();
            } catch (InterruptedException e) {
                Log.e("app_tag", e.getMessage());
            } finally {
                try {
                    JSONObject jsonObject = new JSONObject(httpRunnable.getResponseBody());
                    int result = jsonObject.getInt("result_code");

                    if (result == 1) {
                        Toast.makeText(this, "Успешно!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, DatesActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Неверный логин или пароль!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("app_tag", e.getMessage());
                }
            }
        });
    }
}