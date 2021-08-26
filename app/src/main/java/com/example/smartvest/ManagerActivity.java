package com.example.smartvest;


import androidx.fragment.app.FragmentActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.smartvest.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.smartvest.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ManagerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    UserVO vo;
    List<UserVO> data;
    LatLng smart;

    //    float latitude=null;
//    double longitude=null;
//    float altitude=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        data = new ArrayList<>();

        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get()을 통해서 해당 문서의 정보를 가져온다.


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_gps")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data.add(new UserVO((double) document.getData().get("위도"), (double) document.getData().get("경도"),
                                        (double) document.getData().get("고도")));
                            }
                        } else {
                            Log.w("테스트00", "Error getting documents.", task.getException());
                        }
                        for (int i = 0; i < data.size(); i++) {
                            Log.v("수학", String.valueOf(data.get(i).getAltitude()));
                            Log.v("수학", String.valueOf(data.get(i).getLatitude()));
                            Log.v("수학", String.valueOf(data.get(i).getLongitude()));
                        }
                        for (int i = 0; i < data.size(); i++) {

                            double a = data.get(i).getLatitude();
                            double b = data.get(i).getLongitude();
                            float c = data.get(i).getAltitude();

                            smart = new LatLng(a, b);
                            Log.v("테스트", "if전");
                            if (data.get(i).getLatitude() == data.get(i).getLatitude() + i) {
                                Log.v("테스트", "if안");
                                mMap.addMarker(new MarkerOptions().position(smart).title("사용자" + i)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            } else {
                                Log.v("테스트", "else안");
                                mMap.addMarker(new MarkerOptions().position(smart).title("사용자" + i)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            }
                            Log.v("테스트", "if끝");
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(smart));
                            CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(a, b), c);
                            //newlatlngzoom(latlng, 줌배율 아하 )
                            mMap.moveCamera(cUpdate);
                        }


                    }
                });


//            LatLng sydney = new LatLng( latitude,longitude);
//            mMap.addMarker(new MarkerOptions().position(sydney).title(" 현재위치"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng( latitude,longitude),altitude);
//            mMap.setMaxZoomPreference(20);


    }

    //        // Add a marker in Sydney and move the camera
//        for( double i = 0; i < 0.005; i+=0.0005){
//            smart = new LatLng(latitude + i,longitude+i);
//            if(latitude == latitude+i){
//                mMap.addMarker(new MarkerOptions().position(smart).title("사용자" + i*2000)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//            }else{
//                mMap.addMarker(new MarkerOptions().position(smart).title("사용자" + i*2000)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//            }
//        }
//
//
//
//
//    }
//
//
    LocationManager mLocationManager;

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ManagerActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            } else {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }


        }
        return bestLocation;
    }


}
