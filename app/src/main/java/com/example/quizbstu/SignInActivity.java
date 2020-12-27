package com.example.quizbstu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quizbstu.Konst.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    Button registfromlogin, signin;
    EditText loginsignin, passwordsignin;
    ProgressBar progressBar;

    Connection con;
    String un,pass,db,ip;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().setTitle("Авторизация");

        ip = "80.94.168.145";
        db = "Khimach_project";
        un = "student";
        pass = "Pa$$w0rd";

        registfromlogin = (Button)findViewById(R.id.registfromlogin);
        signin = (Button)findViewById(R.id.signin);
        loginsignin = (EditText)findViewById(R.id.loginsignin);
        passwordsignin = (EditText)findViewById(R.id.passwordsignin);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        registfromlogin.setOnClickListener(view ->  registrfromlogin());

        View.OnClickListener signinlist = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute("");
            }
        };
        signin.setOnClickListener(signinlist);
    }
    private void loginin(){
        Intent intent = new Intent(this, StartActivity.class);
        intent.putExtra("id", user.getId());
        intent.putExtra("Login", user.getLoginName());
        intent.putExtra("Fullname", user.getFullName());
        startActivity(intent);
    }

    private void registrfromlogin(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void checkform(){
        String patternlogin = "^[a-zA-Z0-9_]{5,20}$";
        String patternpassword = "^[a-zA-Z0-9]{6,20}$";

        Pattern patt = Pattern.compile(patternlogin);
        Pattern patt1 = Pattern.compile(patternpassword);

        Matcher match = patt.matcher(loginsignin.getText().toString());
        Matcher match1 = patt1.matcher(passwordsignin.getText().toString());
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
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
            Toast.makeText(SignInActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {

                Log.d("proj", user.getEmail()+ user.getLoginName());
                loginin();
                finish();
            }
        }
        @Override
        protected String doInBackground(String... params)
        {
            String usernam = loginsignin.getText().toString();
            String passwordd = passwordsignin.getText().toString();
            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                z = "Please enter Username and Password";
            else
            {
                try
                {
                    con = connectionclass(un, pass, db, ip);        // Connect to database
                    if (con == null)
                    {
                        z = "Check Your Internet Access!";
                    }
                    else
                    {
                        String query = "select * from Registration where LoginName = '" + usernam.toString() + "' and Password1 = '"+ passwordd.toString() +"'  ";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            z = "Login successful";
                            isSuccess=true;
                            user.setId(rs.getInt(1));
                            user.setEmail(rs.getString(2));
                            user.setLoginName(rs.getString(3));
                            user.setFullName(rs.getString(4));
                            user.setPassword1(rs.getString(5));
                            con.close();
                        }
                        else
                        {
                            z = "Invalid Credentials!";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }
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