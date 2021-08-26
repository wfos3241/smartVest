package com.example.smartvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Info_W_Activity extends AppCompatActivity {

    ListView i_lv_w;
    List<InfoVO> data;
    InfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_wactivity);

        i_lv_w = findViewById(R.id.i_lv_w);

        Intent intent = new Intent();
        getIntent();


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //get()을 통해서 해당 문서의 정보를 가져온다.
                db.collection("InfoWrite")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    data = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("23451", document.getData().get("제목").toString());
                                        Log.d("52353",  document.getData().get("내용").toString());
                                        data.add(new InfoVO(document.getData().get("제목").toString(),document.getData().get("내용").toString()));

                                    }

                                } else {
                                    Log.w("테스트00", "Error getting documents.", task.getException());
                                }

                                adapter = new InfoAdapter(getApplicationContext(), R.layout.infolist, data);

                                i_lv_w.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        });

    }
}