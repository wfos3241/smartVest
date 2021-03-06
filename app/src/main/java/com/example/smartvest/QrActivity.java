package com.example.smartvest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QrActivity extends AppCompatActivity {

    ListView lv_qr;
    List<QrVO> data;
    Button btn_write;
    QrAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        lv_qr = findViewById(R.id.lv_qr);
        btn_write = findViewById(R.id.btn_write);

        data = new ArrayList<>();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String id_m = intent.getStringExtra("id_m");

// Write a message to the database
//        String qrurl = "https://vest-11534-default-rtdb.firebaseio.com/";
//        String qrurl1 = "https://smartvest-2881b-default-rtdb.firebaseio.com/";
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance(qrurl1);
//        DatabaseReference myRef = database.getReference("message");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(id_m.equals("smartvest")){
            db.collection("qr")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                data = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    data.add(new QrVO(String.valueOf(document.getData().get("??????")),String.valueOf(document.getData().get("????????????")),
                                            String.valueOf(document.getData().get("????????????")),String.valueOf(document.getData().get("????????????")),
                                            String.valueOf(document.getData().get("??????"))));

//                                    data.add(new QrVO(document.getData().get("??????").toString(),document.getData().get("????????????").toString(),
//                                            document.getData().get("????????????").toString(),document.getData().get("????????????").toString(),
//                                            document.getData().get("??????").toString()));

                                }

                            } else {
//                                Log.w("?????????00", "Error getting documents.", task.getException());
                            }

                            adapter = new QrAdapter(getApplicationContext(), R.layout.qrlist, data);

                            lv_qr.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    });
        }else {
//

            DocumentReference docRef = db.collection("qr").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        data = new ArrayList<>();
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("??????", "DocumentSnapshot data: " + document.getData());

                            data.add(new QrVO(document.getData().get("??????").toString(), document.getData().get("????????????").toString(),
                                    document.getData().get("????????????").toString(), document.getData().get("????????????").toString(),
                                    document.getData().get("??????").toString()));
                            //data.add(new QrVO(document.getData().get("??????").toString(),document.getData().get("??????").toString()));


                        } else {
                            Log.d("??????", "No such document");
                        }
                    } else {
                        Log.d("?????????", "get failed with ", task.getException());
                    }

                    adapter = new QrAdapter(getApplicationContext(), R.layout.qrlist, data);

                    lv_qr.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            });
        }

//        db.collection("qr").document(id)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            data = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("23451", document.getData().get("??????").toString());
//                                Log.d("52353",  document.getData().get("??????").toString());
////                                data.add(new InfoVO(document.getData().get("??????").toString(),document.getData().get("??????").toString()));
//
//                            }
//
//                        } else {
//                            Log.w("?????????00", "Error getting documents.", task.getException());
//                        }
//
//                        adapter = new QrAdapter(getApplicationContext(), R.layout.qrlist, data);
//
//                        lv_qr.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//
//                    }
//                });

//        //myRef.setValue("Hello, World!");
//        // String msg = etMsg.getText().toString();
//        data.add(new QrVO("????????????", "?????????",
//                "????????????",
//                "??????",
//                "??????"));
//        myRef.push().setValue(data);


        /*for(int i = 0; i < 10; i++) {
            data.add(new QrVO("????????????", "?????????"+i,
                    "????????????",
                    "??????",
                    "??????"));
        }*/

        QrAdapter adapter = new QrAdapter(getApplicationContext(),
                R.layout.qrlist,data);
        lv_qr.setAdapter(adapter);
    }
}
