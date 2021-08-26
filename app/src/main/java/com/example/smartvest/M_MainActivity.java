package com.example.smartvest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class M_MainActivity extends AppCompatActivity {
    ImageButton btn_qr, btn_info, btn_gps,btn_setting;
    TextView tv_m_weather;
    ImageView img_m_weather;
    RequestQueue requestQueue;
    List<WeatherVO> Data;
    String provider;
    float latitude ;
    double longitude ;
    float altitude ;


    String db_url;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_main);

        tv_m_weather=findViewById(R.id.tv_m_weather);
        img_m_weather=findViewById(R.id.img_m_weather);

        btn_qr = findViewById(R.id.btn_qr);
        btn_gps = findViewById(R.id.btn_gps);
        btn_info = findViewById(R.id.btn_info);
        tv_m_weather=findViewById(R.id.tv_m_weather);
        btn_setting = findViewById(R.id.btn_setting);


        long mNow = System.currentTimeMillis();
        Date mReDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        String date = mFormat.format(mReDate);
        String[] datas = date.split(" ");
        String day = datas[0];
        String time = datas[1];
        // 샘플데이터 (온도 - 폭염, 한파 | 날씨 - 맑음, 흐림, 비, 눈)
        int hot = 33;
        int tohot = 35;
        int cold = -12;
        int tocold = -15;
        String sun = "Clear";
        String cloud = "Clouds";
        String rain = "Rain";
        String snow = "Snow";
//        tv_rain = findViewById(R.id.tv_rain);
//        tv_date = findViewById(R.id.tv_date);
        Data = new ArrayList<>();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = "https://api.openweathermap.org/data/2.5/forecast?id=1841811&appid=4efdbc960047583b68aa9e101d58786a";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("weather", response);


                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray list = json.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject lists = list.getJSONObject(i);
                        JSONObject main = lists.getJSONObject("main");
                        JSONArray main2 = lists.getJSONArray("weather");
                        JSONObject weather = main2.getJSONObject(0);

                        String tweather = weather.optString("main");
                        String dt_txt = lists.optString("dt_txt");
                        double humid = main.optDouble("humidity");
                        double temp = main.optDouble("temp");
                        double tempc = Math.floor(temp - 273.15);


                        WeatherVO vo = new WeatherVO(tempc, dt_txt, tweather, humid);
                        Data.add(vo);
                    }


                    for(int i = Data.size()-1; i>=0; i--){
                        if (Data.get(i).getTime().startsWith(datas[0])) {
                            String temp = Data.get(i).getTime().split(" ")[1].split(":")[0];

                            if (Integer.parseInt(temp) <= Integer.parseInt(hour.format(mReDate))){
                                if(Data.get(i).getTweather().equals("Clear")) {
                                    tv_m_weather.setText("현재 날씨 : 맑음 \n");
                                    img_m_weather.setImageResource(R.drawable.sunny);
                                } else if(Data.get(i).getTweather().equals("Clouds")) {
                                    tv_m_weather.setText("현재 날씨 : 흐림 \n");
                                    img_m_weather.setImageResource(R.drawable.cloud);
                                } else if(Data.get(i).getTweather().equals("Rain")) {
                                    tv_m_weather.setText("현재 날씨 : 비 \n");
                                    img_m_weather.setImageResource(R.drawable.rain);
                                } else if(Data.get(i).getTweather().equals("Snow")) {
                                    tv_m_weather.setText("현재 날씨 : 눈 \n");
                                    img_m_weather.setImageResource(R.drawable.snow);
                                }
                                tv_m_weather.append("현재 온도 : " + Data.get(i).getTempc() + "\n");
                                if(Data.get(i).getTempc() >= 33) {
                                    if(Data.get(i).getTempc() < 35) {
                                        tv_m_weather.append("폭염주의보입니다!! 물을 자주 마셔주세요! \n");
                                    }
                                    else if(Data.get(i).getTempc() >= 35) {
                                        tv_m_weather.append("폭염경보입니다!! 열사병에 주의해주세요! \n");
                                    }
                                }else if(Data.get(i).getTempc() <= -12) {
                                    if(Data.get(i).getTempc() > -15) {
                                        tv_m_weather.append("한파주의보입니다!! 옷차림을 두껍게해주세요! \n");
                                    } else if(Data.get(i).getTempc() <= -15) {
                                        tv_m_weather.append("한파경보입니다!! 동상에 주의해주세요! \n");
                                    }
                                }
                                tv_m_weather.append("현재 습도 : " + Data.get(i).getHumid());

                                break;
                            }
                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

        requestQueue.add(request);





        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =getIntent();
                String id = intent.getStringExtra("id");
                String pw = intent.getStringExtra("pw");

                Intent intent1 = new Intent(getApplicationContext(),QrActivity.class);
                intent1.putExtra("id_m",id);
                intent1.putExtra("pw_m",pw);
                startActivity(intent1);




            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),InfoActivity.class);
                startActivity(intent);

            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),M_MainActivity.class);
                startActivity(intent);
            }
        });
//

        btn_gps.setOnClickListener(new View.OnClickListener() {
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






    }
}