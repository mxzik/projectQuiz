package com.example.quizbstu.Konst;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizbstu.R;

import org.w3c.dom.Text;

import java.util.List;

public class TopStudAdapter extends RecyclerView.Adapter<TopStudAdapter.ViewHolder> {
    public final LayoutInflater inflater;
    public final List<ListofTopStudents> listofTopStudents;

    public TopStudAdapter(Context context, List<ListofTopStudents> listofTopStudents){
        this.listofTopStudents = listofTopStudents;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TopStudAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.list_of_top_students, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopStudAdapter.ViewHolder holder, int position) {
        ListofTopStudents listofTopStudents1 = listofTopStudents.get(position);
        holder.idstudView.setText(listofTopStudents1.getStudentsid());
        holder.namestudView.setText(listofTopStudents1.getNamestudent());
        holder.resultstudView.setText(listofTopStudents1.getResult());
    }

    @Override
    public int getItemCount() {
        return listofTopStudents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView idstudView, namestudView, resultstudView;
        ViewHolder(View view){
            super(view);
            idstudView = (TextView)view.findViewById(R.id.idstudent);
            namestudView = (TextView)view.findViewById(R.id.namestud);
            resultstudView = (TextView)view.findViewById(R.id.result);
        }
    }
}
