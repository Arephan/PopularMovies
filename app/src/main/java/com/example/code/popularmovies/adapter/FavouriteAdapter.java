package com.example.code.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.code.popularmovies.R;
import com.example.code.popularmovies.models.Favourite;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by code on 2016-04-14.
 */
public class FavouriteAdapter extends BaseAdapter {
    public ArrayList<Favourite> favouritesArr;
    private Context context;
    private LayoutInflater inflater;
    private String apiKeyUrl = "?api_key=783ba962d1e5f0bb1f38081b34431edd";
    private String baseUrl = "http://image.tmdb.org/t/p/w185";

    public FavouriteAdapter(Context context, ArrayList<Favourite> favourites) {
        this.context = context;
        this.favouritesArr = favourites;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return favouritesArr.size();
    }

    @Override
    public Favourite getItem(int position) {
        return favouritesArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return favouritesArr.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_layout, null);
        } else {
            ImageView posterImageView = (ImageView) convertView.findViewById(R.id.posterGridView);
            Picasso.with(context)
                    .load(baseUrl + favouritesArr.get(position).posterPath + apiKeyUrl)
                    .error(R.drawable.placeholder)
                    .into(posterImageView);
        }

        return convertView;
    }

}