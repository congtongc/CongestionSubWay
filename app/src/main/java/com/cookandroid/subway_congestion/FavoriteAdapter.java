package com.cookandroid.subway_congestion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FavoriteAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> favoriteList;

    public FavoriteAdapter(Context context, ArrayList<String> favoriteList){
        super(context, 0, favoriteList);
        this.context = context;
        this.favoriteList = favoriteList;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.favorite_listview, parent, false);
        }

        String favorite = favoriteList.get(position);

        TextView favoriteTextView = convertView.findViewById(R.id.favoriteTextView);
        favoriteTextView.setText(favorite);

        return convertView;
    }
}
