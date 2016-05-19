package com.example.code.popularmovies.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    public void onFragmentInteraction(Uri uri) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle args = new Bundle();
        GridviewFragment gridviewFragment;

        if (mTwoPane == true) {
            gridviewFragment = (GridviewFragment) getSupportFragmentManager().findFragmentById(R.id.gridviewFragmentTablet);
        } else {
            gridviewFragment = (GridviewFragment) getSupportFragmentManager().findFragmentById(R.id.gridviewFragment);
        }

        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                gridviewFragment.changeSortStatus(1);
                break;
            case R.id.sort_by_rating:
                gridviewFragment.changeSortStatus(2);
                break;
            case R.id.show_favourites:
                gridviewFragment.changeSortStatus(3);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.activity_main);

        //If there is detailsFragment already, then replace it with new fragment,
        //If not, then don't do anything.
        //Poster fragment will tell us if there is new fragment or not
        if (findViewById(R.id.detailsFragmentContainer) == null) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailsFragmentContainer, new MovieDetailsFragment())
                        .commit();
            }
        }


    }}

