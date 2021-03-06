package com.example.smartvest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class QRScan extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    private Button btn_qrscan_inout,btn_qrscan_safe;
    private ImageButton btn_check , btn_isu;
    private TextView tv_check1,tv_check2,tv_check3,tv_check4;
    //qr code scanner object
    private IntentIntegrator qrScan;
    String a ="a";
    String id = null;
    String pw = null;
    String provider;
    float latitude ;
    double longitude ;
    float altitude ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        //View Objects
        btn_qrscan_inout = findViewById(R.id.btn_qrscan_inout);
        btn_qrscan_safe = findViewById(R.id.btn_qrscan_safe);
        tv_check1 = findViewById(R.id.tv_check1);
        tv_check2 = findViewById(R.id.tv_check2);
        tv_check3 = findViewById(R.id.tv_check3);
        tv_check4 = findViewById(R.id.tv_check4);
        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.setPrompt("QR CODE  ?????????!!");
        qrScan.setOrientationLocked(false);
        qrScan.initiateScan();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        pw = intent.getStringExtra("pw");
        String name = intent.getStringExtra("name");

        tv_check1.setVisibility(View.INVISIBLE);
        tv_check2.setVisibility(View.INVISIBLE);
        tv_check3.setVisibility(View.INVISIBLE);
        tv_check4.setVisibility(View.INVISIBLE);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permssionCheck != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "?????? ????????? ???????????????", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this, "000?????? ????????? ?????? ????????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                Toast.makeText(this, "000?????? ????????? ?????? ????????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
            }
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //button onClick
        btn_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String check1 = tv_check1.getText().toString();
                String check2 = tv_check2.getText().toString();
                String check3 = tv_check3.getText().toString();
                String check4 = tv_check4.getText().toString();
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(QRScan.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                } else {

                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    provider = location.getProvider();
                    latitude = (float) location.getLatitude();
                    longitude = location.getLongitude();
                    altitude = (float) location.getAltitude();


                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);


                    //hashmap ?????????
                    HashMap result = new HashMap<>();
                    result.put("??????", name);
                    result.put("????????????", check1);
                    result.put("????????????", check2);
                    result.put("????????????", check3);
                    result.put("??????", check4);
                    HashMap gps = new HashMap<>();
                    gps.put("??????", latitude);
                    gps.put("??????", longitude);
                    gps.put("??????", altitude);


                    db.collection("qr").document(id).set(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(QRScan.this, "????????????", Toast.LENGTH_SHORT).show();
//                        Intent intent2 = new Intent(getApplicationContext(), QrActivity.class);
//                        intent2.putExtra("id", id);
//                        startActivity(intent2);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QRScan.this, "????????????", Toast.LENGTH_SHORT).show();

                        }
                    });

                    db.collection("user_gps").document(id).set(gps).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(QRScan.this, "????????????", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(getApplicationContext(), QrActivity.class);
                            intent2.putExtra("id", id);
                            startActivity(intent2);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QRScan.this, "????????????", Toast.LENGTH_SHORT).show();

                        }
                    });


//                Location location = getLastKnownLocation();
//
//                String provider = location.getProvider();
//                latitude = (float) location.getLatitude();
//                longitude = location.getLongitude();
//                altitude = (float) location.getAltitude();
//                Log.v("latitude", String.valueOf(latitude));
//                Log.v("longitude", String.valueOf(longitude));
//                Log.v("altitude", String.valueOf(altitude));

//                UserVO vo = new UserVO( longitude,latitude,altitude);

//                HashMap gps = new HashMap<>();
//                gps.put("??????", latitude);
//                gps.put("??????",longitude);
//                gps.put("??????", altitude);


//                    txtResult.setText("???????????? : " + provider + "\n" +
//                            "?????? : " + longitude + "\n" +
//                            "?????? : " + latitude + "\n" +
//                            "??????  : " + altitude);

//                                                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                                                            1000,
//                                                            1,
//                                                            gpsLocationListener);
//                                                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                                                            1000,
//                                                            1,
//                                                            gpsLocationListener);
                }
            }
        });


        btn_isu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String check="??????" ;
                //hashmap ?????????
                HashMap result = new HashMap<>();
                result.put("????????????",check);

                db.collection("qr").document("safety").set(result, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
                Toast.makeText(QRScan.this, "????????????", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QRScan.this,Worker_Main_Activity.class);
                startActivity(intent);

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode ??? ?????????
            if (result.getContents() == null) {
                Toast.makeText(QRScan.this, "??????!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode ????????? ?????????
                Toast.makeText(QRScan.this, "????????????!", Toast.LENGTH_SHORT).show();
                try {
                    //data??? json?????? ??????
                    JSONObject obj = new JSONObject(result.getContents());
                    // tv_check.setText(result.getContents());
                    tv_check1.setText(obj.getString("????????????"));
                    tv_check2.setText(obj.getString("????????????"));
                    tv_check3.setText(obj.getString("????????????"));
                    tv_check4.setText(obj.getString("??????"));

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();

//            txtResult.setText("???????????? : " + provider + "\n" +
//                    "?????? : " + longitude + "\n" +
//                    "?????? : " + latitude + "\n" +
//                    "?????? : " + altitude
//            );
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }

    };


    LocationManager mLocationManager;

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(QRScan.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }else{
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