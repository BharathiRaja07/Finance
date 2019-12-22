package com.bharathi.finance.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bharathi.finance.MainActivity;
import com.bharathi.finance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProfileActivity extends AppCompatActivity {
    Button save;
    EditText name,phoneNumber,mailId,dob,address;
    CheckBox agent,customer;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        initilaizeUI();
        database = FirebaseDatabase.getInstance();

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
                DatabaseReference myRef = database.getReference("users");
                myRef.setValue(userProfile);

                Intent intent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
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
