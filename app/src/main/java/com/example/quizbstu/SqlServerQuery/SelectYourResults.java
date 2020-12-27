package com.example.quizbstu.SqlServerQuery;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectYourResults extends AsyncTask<Integer, String, ResultSet> {
    private static final String TAG = SelectYourResults.class.getSimpleName();

    private static final String IP = "80.94.168.145";
    private static final String DBNAME = "Khimach_project";
    private static final String USERNAME = "student";
    private static final String PASSWORD = "Pa$$w0rd";

    ResultSet resultSet = null;

    @Override
    protected ResultSet doInBackground(Integer... params) {
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Log.d(TAG, "Driver is registered");

            ConnectionURL = "jdbc:jtds:sqlserver://" + IP + "/" + DBNAME + ";user="
                    + USERNAME + ";password=" + PASSWORD + ";";
            connection = DriverManager.getConnection(ConnectionURL);

            String query = "select PersonsId, PersonsName, Result from Results where PersonsId = " + params[0];
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            Log.e(TAG, throwable.getMessage());
        }

        return resultSet;
    }
}
