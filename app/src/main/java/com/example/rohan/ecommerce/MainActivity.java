package com.example.rohan.ecommerce;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;


public class MainActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener{


    GoogleApiClient googleApiClient;
    Fragment picker_frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        picker_frag = new PlacePickerFrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fl,picker_frag).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
