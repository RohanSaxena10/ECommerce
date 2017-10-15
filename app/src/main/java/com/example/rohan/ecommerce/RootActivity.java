package com.example.rohan.ecommerce;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


/**
 * Created by Rohan on 09/10/17.
 */
public class RootActivity extends Activity{


    TextView tv_location;
    Toolbar toolbar;
    Tab1ContainerFragment tab1ContainerFragment;
    ImageButton btnSearch;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        context = this;

        tv_location = (TextView)findViewById(R.id.tv_location);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        btnSearch = (ImageButton)findViewById(R.id.btnsearch);
        tab1ContainerFragment = new Tab1ContainerFragment();


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SearchActivity.class);
                startActivity(intent);
            }
        });


        if(getIntent().getExtras().getString("Location")!= null)
        {
            Log.d("Arguments not null","Main Activity");

            Log.d("Latitude",String.valueOf(getIntent().getExtras().getDouble("Latitude")));
            tv_location.setText(getIntent().getExtras().getString("Location"));
            Bundle bundle = new Bundle();
            bundle.putDouble("Latitude",getIntent().getExtras().getDouble("Latitude"));
            bundle.putDouble("Longitude",getIntent().getExtras().getDouble("Longitude"));
            tab1ContainerFragment.setArguments(bundle);
        }


//        tv_location.setText(getIntent().getExtras().getString("Location"));

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Rohan Saxena").withEmail("rohanstark10@gmail.com")
                                .withIcon(getResources().getDrawable(R.drawable.rdj))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Categories").withSubItems(new SecondaryDrawerItem().withName("HandiCrafts").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                /*etFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left, // Contains animation
                                        R.animator.slide_out_right, R.animator.slide_in_right).replace(R.id.container_fl,new ListDisplayFragment()).addToBackStack(null)
                                        .commit();
                                getFragmentManager().executePendingTransactions();*/
                                Intent intent = new Intent(context,SearchActivity.class);
                                startActivity(intent);

                                return true;
                            }
                        })).withSubItems(new SecondaryDrawerItem().withName("Agriculture").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                /*etFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left, // Contains animation
                                        R.animator.slide_out_right, R.animator.slide_in_right).replace(R.id.container_fl,new ListDisplayFragment()).addToBackStack(null)
                                        .commit();
                                getFragmentManager().executePendingTransactions();*/
                                Intent intent = new Intent(context,SearchActivity.class);
                                startActivity(intent);

                                return true;
                            }
                        })).withSubItems(new SecondaryDrawerItem().withName("Manufactured").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                /*etFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left, // Contains animation
                                        R.animator.slide_out_right, R.animator.slide_in_right).replace(R.id.container_fl,new ListDisplayFragment()).addToBackStack(null)
                                        .commit();
                                getFragmentManager().executePendingTransactions();*/
                                Intent intent = new Intent(context,SearchActivity.class);
                                startActivity(intent);

                                return true;
                            }
                        })),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                )
                .addStickyDrawerItems(new PrimaryDrawerItem().withName("Footer").withDescription("This is footer"))
                .build();


        getFragmentManager().beginTransaction().replace(R.id.container_fl,tab1ContainerFragment).commit();
        getFragmentManager().executePendingTransactions();

    }

    @Override
    public void onBackPressed() {
        Boolean isPopFragment = false;

        isPopFragment = tab1ContainerFragment.popFragment();

        if (!isPopFragment) {
            super.onBackPressed();
        }
    }


}



