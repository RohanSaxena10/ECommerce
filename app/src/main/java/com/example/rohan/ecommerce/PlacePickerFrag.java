package com.example.rohan.ecommerce;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Rohan on 08/10/17.
 */
public class PlacePickerFrag extends Fragment {

    private static final int REQUEST_PLACE_PICKER = 1;
    int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    Button btnPickLocation;
    Button btnSkip;
    int counter = 6;

    GeoFire geoFire;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picker_fragment,null);

        final PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(BOUNDS_INDIA);

        btnPickLocation = (Button)view.findViewById(R.id.btn_picker);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofire");
        geoFire = new GeoFire(ref);

        btnPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


            }
        });

        btnSkip = (Button)view.findViewById(R.id.btn_skip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),RootActivity.class);
                intent.putExtra("Location","Delhi");
                getActivity().startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // BEGIN_INCLUDE(activity_result)
        if (requestCode == REQUEST_PLACE_PICKER) {
            // This result is from the PlacePicker dialog.


            if (resultCode == Activity.RESULT_OK) {
                counter = counter  + 1;
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, getActivity());

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */

                final String name = place.getName().toString();
                final CharSequence address = place.getAddress();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();
                String attribution = PlacePicker.getAttributions(data);




                if(attribution == null){
                    attribution = "";
                }


                // Print data to debug log
                Log.d("Place", "Place selected: " + placeId + " (" + name.toString() + ")");
                Toast.makeText(getActivity(),"Place selected: " + place.getName() ,Toast.LENGTH_LONG).show();

              geoFire.setLocation(String.valueOf(counter),new GeoLocation(place.getLatLng().latitude,place.getLatLng().longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Toast.makeText(getActivity(),"location saved to server",Toast.LENGTH_LONG).show();
                    }
                });

                /*Intent intent = new Intent(getActivity(),RootActivity.class);
                intent.putExtra("Location",name);
                intent.putExtra("Latitude",place.getLatLng().latitude);
                intent.putExtra("Longitude",place.getLatLng().longitude);
                getActivity().startActivity(intent);
*/


            } else {
                // User has not selected a place, hide the card.
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // END_INCLUDE(activity_result)
    }


}
