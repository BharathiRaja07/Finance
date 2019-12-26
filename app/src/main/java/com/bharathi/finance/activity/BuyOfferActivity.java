package com.bharathi.finance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bharathi.finance.R;
import com.bharathi.finance.fragment.MainPanel;
import com.bharathi.finance.fragment.Offer;
import com.bharathi.finance.loan.Installment;
import com.bharathi.finance.loan.Loan;
import com.bharathi.finance.user.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BuyOfferActivity extends AppCompatActivity {
    Button order;
    TextView companyName,amount,principal,duration,interest,monthlyInstallement,startDate,endDate;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_offer);
        initilaizeUI();
        Offer offer = (Offer)(getIntent().getSerializableExtra("selectedOffer"));
        updateValue(offer);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser != null) {
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();
                    Loan loan = generateLoanAndInstallments();
                    databaseReference = FirebaseDatabase.getInstance().getReference("loans").child(userid);

                    databaseReference.setValue(loan).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(BuyOfferActivity.this, MainPanel.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                   return;
                }



            }
        });
    }

    private void initilaizeUI(){
        order = findViewById(R.id.buy_offer_order);
        companyName = findViewById(R.id.buy_offer_companyName);
        amount = findViewById(R.id.buy_offer_amount);
        principal = findViewById(R.id.buy_offer_principal);
        duration = findViewById(R.id.buy_offer_duration);
        interest = findViewById(R.id.buy_offer_interest);
        monthlyInstallement = findViewById(R.id.buy_offer_monthly_installement);
        startDate = findViewById(R.id.buy_offer_start_date);
        endDate = findViewById(R.id.buy_offer_end_date);

    }

    private void updateValue(Offer offer){
        companyName.setText(offer.getCompanyName());
        amount.setText(offer.getAmount());
        principal.setText(offer.getPrincipal());
        duration.setText(offer.getDuration());
        interest.setText(offer.getInterest());
        double monthlyInstallment = ((Double.parseDouble(offer.getAmount()) / ( Double.parseDouble(offer.getInterest()) * 100))) + Double.parseDouble(offer.getPrincipal());
        monthlyInstallement.setText(String.valueOf(monthlyInstallment));

        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar startDateCalendar = Calendar.getInstance();
        int monthForStartDate = startDateCalendar.get(Calendar.MONTH);
        startDateCalendar.set(Calendar.MONTH, + monthForStartDate + 1);
        startDateCalendar.set(Calendar.DAY_OF_MONTH,6);
        Calendar endDateCalendar = Calendar.getInstance();
        for(int i = 1;i <= Integer.parseInt(duration.getText().toString());i++){
            int month = endDateCalendar.get(Calendar.MONTH);
            endDateCalendar.set(Calendar.MONTH, + month + 1);
            endDateCalendar.set(Calendar.DAY_OF_MONTH,6);
        }
        startDate.setText(formatter.format(startDateCalendar.getTime()));
        endDate.setText(formatter.format(endDateCalendar.getTime()));
    }

    private Loan generateLoanAndInstallments(){
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        Loan loan = new Loan();
        loan.setAmount(amount.getText().toString());
        loan.setDuration(duration.getText().toString());
        loan.setName(companyName.getText().toString());
        loan.setInterest(interest.getText().toString());
        loan.setPrinciple(principal.getText().toString());
        List<Installment> installments = new ArrayList<Installment>();
        Calendar calendar = Calendar.getInstance();
        for(int i = 1;i <= Integer.parseInt(duration.getText().toString());i++){
            Installment installment = new Installment();
            installment.setAmount(monthlyInstallement.getText().toString());
            int month = calendar.get(Calendar.MONTH);
            calendar.set(Calendar.MONTH, + month + 1);
            calendar.set(Calendar.DAY_OF_MONTH,6);
            installment.setDueDate(formatter.format(calendar.getTime()));
            installment.setInterest( String.valueOf((Double.parseDouble(amount.getText().toString())) / ( Double.parseDouble(interest.getText().toString()) * 100)));
            installment.setPrincipal(principal.getText().toString());
            installment.setPaid(false);
            installments.add(installment);


        }
        loan.setInstallmentList(installments);
        return loan;
    }

}
