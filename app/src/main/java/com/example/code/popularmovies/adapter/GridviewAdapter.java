package com.example.code.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.code.popularmovies.R;
import com.example.code.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by code on 2016-04-20.
 */
public class GridviewAdapter extends BaseAdapter {
    public ArrayList<Movie> moviesArr;
    private Context context;
    private LayoutInflater inflater;
    private String apiKeyUrl = "?api_key=783ba962d1e5f0bb1f38081b34431edd";
    private String baseUrl = "http://image.tmdb.org/t/p/w185";

    public GridviewAdapter(Context context, ArrayList<Movie> movies) {
        this.moviesArr = movies;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return moviesArr.size();
    }

    @Override
    public Movie getItem(int position) {
        return moviesArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return moviesArr.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_layout, null);
        } else {
            ImageView posterImageView = (ImageView) convertView.findViewById(R.id.posterGridView);
            Picasso.with(context)
                    .load(baseUrl + moviesArr.get(position).posterPath + apiKeyUrl)
                    .error(R.drawable.placeholder)
                    .into(posterImageView);
        }

        return convertView;
    }
}
