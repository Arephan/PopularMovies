package com.example.code.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.code.popularmovies.R;
import com.example.code.popularmovies.adapter.FavouriteAdapter;
import com.example.code.popularmovies.adapter.GridviewAdapter;
import com.example.code.popularmovies.models.Favourite;
import com.example.code.popularmovies.models.Movie;
import com.example.code.popularmovies.services.MovieParseJSON;
import com.example.code.popularmovies.services.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class GridviewFragment extends Fragment {
    public GridviewAdapter gridviewAdapter;
    public FavouriteAdapter favouriteAdapter;
    public String apiKeyURL = new String("&api_key=783ba962d1e5f0bb1f38081b34431edd");
    public String baseURL = new String("http://api.themoviedb.org/3");
    public String popMoviesURL = new String("/discover/movie?sort_by=popularity.desc");
    public String highestRatedMoviesURL = new String("/discover/movie?sort_by=vote_average.desc");
    public String pageNumURL = new String("&page=");
    @Bind(R.id.gridView)
    GridView gridView;
    private String imageBaseURL = "http://image.tmdb.org/t/p/w185";
    private String generalBaseURL = "http://api.themoviedb.org/3/movie/%s/videos";
    private ArrayList popMovieList = new ArrayList();
    private ArrayList highestRatedList = new ArrayList();
    private ArrayList favList = new ArrayList<Favourite>();
    private Integer highestRatedPageNum = 1;
    private Integer popMoviePageNum = 1;
    private Boolean sortState = true;  //true = popular movies, false = highest rated
    private Boolean isShowingFav = false; //Fetch popMovies by default
    private Integer gridIndex;


    private OnFragmentInteractionListener mListener;

    public GridviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gridview, container, true);
        ButterKnife.bind(this,view);
        setupView();
        if (savedInstanceState != null) {
            gridIndex = savedInstanceState.getInt("gridIndex");
            gridView.smoothScrollToPosition(gridIndex);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void makeCall(String searchURL, Integer pageNum) {
        gridIndex = gridView.getFirstVisiblePosition();
        String URL = baseURL + searchURL + pageNumURL + pageNum + apiKeyURL;
        sendRequest(URL);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        gridIndex = gridView.getFirstVisiblePosition();
        outState.putInt("gridIndex", gridIndex);
        super.onSaveInstanceState(outState);

    }

    private void setupView() {
        // FIND OUT IF FAVOURITES ARE BEING SHOWN OR NOT, SET APPROPARIATE ADAPTER.
        if (isShowingFav) {
            if (favouriteAdapter == null) {
                favouriteAdapter = new FavouriteAdapter(this.getContext(), favList);
            }
            gridView.setAdapter(favouriteAdapter);
        } else {
            if (gridviewAdapter == null) {
                gridviewAdapter = new GridviewAdapter(this.getContext(), popMovieList);
            }
            gridView.setAdapter(gridviewAdapter);
        }


        setupGridView();


        // BEGIN FETCHING CYCLE IF FIRST TIME LOADING.
        if (popMoviePageNum == 1 && sortState == true) {
            makeCall(popMoviesURL, popMoviePageNum);
            popMoviePageNum++;
        }
    }

    public void changeSortStatus(Integer sortState) {
        switch (sortState) {
            case 1:
                this.sortState = true;
                this.isShowingFav = false;
                gridviewAdapter.moviesArr = popMovieList;
                gridviewAdapter.notifyDataSetChanged();
                gridView.setAdapter(gridviewAdapter);
                return;
            case 2:
                this.sortState = false;
                this.isShowingFav = false;

                if (highestRatedPageNum == 1) {
                    makeCall(highestRatedMoviesURL, highestRatedPageNum);
                    highestRatedPageNum++;
                } else {
                    gridviewAdapter.moviesArr = highestRatedList;
                    gridviewAdapter.notifyDataSetChanged();
                    gridView.setAdapter(gridviewAdapter);
                }
                return;
            case 3:
                this.isShowingFav = true;

                if (favouriteAdapter == null) {
                    favouriteAdapter = new FavouriteAdapter(this.getContext(), favList);
                }
                //Always make sure fovouritesList is empty to avoid redundancy
                favouriteAdapter.favouritesArr.clear();
                List<Favourite> favList = Favourite.listAll(Favourite.class);
                for (Favourite fav : favList) {
                    this.favList.add(fav);
                }
                favouriteAdapter.notifyDataSetChanged();
                gridView.setAdapter(favouriteAdapter);
                return;
        }
    }


    // GRIDVIEW SETUP BEGINS
    private void setupGridView() {
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE && !isShowingFav) {
                    /** To do code here*/
                    if (sortState == true) {
                        popMoviePageNum++;
                        makeCall(popMoviesURL, popMoviePageNum);
                    } else {
                        highestRatedPageNum++;
                        makeCall(highestRatedMoviesURL, highestRatedPageNum);
                    }

                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetailsFragment newFragment = new MovieDetailsFragment();
                Bundle args = new Bundle();
                if (isShowingFav) {
                    packFavouriteBundle(args, favouriteAdapter.getItem(position));
                } else {
                    packMovieBundle(args, gridviewAdapter.getItem(position));
                }
                newFragment.setArguments(args);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if (getActivity().findViewById(R.id.detailsFragmentContainer) != null) {
                    transaction.replace(R.id.detailsFragmentContainer, newFragment);
                } else {
                    transaction.replace(R.id.gridviewFragmentContainer, newFragment);
                }
                transaction.addToBackStack(null);
                transaction.commit();
            }

        });

    }

    private void packMovieBundle(Bundle b, Movie movie) {
        b.putString("title", movie.title);
        b.putString("posterPath", movie.posterPath);
        b.putDouble("voteAverage", movie.voteAverage);
        b.putString("releaseDate", movie.releaseDate);
        b.putString("plotSynopsis", movie.plotSynopsis);
        b.putDouble("popularity", movie.popularity);
        b.putBoolean("favourited", movie.favourited);
        b.putLong("id", movie.id);
    }

    private void packFavouriteBundle(Bundle b, Favourite favourite) {
        b.putString("title", favourite.title);
        b.putString("posterPath", favourite.posterPath);
        b.putDouble("voteAverage", favourite.voteAverage);
        b.putString("releaseDate", favourite.releaseDate);
        b.putString("plotSynopsis", favourite.plotSynopsis);
        b.putDouble("popularity", favourite.populairty);
        b.putBoolean("favourited", favourite.favourited);
        b.putLong("id", favourite.id);
    }

    public void sendRequest(String URL) {
        RequestQueue queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    private void showJSON(String json) {
        MovieParseJSON pj = new MovieParseJSON(json);
        pj.movieParseJSON();

        if (gridviewAdapter == null) {
            popMovieList.addAll(pj.movieList);
            gridviewAdapter = new GridviewAdapter(this.getContext(), popMovieList);  // Choose popMovieList by default, since it is shown by default
        } else {
            if (sortState == true) {
                popMovieList.addAll(pj.movieList);
                gridviewAdapter.moviesArr = popMovieList;
            } else {
                highestRatedList.addAll(pj.movieList);
                gridviewAdapter.moviesArr = highestRatedList;
            }
        }
        gridviewAdapter.notifyDataSetChanged();
        gridView.setAdapter(gridviewAdapter);
        gridView.smoothScrollToPosition(gridIndex);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {


        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);


    }
}
