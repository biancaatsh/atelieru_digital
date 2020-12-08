package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String OUR_URL = "https://developer.android.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        WebView webView = findViewById(R.id.webview);
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webView.loadUrl(OUR_URL);
        List<String> data = getColors();
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                data
        );
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(myAdapter);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectITem = myAdapter.getItem(position);
                    Toast.makeText(MainActivity.this, selectITem, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(MainActivity.this, "Empty adapter!", Toast.LENGTH_LONG).show();
                }
            });


        Button emptyButton = findViewById(R.id.empty_button);
        emptyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                myAdapter.clear();
            }
                                       }


        );
    }



    private List<String> getColors() {
        List<String> colors = new ArrayList<>();
        colors.add("Yellow");
        colors.add("Green");
        colors.add("Red");
        colors.add("Pink");
        colors.add("Orange");
        return colors;
    }
}