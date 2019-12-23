package com.bharathi.finance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bharathi.finance.R;
import com.bharathi.finance.user.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    TextView profile_name,profile_phoneNumber,profile_mailId,profile_dob,profile_address,profile_agent_customer;
    Button signOut;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initilaizeUI();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                profile_name.setText(user.getName());
                profile_phoneNumber.setText(user.getPhoneNumber());
                profile_mailId.setText(user.getEmailId());
                profile_dob.setText(user.getDob());
                profile_address.setText(user.getAddress());
                if(user.isAgent() && user.isCustomer()) {
                    profile_agent_customer.setText("You are Agent and Customer");
                } else if(user.isCustomer()){
                    profile_agent_customer.setText("You are Customer");

                } else if(user.isAgent()){
                    profile_agent_customer.setText("You are Agent");

                } else {
                    profile_agent_customer.setText("");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });
    }

    private void initilaizeUI(){
        signOut = findViewById(R.id.profile_id_sign_out);
        profile_name = findViewById(R.id.profile_id_name);
        profile_phoneNumber = findViewById(R.id.profile_id_phone);
        profile_mailId = findViewById(R.id.profile_id_email);
        profile_dob = findViewById(R.id.profile_id_dateOfBirth);
        profile_address = findViewById(R.id.profile_id_address);
        profile_agent_customer = findViewById(R.id.profile_id_agent_customer_msg);

    }

}

