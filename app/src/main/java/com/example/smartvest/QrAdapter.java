package com.example.smartvest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class QrAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<QrVO> data;
    LayoutInflater inflater;
    ViewHolder viewHolder;

    public QrAdapter(Context context, int layout, List<QrVO> data) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // converView가 안 만들어져 있다면,
            convertView = inflater.inflate(layout, null);

        }
        viewHolder = new ViewHolder();

        // xml파일에 부여한 id를 초기화!
        viewHolder.tv_company = convertView.findViewById(R.id.tv_company);
        viewHolder.tv_w_name = convertView.findViewById(R.id.tv_w_name);
        viewHolder.tv_work = convertView.findViewById(R.id.tv_work);
        viewHolder.tv_edu = convertView.findViewById(R.id.tv_edu);
        viewHolder.tv_in_out = convertView.findViewById(R.id.tv_in_out);
        viewHolder.btn_live = convertView.findViewById(R.id.btn_live);


        // convertView가 만들어져 있는지 판단하기 위한 도구
        convertView.setTag(viewHolder);

        viewHolder = (ViewHolder) convertView.getTag();

        // 가져온거 직접 데이터 사이즈만큼 업데이트 해주기
        viewHolder.tv_company.setText(data.get(position).getTv_company());
        viewHolder.tv_w_name.setText(data.get(position).getTv_w_name());
        viewHolder.tv_work.setText(data.get(position).getTv_work());
        viewHolder.tv_edu.setText(data.get(position).getTv_edu());
        viewHolder.tv_in_out.setText(data.get(position).getTv_in_out());

        viewHolder.btn_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String str = data.get(position).getUrl();
//                Uri url = Uri.parse(str);

                Intent intent = new Intent(context,LiveActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);





            }
        });




        return convertView;


    }
}
