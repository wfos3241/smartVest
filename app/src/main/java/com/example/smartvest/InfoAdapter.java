package com.example.smartvest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class InfoAdapter extends BaseAdapter {

    Context context; // 현재 페이지 정보
    int layout; // 리스트뷰에 보여질 레이아웃 정보
    List<InfoVO> data; // 레이아웃에 출력할 데이터 셋
    LayoutInflater inflater; // xml을 view객체로 변환시키는 역할
    ViewHolder viewHolder; // ID를 1회만 초기화하기 위해 ViewHolder Pattern 사용

    public InfoAdapter(Context context, int layout, List<InfoVO> data) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            convertView = inflater.inflate(layout, null);

            viewHolder = new ViewHolder();

            // xml 파일에 부여한 id들을 초기화!
            viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
            viewHolder.tv_date = convertView.findViewById(R.id.tv_date);

            // converView가 만들어져 있는 지 판단하기 위한 도구
            convertView.setTag(viewHolder);
        }

        // 만들어진 viewHolder를 가져다 사용!
        viewHolder = (ViewHolder) convertView.getTag();

        // name, tel, img를 position으로 구분하여 setting
        viewHolder.tv_title.setText(data.get(position).getTitle());
        viewHolder.tv_date.setText(data.get(position).getDate());

        return convertView;
    }
}
