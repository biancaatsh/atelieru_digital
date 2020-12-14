package com.example.challenge2week3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Screen4_5 extends AppCompatActivity {
    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_screen4_5);
        calendarView=(CalendarView)findViewById(R.id.calendar);
        List<String> optiuniSpinner=new ArrayList<>();
        optiuniSpinner.add("Cupcake");
        optiuniSpinner.add("Donut");
        optiuniSpinner.add("Eclair");
        optiuniSpinner.add("Kit-Kat");
        optiuniSpinner.add("Pie");
        Spinner spinner=findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,optiuniSpinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text=optiuniSpinner.get(position);
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}