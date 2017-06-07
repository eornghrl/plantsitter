package com.example.zzamtiger.textview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by 성호기 on 2017-05-31.
 */




public class MyListAdapter extends BaseAdapter{
    Context context;
    ArrayList<list_item> list_itemArrayList;
    ViewHolder viewholder;

    class ViewHolder{
        TextView nickname_textView;
        TextView title_textView;
        TextView date_textView;
        TextView content_textView;
        ImageView profile_imageView;
    }

    public MyListAdapter(Context context, ArrayList<list_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }
    @Override
    public int getCount() {
        return this.list_itemArrayList.size();          //예제는 return이 없었음
    }

    @Override
    public Object getItem(int position) {
        return this.list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
            viewholder = new ViewHolder();
            viewholder.nickname_textView = (TextView)convertView.findViewById(R.id.nickname_textview);
            viewholder.content_textView = (TextView)convertView.findViewById(R.id.content_texview);
            viewholder.date_textView = (TextView)convertView.findViewById(R.id.date_textview);
            viewholder.title_textView  =(TextView)convertView.findViewById(R.id.title_textview);
            viewholder.profile_imageView = (ImageView)convertView.findViewById(R.id.profile_imageView);
            convertView.setTag(viewholder);
        }else{
            viewholder = (ViewHolder)convertView.getTag();
        }

        viewholder.nickname_textView.setText(list_itemArrayList.get(position).getNickname());
        viewholder.title_textView.setText(list_itemArrayList.get(position).getTitle());
        viewholder.content_textView.setText(list_itemArrayList.get(position).getContent());
        viewholder.date_textView.setText(list_itemArrayList.get(position).getWrite_date().toString());
        Glide.with(context).load(list_itemArrayList.get(position).getProfile_image()).into(viewholder.profile_imageView);
        return convertView;
    }


}
