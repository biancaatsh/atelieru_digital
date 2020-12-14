package com.example.week4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Student> students = getStudents();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        StudentRecylerAdapter adapter = new StudentRecylerAdapter(students);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private  List<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();

        students.add(new Student("firstName 1","lastName 1"));
        students.add(new Student("firstName 2","lastName 2"));
        students.add(new Student("firstName 3","lastName 3"));
        students.add(new Student("firstName 4","lastName 4"));
        students.add(new Student("firstName 5","lastName 5"));
        students.add(new Student("firstName 6","lastName 6"));
        students.add(new Student("firstName 7","lastName 7"));
        students.add(new Student("firstName 8","lastName 8"));
        students.add(new Student("firstName 9","lastName 9"));
        students.add(new Student("firstName 10","lastName 10"));
        students.add(new Student("firstName 11","lastName 11"));
        students.add(new Student("firstName 12","lastName 12"));

        return students;
    }
}