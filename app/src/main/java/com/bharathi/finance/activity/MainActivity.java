package com.bharathi.finance.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bharathi.finance.R;
import com.bharathi.finance.navigation.MainPanel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    private EditText editTextMobile;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        //check if user is null
        if (firebaseUser != null){
            Intent intent = new Intent(MainActivity.this, MainPanel.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        setContentView(R.layout.activity_main);

        editTextMobile = findViewById(R.id.editTextMobile);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
        findViewById(R.id.tempButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainPanel.class);
                startActivity(intent);
            }
        });
    }

}