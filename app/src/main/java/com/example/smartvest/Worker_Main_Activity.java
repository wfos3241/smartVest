package com.example.smartvest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Worker_Main_Activity extends AppCompatActivity implements CircleProgressBar.ProgressFormatter {

    private static final String DEFAULT_PATTERN = "%d%%";

    TextView tv_worker_id, tv_weather1,tv_co,tv_ch4,tv_lpg;
    ImageButton btn_qr,btn_worker_notice;
    ImageView img_weather1;
    int co=0;
    int ch4=0;
    int lpg=0;
    List<WeatherVO> Data;
    RequestQueue requestQueue;
    InfoAdapter adapter;
    ListView i_lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_main);
        tv_worker_id=findViewById(R.id.tv_worker_id);
        tv_weather1=findViewById(R.id.tv_weather1);
        tv_co=findViewById(R.id.tv_co);
        tv_ch4=findViewById(R.id.tv_ch4);
        tv_lpg=findViewById(R.id.tv_lpg);
        btn_qr=findViewById(R.id.btn_qr);
        btn_worker_notice=findViewById(R.id.btn_worker_notice);
        i_lv = findViewById(R.id.i_lv);
        img_weather1 = findViewById(R.id.img_weather1);

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
                                    tv_weather1.setText("현재 날씨 : 맑음 \n");
                                    img_weather1.setImageResource(R.drawable.sunny);
                                } else if(Data.get(i).getTweather().equals("Clouds")) {
                                    tv_weather1.setText("현재 날씨 : 흐림 \n");
                                    img_weather1.setImageResource(R.drawable.cloud);
                                } else if(Data.get(i).getTweather().equals("Rain")) {
                                    tv_weather1.setText("현재 날씨 : 비 \n");
                                    img_weather1.setImageResource(R.drawable.rain);
                                } else if(Data.get(i).getTweather().equals("Snow")) {
                                    tv_weather1.setText("현재 날씨 : 눈 \n");
                                    img_weather1.setImageResource(R.drawable.snow);
                                }
                                tv_weather1.append("현재 온도 : " + Data.get(i).getTempc() + "\n");
                                if(Data.get(i).getTempc() >= 33) {
                                    if(Data.get(i).getTempc() < 35) {
                                        tv_weather1.append("폭염주의보입니다!! 물을 자주 마셔주세요! \n");
                                    }
                                    else if(Data.get(i).getTempc() >= 35) {
                                        tv_weather1.append("폭염경보입니다!! 열사병에 주의해주세요! \n");
                                    }
                                }else if(Data.get(i).getTempc() <= -12) {
                                    if(Data.get(i).getTempc() > -15) {
                                        tv_weather1.append("한파주의보입니다!! 옷차림을 두껍게해주세요! \n");
                                    } else if(Data.get(i).getTempc() <= -15) {
                                        tv_weather1.append("한파경보입니다!! 동상에 주의해주세요! \n");
                                    }
                                }
                                tv_weather1.append("현재 습도 : " + Data.get(i).getHumid());

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






        CircleProgressBar circleProgressBar = findViewById(R.id.cpb_circlebar);
        CircleProgressBar circleProgressBar2 = findViewById(R.id.cpb_circlebar2);
        CircleProgressBar circleProgressBar3 = findViewById(R.id.cpb_circlebar3);

        tv_worker_id.setText("박성민");

//        circleProgressBar.setProgress(10);  // 해당 퍼센트를 적용
//        circleProgressBar2.setProgress(50);
//        circleProgressBar3.setProgress(30);

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



        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = (String) tv_worker_id.getText();
                Intent intent =getIntent();
                String id = intent.getStringExtra("id");
                String pw = intent.getStringExtra("pw");
                Log.v("아이디넘어옴",id);

                Intent intent1 = new Intent(Worker_Main_Activity.this,QRScan.class);
                intent1.putExtra("id",id);
                intent1.putExtra("pw",pw);
                intent1.putExtra("name",name);
                Log.v("들어와라",id);
                startActivity(intent1);

            }
        });
        btn_worker_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                //get()을 통해서 해당 문서의 정보를 가져온다.
//                db.collection("InfoWrite")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                                if (task.isSuccessful()) {
//                                    data = new ArrayList<>();
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Log.d("23451", document.getData().get("제목").toString());
//                                        Log.d("52353",  document.getData().get("내용").toString());
//                                        data.add(new InfoVO(document.getData().get("제목").toString(),document.getData().get("내용").toString()));
//
//                                    }
//
//                                } else {
//                                    Log.w("테스트00", "Error getting documents.", task.getException());
//                                }
//
//                                adapter = new InfoAdapter(getApplicationContext(), R.layout.infolist, data);
//
//                                i_lv.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//
//                            }
//                        });

                Intent intent = new Intent(getApplicationContext(),Info_W_Activity.class);
                startActivity(intent);
            }
        });


    }// onCreate()..



    @Override
    public CharSequence format(int progress, int max) {
        return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
    }

}// MainActivity Class..