package com.example.rohan.ecommerce;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.helpers.Searcher;
import com.bumptech.glide.Glide;
import com.example.rohan.ecommerce.POJOS.RelevantProducts;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by Rohan on 09/10/17.
 */
public class MainFragment extends Fragment {

    AutoScrollViewPager autoScrollViewPager;
    RecyclerView rvRelevant;
    DatabaseReference geoRef;
    GeoFire geoFire;
    GeoQuery geoQuery;

    ImageButton img1;
    ImageButton img2;
    ImageButton img3;


    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());

        return drawableResourceId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);

        autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.autoscroll_vp);
        rvRelevant = (RecyclerView)view.findViewById(R.id.rv_relevant);
       // rvRelevant.setAdapter(new RVRelevantAdapter());
        rvRelevant.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        geoRef = FirebaseDatabase.getInstance().getReference("geofire");
        geoFire = new GeoFire(geoRef);

        img1 = (ImageButton)view.findViewById(R.id.imgcategory1);
        img2 = (ImageButton)view.findViewById(R.id.imgcategory2);
        img3 = (ImageButton)view.findViewById(R.id.imgcategory3);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
            }
        });


        Glide.with(getActivity()).
                load(getImage("handicraft"))
                .into(img1);
        Glide.with(getActivity()).load(getImage("cultivation")).into(img2);
        Glide.with(getActivity()).load(getImage("lively")).into(img3);

        autoScrollViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                if(position == 0) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageResource(R.drawable.dsc);
                    container.addView(imageView);
                    return imageView;
                }
                else
                {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageResource(R.drawable.lively);
                    container.addView(imageView);
                    return imageView;
                }
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == (ImageView) object;
            }
        });

        autoScrollViewPager.startAutoScroll();
        autoScrollViewPager.setScrollDurationFactor(5);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setInterval(5000);



        if(getArguments() != null)
        {
            Log.d("Arguments not null in","Main Fragment");
            Double latitude = getArguments().getDouble("Latitude");
            Double longitude = getArguments().getDouble("Longitude");
            Log.d("query latitude",String.valueOf(latitude));
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 100);
            rvRelevant.setAdapter(new RVRelevantAdapter(getActivity(),latitude, longitude,geoQuery));

        }

        return view;
    }


    public class RVRelevantAdapter extends RecyclerView.Adapter<RVRelevantAdapter.ViewHolder> {

        Context context;

        GeoQuery geoQuery;
        ArrayList<String> uidList;
        ArrayList<RelevantProducts> pojoList;

        public RVRelevantAdapter(Context context, Double latitude, Double longitude, final GeoQuery geoQuery) {
            this.context = context;
            this.geoQuery = geoQuery;
            uidList = new ArrayList<>();
            pojoList = new ArrayList<>();
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, GeoLocation location) {
                    Log.d("Location found",key + " " +  location.latitude);

                    FirebaseDatabase.getInstance().getReference().child("Products").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            RelevantProducts relevantProduct = dataSnapshot.getValue(RelevantProducts.class);
                            pojoList.add(relevantProduct);
                            Log.d("Pojo ADD",relevantProduct.getName());
                            uidList.add(key);
                            notifyItemInserted(pojoList.size() - 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });
        }

        @Override
        public RVRelevantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_relevant, parent, false);
            return new ViewHolder(view);
        }



        @Override
        public void onBindViewHolder(RVRelevantAdapter.ViewHolder holder, final int position) {
            holder.tvName.setText(pojoList.get(position).getName());
            holder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductPageFragment productPageFragment = new ProductPageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Key",uidList.get(position));
                    productPageFragment.setArguments(bundle);
                    ((Tab1ContainerFragment)getParentFragment()).replaceFragment(productPageFragment,true);
                }
            });
            Glide.with(getActivity())
                    .load(pojoList.get(position).getDpurl())
                    .into(holder.img);
            holder.tvPrice.setText(String.valueOf(pojoList.get(position).getPrice()));
            holder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Tab1ContainerFragment)getParentFragment()).replaceFragment(new ProductPageFragment(),true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pojoList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }

            TextView tvName = (TextView)itemView.findViewById(R.id.product_name);
            ImageView img = (ImageView)itemView.findViewById(R.id.product_image);
            TextView tvPrice = (TextView)itemView.findViewById(R.id.product_price);

        }
    }



}
