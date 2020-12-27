package com.example.quizbstu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizbstu.DB.DbHelper;
import com.example.quizbstu.Konst.Question;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCategory;
    private TextView textViewDifficulty;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;
    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;
    private long backPressedTime;
    private ProgressBar progressBar;
    Connection con;
    String id, login, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Квиз");

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCategory = findViewById(R.id.text_view_category);
        textViewDifficulty = findViewById(R.id.text_view_difficulty);
        textViewCountDown = findViewById(R.id.text_view_countdown);

        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();
        progressBar = (ProgressBar)findViewById(R.id.quizprogressbar);

        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(HomePage.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(HomePage.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(HomePage.EXTRA_DIFFICULTY);

        id = intent.getStringExtra("id");
        login = intent.getStringExtra("Login");
        fullname = intent.getStringExtra("Fullname");
        Log.d("proj", id+""+login + " " + fullname);

        textViewCategory.setText("Категория: " + categoryName);
        textViewDifficulty.setText("Сложность: " + difficulty);

        if (savedInstanceState == null) {
            DbHelper dbHelper = DbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID, difficulty);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);
            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }
    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            questionCounter++;
            textViewQuestionCount.setText("Вопрос: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Подтвердить");
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }
    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);
        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }
    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            textViewScore.setText("Score: " + score);
        }
        showSolution();
    }
    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
        }
        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }
    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        WriteToDb writeToDb = new WriteToDb();
        writeToDb.execute("");
        finish();
    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }

    public class WriteToDb extends AsyncTask<String,String,String> {
        private static final String IP = "80.94.168.145";
        private static final String DBNAME = "Khimach_project";
        private static final String USERNAME = "student";
        private static final String PASSWORD = "Pa$$w0rd";

        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(QuizActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                finish();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                con = connectionclass(USERNAME, PASSWORD, DBNAME, IP);
                if (con == null)
                {
                    z = "Проверьте подключение к интернету";
                }
                else
                {
                    PreparedStatement preparedStatement = null;
                    preparedStatement = con.prepareStatement("insert into Results(PersonsId, PersonsName, Result) " +
                            "values(?, ?, ?)");

                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, login);
                    preparedStatement.setInt(3, score);
                    preparedStatement.addBatch();
                    preparedStatement.executeBatch();
                    z="Данные сохранены";
                    isSuccess = true;
                    con.close();
                }
            }catch (Exception ex)
            {
                isSuccess = false;
                z = ex.getMessage();
            }
            return z;
        }
        @SuppressLint("NewApi")
        public Connection connectionclass(String user, String password, String database, String server)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection connection = null;
            String ConnectionURL = null;
            try
            {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnectionURL = "jdbc:jtds:sqlserver://" + server + "/" + database + ";user=" + user+ ";password=" + password + ";";
                connection = DriverManager.getConnection(ConnectionURL);
            }
            catch (SQLException se)
            {
                Log.e("error here 1 : ", se.getMessage());
            }
            catch (ClassNotFoundException e)
            {
                Log.e("error here 2 : ", e.getMessage());
            }
            catch (Exception e)
            {
                Log.e("error here 3 : ", e.getMessage());
            }
            return connection;
        }
    }

}