package com.example.zxcbn.qrpay;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zxcbn on 2018-09-22.
 */

public class OrderAdapter extends BaseAdapter {
    Context context;
    ArrayList<Order> arrayOrder;

    public OrderAdapter(Context context, ArrayList<Order> arrayOrder) {
        this.context = context;
        this.arrayOrder = arrayOrder;
    }

    @Override
    public int getCount() {
        return arrayOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayOrder.get(position).oid;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = View.inflate(context,R.layout.custom,null);
        }
        TextView user = (TextView)convertView.findViewById(R.id.user);
        TextView menulist = (TextView)convertView.findViewById(R.id.menulist);
        TextView oid = (TextView)convertView.findViewById(R.id.oid);
        String name = arrayOrder.get(position).name;
        String menu = arrayOrder.get(position).menu;
        String cnt = arrayOrder.get(position).count;
        user.setText(name);
        menulist.setText(menu + "  " + cnt +"개");
        oid.setText(String.valueOf(position));
        return convertView;
    }

    public void removeItem(int position){
        if(getCount()!=0)
        arrayOrder.remove(position);
    }
}
