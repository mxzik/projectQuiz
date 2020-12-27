package com.example.quizbstu.Konst;

import androidx.annotation.NonNull;

public class ListofTopStudents {
    private String studentsid;
    private String namestudent;
    private String result;
    public ListofTopStudents(){

    }
    public ListofTopStudents(String studentsid, String namestudent, String result){
        this.studentsid = studentsid;
        this.namestudent = namestudent;
        this.result = result;
    }

    public String getStudentsid() {
        return studentsid;
    }

    public void setStudentsid(String studentsid) {
        this.studentsid = studentsid;
    }

    public String getNamestudent() {
        return namestudent;
    }

    public void setNamestudent(String namestudent) {
        this.namestudent = namestudent;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
