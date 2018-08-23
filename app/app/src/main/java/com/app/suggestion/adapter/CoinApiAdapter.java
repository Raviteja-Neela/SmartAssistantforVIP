package com.app.suggestion.adapter;


import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.suggestion.R;
import com.app.suggestion.model.CoinData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinApiAdapter extends ArrayAdapter<CoinData> {

    Activity context;
    int resource, textViewResourceId;
    List<CoinData> items, tempItems, suggestions;
    private Dialog pinDialog;
    private EditText popup_title, popup_review;
    private TextView submit;
    private RatingBar rating;


    public CoinApiAdapter(Activity mainActivity, List<CoinData> dataArrayList) {
        super(mainActivity, 0, dataArrayList);

        this.context = mainActivity;
        this.items = dataArrayList;
    }


    private class ViewHolder {

        TextView currency, price, time;
        ImageView image;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CoinApiAdapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(
                    R.layout.recycler, parent, false);

            holder = new CoinApiAdapter.ViewHolder();
            holder.currency = (TextView) convertView.findViewById(R.id.currency);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.time = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(holder);

        } else {
            holder = (CoinApiAdapter.ViewHolder) convertView.getTag();
        }

        CoinData productItems = items.get(position);


        holder.currency.setText(productItems.getCurrency());
        holder.price.setText(productItems.getPrice());
        holder.time.setText(productItems.getTime());


        return convertView;

    }


}