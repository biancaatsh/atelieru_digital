package com.example.week4;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentViewHolder extends RecyclerView.ViewHolder {
    private TextView firstnameText;
    private TextView  lastnameText;
    public  StudentViewHolder(@NonNull View itemView){
        super(itemView);
       firstnameText = itemView.findViewById(R.id.firstName);
        lastnameText = itemView.findViewById(R.id.lastName);
    }

    public TextView getFirstnameText() {
        return firstnameText;
    }

    public TextView getLastnameText() {
        return lastnameText;
    }
}
