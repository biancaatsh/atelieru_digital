package com.example.traveljournal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

    //aici creez partea de login in si register utilizand firebaseAuth
public class MainActivity extends AppCompatActivity {
    //declar instanta lui FirebaseAuth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // definesc constantele
    private Button mSignIn;
    private Button mSignUp;
    private EditText mEmail;
    private EditText mPassword;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //initializez Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }
  // imi iau elementele dupa id
    private void initView() {
        mSignIn=findViewById(R.id.sign_in);
        mSignUp=findViewById(R.id.sign_up);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
    }

  // creez onSignIn, metoda ce ia adresa de mail si parola, le valideaza
  //apoi autentifica un user cu metoda signInEmailAndPassword
    public void onSignIn(View view) {
        if(mEmail.getText().toString().isEmpty())
            Toast.makeText(MainActivity.this, "Please insert Email.", Toast.LENGTH_SHORT).show();
        else
            if(mPassword.getText().toString().isEmpty())
                Toast.makeText(MainActivity.this, "Please insert Password.", Toast.LENGTH_SHORT).show();
            else {
                mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.e(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(MainActivity.this, "Sign In Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent myIntent = new Intent(MainActivity.this, NavigationMenuActivity.class);
                                    //startActivityForResult(myIntent,1);
                                    startActivity(myIntent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.e(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Sign In Failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }

    }

    // creez un nou cont in care metoda onSignUp ia adresa de mail, parola, le valideaza
    // apoi creaza un nou user cu metoda
    // createUserWithEmailAndPassowrd
    public void onSignUp(View view) {
        if(mEmail.getText().toString().isEmpty())
            Toast.makeText(MainActivity.this, "Please insert Email.", Toast.LENGTH_SHORT).show();
        else
        if(mPassword.getText().toString().isEmpty())
            Toast.makeText(MainActivity.this, "Please insert Password.", Toast.LENGTH_SHORT).show();
        else {
            mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Account Created.",
                                        Toast.LENGTH_SHORT).show();
                                mSignUp.setVisibility(View.GONE);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Sing up failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }
}
