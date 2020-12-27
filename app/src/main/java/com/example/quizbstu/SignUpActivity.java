package com.example.quizbstu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    Button loginfromregistr, signup;
    EditText loginameregistr, emailregistr, fullnaemregistr, password1registr, password2registr;
    ProgressBar progressBar;
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Регистрация");

        loginfromregistr = (Button) findViewById(R.id.loginfromregistr);
        signup = (Button)findViewById(R.id.signup);
        loginameregistr = (EditText)findViewById(R.id.loginnameregistr);
        emailregistr = (EditText)findViewById(R.id.email_reistr_edit);
        fullnaemregistr = (EditText)findViewById(R.id.fullnameregistr);
        password1registr = (EditText)findViewById(R.id.password1registr);
        password2registr = (EditText)findViewById(R.id.password2registr);
        progressBar = (ProgressBar)findViewById(R.id.progressbarsignup);

        loginfromregistr.setOnClickListener(view -> loginfromregistr());

        signup.setOnClickListener(view -> checkform());

    }

    private void loginfromregistr(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
    private void checkform(){
        String patternlogin = "^[a-zA-Z0-9_]{5,20}$";
        String patternemail = "^[a-zA-Z0-9]{0,15}[@][a-zA-Z0-9]{0,15}[.][a-zA-Z]{0,5}$";
        String patternpassword1 = "^[a-zA-Z0-9]{6,20}$";
        String patternpassword2 = "^[a-zA-Z0-9]{6,20}$";

        Pattern patt = Pattern.compile(patternlogin);
        Pattern patt1 = Pattern.compile(patternemail);
        Pattern patt3 = Pattern.compile(patternpassword1);
        Pattern patt4 = Pattern.compile(patternpassword2);

        Matcher match = patt.matcher(loginameregistr.getText().toString());
        Matcher match1 = patt1.matcher(emailregistr.getText().toString());
        Matcher match3 = patt3.matcher(password1registr.getText().toString());
        Matcher match4 = patt3.matcher(password2registr.getText().toString());


        if(!match1.matches()){
            Toast.makeText(this, "Неверный формат элекстронной почты", Toast.LENGTH_LONG).show();
            emailregistr.setTextColor(Color.rgb(255, 0, 0));
        }
        else if(!match.matches()){
            Toast.makeText(this, "Логин должен быть короче 30 и больше 5 символов", Toast.LENGTH_LONG).show();
            loginameregistr.setTextColor(Color.rgb(255, 0, 0));
        }
        else if(!match3.matches()){
            Toast.makeText(this, "Неверный формат пароля", Toast.LENGTH_LONG).show();
            password1registr.setTextColor(Color.rgb(255, 0, 0));
        }
        else if(!match4.matches()){
            Toast.makeText(this, "Неверный формат пароля 2", Toast.LENGTH_LONG).show();
            password2registr.setTextColor(Color.rgb(255, 0, 0));
        }
        else if(!password2registr.getText().toString().equals(password1registr.getText().toString())){
            Toast.makeText(this, "Неверный повторный пароль", Toast.LENGTH_LONG).show();
            password2registr.setTextColor(Color.rgb(255, 0, 0));
        }
        else{
            CheckRegistration checkRegistration = new CheckRegistration();
            checkRegistration.execute("");
            loginfromregistr();
        }
    }
    public class CheckRegistration extends AsyncTask<String,String,String> {
        private static final String IP = "80.94.168.145";
        private static final String DBNAME = "Khimach_project";
        private static final String USERNAME = "student";
        private static final String PASSWORD = "Pa$$w0rd";

        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(SignUpActivity.this, r, Toast.LENGTH_SHORT).show();
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
                    z = "Check Your Internet Access!";
                }
                else
                {
                    PreparedStatement preparedStatement = null;
                    preparedStatement = con.prepareStatement("insert into Registration(Email, LoginName, FullName, Password1) " +
                            "values(?, ?, ?, ?)");
                    preparedStatement.setString(1, emailregistr.getText().toString());
                    preparedStatement.setString(2, loginameregistr.getText().toString());
                    preparedStatement.setString(3, fullnaemregistr.getText().toString());
                    preparedStatement.setString(4, password1registr.getText().toString());
                    preparedStatement.addBatch();
                    preparedStatement.executeBatch();
                    z="Успешная регистрация";
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