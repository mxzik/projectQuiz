package com.example.quizbstu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizbstu.Konst.ListofTopStudents;
import com.example.quizbstu.Konst.TopStudAdapter;
import com.example.quizbstu.SqlServerQuery.SelectTopStudents;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TopStudentsFragment extends Fragment {

    ArrayList<ListofTopStudents> listofTopStudents = new ArrayList<ListofTopStudents>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_students, null );
        try {
            RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recycleviewtopstudents);
            SelectTopStudents selectTopStudents = new SelectTopStudents();
            ResultSet resultSet = selectTopStudents.execute().get();
            while (resultSet.next()) {
                listofTopStudents.add(new ListofTopStudents(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
            }
            TopStudAdapter adapter = new TopStudAdapter(getActivity(), listofTopStudents);
            recyclerView.setAdapter(adapter);
            resultSet.close();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return v;

    }
}