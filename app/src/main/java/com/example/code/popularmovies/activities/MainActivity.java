package com.example.code.popularmovies.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import com.example.code.popularmovies.R;
import com.example.code.popularmovies.fragments.GridviewFragment;
import com.example.code.popularmovies.fragments.MovieDetailsFragment;
import com.orm.SugarContext;


public class MainActivity extends AppCompatActivity implements GridviewFragment.OnFragmentInteractionListener, MovieDetailsFragment.OnFragmentInteractionListener {
    Boolean mTwoPane;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        GridView gv = (GridView) findViewById(R.id.gridviewFragment);
        gv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.activity_main);

        //If there is detailsFragment already, then replace it with new fragment,
        //If not, then don't do anything.

       if (findViewById(R.id.detailsFragment) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailsFragment, new MovieDetailsFragment())
                        .commit();
            }
        } else {
           mTwoPane = false;
       }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
