package com.example.codechallengeex1login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity<emailPattern> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.id_button);
        TextView tv = findViewById(R.id.id_tvInput);
        CheckBox checkBox = findViewById(R.id.id_checkBox);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_Email = findViewById(R.id.id_email);
                EditText editText_Phone = findViewById(R.id.id_phone);
                String email = editText_Email.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String phone =editText_Phone.getText().toString().trim();
                if(editText_Email.getText()==null || email.isEmpty()){
                    editText_Email.setError("Fill the input with a valid address");
                }
                if(!email.matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(),"Fill the input with a valid address", Toast.LENGTH_SHORT).show();
                }
                if(editText_Phone.getText()==null || phone.isEmpty()){
                    editText_Phone.setError("Fill the input");
                }
                Boolean cb;
                if(checkBox.isChecked()){
                    cb = true;
                }
                else
                {
                    cb=false;
                }
                tv.setText("Input email:"+email+"\nInput Phone:"+phone+"\nInput checkBox"+cb.toString());git checkout
            }
        });



    }

}