package com.example.hci_project.dummy;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FileAdapter extends BaseAdapter {
    ArrayList<String> array;
    Context mContext;

    public FileAdapter(ArrayList<String> fileArray, Context mContext) {
        this.array = fileArray;
        this.mContext = mContext;
    }



    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            TextView textview = new TextView(mContext);
            textview.setTextColor(Color.rgb(0,0,0));
            textview.setTextSize(20);
            textview.setText(array.get(position));
            convertView = textview;

        }



        return convertView;
    }
}
