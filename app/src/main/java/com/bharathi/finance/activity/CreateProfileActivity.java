package com.bharathi.finance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bharathi.finance.R;
import com.bharathi.finance.user.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateProfileActivity extends AppCompatActivity {
    Button save;
    EditText name,phoneNumber,mailId,dob,address;
    CheckBox agent,customer;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        initilaizeUI();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile userProfile = new UserProfile();
                userProfile.setName(name.getText().toString());
                userProfile.setPhoneNumber(phoneNumber.getText().toString());
                userProfile.setAddress(address.getText().toString());
                userProfile.setAgent(agent.isChecked());
                userProfile.setCustomer(customer.isChecked());
                userProfile.setEmailId(mailId.getText().toString());
                userProfile.setDob(dob.getText().toString());
                userProfile.setGender("Male");
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser != null) {
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userid);

                    databaseReference.setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                   address.setError("user is null");
                   address.requestFocus();
                   return;
                }



            }
        });
    }

    private void initilaizeUI(){
        save = findViewById(R.id.save);
        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phone);
        mailId = findViewById(R.id.email);
        dob = findViewById(R.id.dateOfBirth);
        address = findViewById(R.id.address);
        agent = findViewById(R.id.agent);
        customer = findViewById(R.id.customer);

    }

}
