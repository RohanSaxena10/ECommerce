package com.example.rohan.ecommerce;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rohan.ecommerce.POJOS.RelevantProducts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Rohan on 10/10/17.
 */
public class ProductPageFragment extends Fragment {

    ImageView imgDP;
    TextView tvName;
    TextView tvSellerName;
    TextView tvdesc;
    TextView tvGST;
    TextView tvPrice;
    Button btnBuy;
    Button btnShare;
    Button btnAddtoWishList;

    TextView tvRecentlyVisited;
    RecyclerView rvRecentlyVisited;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productpage,null);


        imgDP = (ImageView)view.findViewById(R.id.imgDP);
        tvName = (TextView)view.findViewById(R.id.tv_productname);
        tvSellerName = (TextView)view.findViewById(R.id.tvSellerName);
        tvPrice = (TextView)view.findViewById(R.id.tvPrice);
        btnShare = (Button)view.findViewById(R.id.btnShare);

        FirebaseDatabase.getInstance().getReference().child("Products").child("4").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelevantProducts product = dataSnapshot.getValue(RelevantProducts.class);
                tvName.setText(product.getName());
                tvPrice.setText(String.valueOf(product.getPrice()) + " INR");
                tvSellerName.setText(product.getSellername());

                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String message = "Text I want to share.";
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, message);

                        startActivity(Intent.createChooser(share, "Share in app"));
                    }
                });

                Glide.with(getActivity())
                        .load("https://firebasestorage.googleapis.com/v0/b/ecom-f440e.appspot.com/o/4.jpg?alt=media&token=009e0d73-d20f-4a62-91b0-265b05900c3e")
                        .into(imgDP);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;


    }
}
