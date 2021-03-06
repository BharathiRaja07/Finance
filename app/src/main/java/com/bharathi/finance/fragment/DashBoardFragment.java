package com.bharathi.finance.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bharathi.finance.R;
import com.bharathi.finance.activity.BuyOfferActivity;
import com.bharathi.finance.loan.Loan;
import com.bharathi.finance.user.UserProfile;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class DashBoardFragment extends Fragment {

    private static final String TAG = DashBoardFragment.class.getSimpleName();

    // url to fetch shopping items
    private static final String URL = "https://api.androidhive.info/json/movies_2017.json";

    private RecyclerView recyclerView;
    private List<Loan> loanList;
    private DashBoardFragment.DashBoardAdapter mAdapter;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    public DashBoardFragment() {
        // Required empty public constructor
    }

    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = view.findViewById(R.id.dashboard_card_view);
        loanList = new ArrayList<>();
        mAdapter = new DashBoardFragment.DashBoardAdapter(getActivity(), loanList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DashBoardFragment.GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        fetchDashBoardItems();

        return view;
    }

    /**
     * fetching shopping item by making http call
     */
    private void fetchDashBoardItems() {
        loanList.clear();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("loans").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Loan loan = dataSnapshot.getValue(Loan.class);
                loanList.add(loan);
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
    class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.MyViewHolder> {
        private Context context;
        private List<Loan> loanList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, amount,principal,duration,interest;
            public View currentView;
            public Loan currentLoan;
            public MyViewHolder(final View view) {
                super(view);
                name = view.findViewById(R.id.loan_name);
                amount = view.findViewById(R.id.loan_amount);
                principal = view.findViewById(R.id.loan_principal);
                duration = view.findViewById(R.id.loan_duration);
                interest = view.findViewById(R.id.loan_interest);

                currentView = view;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        String name = currentLoan.getName();
                        Intent intent = new Intent(getActivity(), InstallmentActivity.class);
                        intent.putExtra("selectedLoan",currentLoan);
                        startActivity(intent);

                    }
                });

            }

        }


        public DashBoardAdapter(Context context, List<Loan> loanList) {
            this.context = context;
            this.loanList = loanList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_dashboard_item_row, parent, false);

            return new DashBoardFragment.DashBoardAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DashBoardFragment.DashBoardAdapter.MyViewHolder holder, final int position) {
            final Loan loan = loanList.get(position);
            holder.name.setText(loan.getName());
            holder.amount.setText(loan.getAmount());
            holder.principal.setText(loan.getPrinciple());
            holder.duration.setText(loan.getDuration());
            holder.interest.setText(loan.getInterest());
            holder.currentLoan = loan;
        }

        @Override
        public int getItemCount() {
            return loanList.size();
        }
    }
}