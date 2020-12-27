package com.example.quizbstu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.quizbstu.DB.DbHelper;
import com.example.quizbstu.Konst.Category;
import com.example.quizbstu.Konst.Question;
import com.example.quizbstu.Konst.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private TextView textViewHighscore;
    private Spinner spinnerCategory;
    private Spinner spinnerDifficulty;
    private int highscore;
    User user = new User();
    String id, login, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().setTitle("Главная страница");

        textViewHighscore = findViewById(R.id.text_view_highscore);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get("id").toString();
        login = arguments.get("Login").toString();
        fullname = arguments.get("Fullname").toString();
        Log.d("proj", login + " " + fullname);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePage()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selecteFragment = null;
                    switch (item.getItemId()){
                        case R.id.fragment_home_page:
                            selecteFragment = new HomePage();
                            break;
                        case R.id.fragment_top_students:
                            selecteFragment = new TopStudentsFragment();
                            break;
                        case R.id.fragment_student_results:
                            selecteFragment = new StudentResults();
                            break;
                        case R.id.exitbutton:
                            Intent intent = new Intent(StartActivity.this, SignInActivity.class);
                            intent.putExtra("id", "");
                            intent.putExtra("Login", "");
                            intent.putExtra("Fullname", "");
                            startActivity(intent);
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selecteFragment).commit();
                    return true;
                }
            };
}