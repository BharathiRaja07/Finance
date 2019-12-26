package com.bharathi.finance.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bharathi.finance.R;
import com.bharathi.finance.loan.Installment;
import com.bharathi.finance.loan.Loan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class InstallmentActivity extends AppCompatActivity {

    private static final String TAG = InstallmentActivity.class.getSimpleName();


    private RecyclerView recyclerView;
    private List<Installment> installmentList;
    private InstallmentActivity.InstallmentAdapter mAdapter;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    public InstallmentActivity() {
        // Required empty public constructor
    }

    public static InstallmentActivity newInstance(String param1, String param2) {
        InstallmentActivity fragment = new InstallmentActivity();
        Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_installment);
        recyclerView = findViewById(R.id.loan_installment_recycle_view);
        installmentList = new ArrayList<>();
        mAdapter = new InstallmentActivity.InstallmentAdapter(InstallmentActivity.this, installmentList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(InstallmentActivity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new InstallmentActivity.GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        fetchInstallmentItems();
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.loan_installment, container, false);



        return view;
    }
*/

    /**
     * fetching shopping item by making http call
     */
    private void fetchInstallmentItems() {
        installmentList.clear();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("loans").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Loan loan = dataSnapshot.getValue(Loan.class);
                installmentList.addAll(loan.getInstallmentList());
                // refreshing recycler view
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //List<Loan> items = new Gson().fromJson(getResources().getString(R.string.loans), new TypeToken<List<Loan>>() {
        //}.getType());


        //MyApplication.getInstance().addToRequestQueue(request);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    /**
     * RecyclerView adapter class to render items
     * This class can go into another separate class, but for simplicity
     */
    class InstallmentAdapter extends RecyclerView.Adapter<InstallmentAdapter.MyViewHolder> {
        private Context context;
        private List<Installment> installmentList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView dueDate, amount,principal,interest,status;
            public View currentView;
            public Installment currentInstallment;
            public MyViewHolder(final View view) {
                super(view);
                dueDate = view.findViewById(R.id.loan_installment_due_date);
                amount = view.findViewById(R.id.loan_installment_amount);
                principal = view.findViewById(R.id.loan_installment_principal);
                interest = view.findViewById(R.id.loan_installment_interest);
                status = view.findViewById(R.id.loan_installment_status);

                currentView = view;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        String name = currentInstallment.getDueDate();
                    }
                });

            }

        }


        public InstallmentAdapter(Context context, List<Installment> installmentList) {
            this.context = context;
            this.installmentList = installmentList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loan_installment_item_row, parent, false);

            return new InstallmentActivity.InstallmentAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InstallmentActivity.InstallmentAdapter.MyViewHolder holder, final int position) {
            final Installment installment = installmentList.get(position);
            holder.dueDate.setText(installment.getDueDate());
            holder.amount.setText(installment.getAmount());
            holder.principal.setText(installment.getPrincipal());
            holder.interest.setText(installment.getInterest());
            if(installment.isPaid()) {
                holder.status.setText("Paid");
            } else {
                holder.status.setText("Not Paid");
            }
            holder.currentInstallment = installment;
        }

        @Override
        public int getItemCount() {
            return installmentList.size();
        }
    }
}