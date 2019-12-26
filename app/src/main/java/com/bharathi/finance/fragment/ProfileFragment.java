package com.bharathi.finance.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bharathi.finance.R;
import com.bharathi.finance.activity.MainActivity;
import com.bharathi.finance.activity.ProfileActivity;
import com.bharathi.finance.user.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    Button signOut;
    TextView profile_name,profile_phoneNumber,profile_mailId,profile_dob,profile_address,profile_agent_customer;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initilaizeUI(View v){
        signOut = v.findViewById(R.id.profile_id_sign_out);
        profile_name = v.findViewById(R.id.profile_id_name);
        profile_phoneNumber = v.findViewById(R.id.profile_id_phone);
        profile_mailId = v.findViewById(R.id.profile_id_email);
        profile_dob = v.findViewById(R.id.profile_id_dateOfBirth);
        profile_address = v.findViewById(R.id.profile_id_address);
        profile_agent_customer = v.findViewById(R.id.profile_id_agent_customer_msg);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initilaizeUI(rootView);

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


                //Intent intent = new Intent(ProfileFragment.this,MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                //startActivity();

            }
        });



        return rootView;
    }

}
