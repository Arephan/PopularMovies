package com.example.code.popularmovies.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.code.popularmovies.R;
import com.example.code.popularmovies.fragments.GridviewFragment;
import com.example.code.popularmovies.fragments.MovieDetailsFragment;
import com.example.code.popularmovies.fragments.ReviewFragment;
import com.orm.SugarContext;


public class MainActivity extends AppCompatActivity implements GridviewFragment.OnFragmentInteractionListener, MovieDetailsFragment.OnFragmentInteractionListener, ReviewFragment.OnFragmentInteractionListener {
    Boolean mTwoPane;

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.activity_main);

        //If there is detailsFragment already, then replace it with new fragment,
        //If not, then don't do anything.
        //Poster fragment will tell us if there is new fragment or not
        if (findViewById(R.id.detailsFragment) == null) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailsFragment, new MovieDetailsFragment())
                        .commit();
            }
        }


    }}

