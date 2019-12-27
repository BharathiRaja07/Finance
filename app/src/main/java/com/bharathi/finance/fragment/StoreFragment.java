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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bharathi.finance.R;
import com.bharathi.finance.activity.BuyOfferActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class StoreFragment extends Fragment {

    private static final String TAG = StoreFragment.class.getSimpleName();

    // url to fetch shopping items
    private static final String URL = "https://api.androidhive.info/json/movies_2017.json";

    private RecyclerView recyclerView;
    private List<Offer> itemsList;
    private StoreAdapter mAdapter;

    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
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
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        recyclerView = view.findViewById(R.id.store_recycler_view);
        itemsList = new ArrayList<>();
        mAdapter = new StoreAdapter(getActivity(), itemsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        fetchStoreItems();

        return view;
    }

    /**
     * fetching shopping item by making http call
     */
    private void fetchStoreItems() {
                        List<Offer> items = new Gson().fromJson(getResources().getString(R.string.offers), new TypeToken<List<Offer>>() {
                        }.getType());

                        itemsList.clear();
                        itemsList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();

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
    class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
        private Context context;
        private List<Offer> offerList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView offer_companyName, offer_amount,offer_principal,offer_duration,offer_interst;
            public ImageView offer_image;
            View currentView;
            Offer currentOffer;
            public MyViewHolder(View view) {
                super(view);
                offer_companyName = view.findViewById(R.id.offer_companyName);
                offer_amount = view.findViewById(R.id.offer_amount);
                offer_principal = view.findViewById(R.id.offer_principal);
                offer_interst = view.findViewById(R.id.offer_interest);
                offer_duration = view.findViewById(R.id.offer_duration);
                offer_image = view.findViewById(R.id.offer_image);
                currentView = view;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        String name = currentOffer.getCompanyName();
                        Intent intent = new Intent(getActivity(), BuyOfferActivity.class);
                        intent.putExtra("selectedOffer",currentOffer);
                        startActivity(intent);
                    }
                });

            }
        }


        public StoreAdapter(Context context, List<Offer> offerList) {
            this.context = context;
            this.offerList = offerList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_store_item_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Offer offer = offerList.get(position);
            holder.offer_companyName.setText(offer.getCompanyName());
            holder.offer_amount.setText(offer.getAmount());
            //holder.offer_principal.setText(offer.getCompanyName());
            holder.offer_principal.setText(offer.getPrincipal());
            holder.offer_interst.setText(offer.getInterest());
            holder.offer_duration.setText(offer.getDuration());

            Glide.with(context)
                    .load(R.drawable.images)
                    .into(holder.offer_image);
            holder.currentOffer = offer;
        }

        @Override
        public int getItemCount() {
            return offerList.size();
        }
    }
}
