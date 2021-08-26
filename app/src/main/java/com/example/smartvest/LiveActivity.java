package com.example.smartvest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;



import com.dinuscxj.progressbar.CircleProgressBar;
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

public class LiveActivity extends AppCompatActivity implements CircleProgressBar.ProgressFormatter {

    private static final String DEFAULT_PATTERN = "%d%%";
    int co=0;
    int ch4=0;
    int lpg=0;

    ImageButton btn_w_gps,btn_w_cam;

    String db_url;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String provider;
    float latitude ;
    double longitude ;
    float altitude ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        CircleProgressBar circleProgressBar = findViewById(R.id.cpb_circlebar);
        CircleProgressBar circleProgressBar2 = findViewById(R.id.cpb_circlebar2);
        CircleProgressBar circleProgressBar3 = findViewById(R.id.cpb_circlebar3);
        btn_w_gps = findViewById(R.id.btn_w_gps);
        btn_w_cam = findViewById(R.id.btn_w_cam);

        btn_w_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ManagerActivity.class);
//                db_url="https://smartvest-2881b-default-rtdb.firebaseio.com/";
//                database = FirebaseDatabase.getInstance(db_url);
//                myRef = database.getReference("user");
//
//                myRef.child("latitude").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        latitude =dataSnapshot.getValue(float.class);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError Error) {
//
//                    }
//                });
//                myRef.child("longitude").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        longitude =dataSnapshot.getValue(Double.class);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//                myRef.child("altitude").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        altitude =dataSnapshot.getValue(float.class);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                myRef.child("provider").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        provider =dataSnapshot.getValue(String.class);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//                intent.putExtra("provider",provider);
//                intent.putExtra("latitude",latitude);
//                intent.putExtra("longitude",longitude);
//                intent.putExtra("altitude",altitude);
                startActivity(intent);

            }


        });

        btn_w_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                startActivity(intent);
            }
        });




        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("vest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("일산화탄소", document.getId() + " => " + document.getData().get("sensor2").toString());
                        //       Log.d("메탄", document.getId() + " => " + document.getData());
                        co = Integer.parseInt(document. getData().get("co").toString());
                        ch4= Integer.parseInt(document. getData().get("ch4").toString());
                        lpg= Integer.parseInt(document. getData().get("lpg").toString());
                        break;
                    }

                } else {
                    Log.w("테스트3", "Error getting documents.", task.getException());
                }
                circleProgressBar.setProgress(co);  // 해당 퍼센트를 적용
                circleProgressBar2.setProgress(ch4);  // 해당 퍼센트를 적용
                circleProgressBar3.setProgress(lpg);  // 해당 퍼센트를 적용

            }
        });




    }// onCreate()..


    @Override
    public CharSequence format(int progress, int max) {
        return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
    }

}// MainActivity Class..