package com.example.week4;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentRecylerAdapter extends RecyclerView.Adapter<StudentViewHolder> {

    private final List<Student> students;

    public StudentRecylerAdapter(List<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view  = View.inflate(viewGroup.getContext(),R.layout.student_row, null);
        return new StudentViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder studentViewHolder, int position) {
        Student student = students.get(position);

        studentViewHolder.getFirstnameText().setText(student.getFirstName());
        studentViewHolder.getLastnameText().setText(student.getLastName());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }
}
