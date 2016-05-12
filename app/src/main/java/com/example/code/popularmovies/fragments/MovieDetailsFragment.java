package com.example.code.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.code.popularmovies.R;
import com.example.code.popularmovies.models.Favourite;
import com.example.code.popularmovies.services.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MovieDetailsFragment extends Fragment {
    private static String KEY_YOUTUBE = "key";
    private static String JSON_ARRAY = "results";
    private static String apiKeyURL = "?api_key=783ba962d1e5f0bb1f38081b34431edd";
    private static String generalBaseURL = "http://api.themoviedb.org/3/movie/%s/videos";
    private static String youtubeBaseURL = "https://www.youtube.com/watch?v=";
    private static String youtubeKey;
    public String KEY_CONTENT = "content";
    @Bind(R.id.posterFragment)
    ImageView moviePoster;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.releaseDate)
    TextView releaseDate;
    @Bind(R.id.plotSynopsis)
    TextView plotSynopsis;
    @Bind(R.id.popularity)
    TextView popularity;
    @Bind(R.id.voteAverage)
    TextView voteAverage;
    @Bind(R.id.favouritesButton)
    ImageView favouritesButton;
    @Bind(R.id.trailerPlayButton)
    ImageView playButton;
    @Bind(R.id.readReviews)
    ImageView readReview;
    private String imageBaseURL = "http://image.tmdb.org/t/p/w185";
    private ArrayList<String> reviews = new ArrayList();
    private JSONArray results = null;
    private String reviewBaseURL = "http://api.themoviedb.org/3/movie/";
    private String posterPathStr;
    private String releaseDateStr;
    private String plotSynopsisStr;
    private Double voteAverageDoub;
    private Double popularityDoub;
    private Boolean favourited;
    private String titleStr;
    private Long id;

    private OnFragmentInteractionListener mListener;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getYoutubeKey(String movieID) {
        String URL = String.format(generalBaseURL, movieID) + apiKeyURL;
        sendRequest(URL, "youtube");
    }

    public void reviewParseJSON(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            results = jsonObject.getJSONArray(JSON_ARRAY);

            for (int i = 0; i < results.length(); i++) {
                JSONObject jo = results.getJSONObject(i);
                reviews.add(jo.getString(KEY_CONTENT));
            }

            ReviewFragment newFragment = new ReviewFragment();
            Bundle args = new Bundle();
            args.putStringArrayList("reviews", reviews);
            newFragment.setArguments(args);
            android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.detailsContainer, newFragment);
//            GridView gv = (GridView) getActivity().findViewById(R.id.gridviewFragment);
//            gv.setVisibility(View.GONE);
            transaction.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    private void extractArguments(Bundle b) {
        titleStr = b.getString("title");
        title.setText(titleStr);
        posterPathStr = b.getString("posterPath");
        plotSynopsis.setText(b.getString("plotSynopsis"));
        id = b.getLong("id");
        voteAverage.setText(String.valueOf(b.getDouble("voteAverage")));
        popularity.setText(String.valueOf(b.getDouble("popularity")));
        favourited = b.getBoolean("favourited");
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle b = getArguments();
        if (b != null) {
            extractArguments(b);
            setupView();
        }
    }

    private void setupView() {
        unPackBundle(getArguments());
        setView();

        //Did user already favourite this movie?
        List<Favourite> favourites = Favourite.find(Favourite.class, "title = ?", titleStr);

        if (favourites.size() == 1) {
            favouritesButton.setImageResource(R.drawable.favourited);
        } else {
            favouritesButton.setImageResource(R.drawable.unfavourited);
        }

        //Make Scrolling possible incase of text overflow
        plotSynopsis.setMovementMethod(new ScrollingMovementMethod());

        //ToolBar setup
        //TODO: Complete toolbar setup

        //If trailer button is clicked
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYoutubeKey(String.valueOf(id));
            }
        });

        //If favourites button is clicked
        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Confirm fav object doesn't exist yet
                List<Favourite> favourites = Favourite.find(Favourite.class, "title=?", titleStr);


                if (favourites.size() == 0) {
                    Favourite favourite = new Favourite(
                            id,
                            titleStr,
                            posterPathStr,
                            releaseDateStr,
                            popularityDoub,
                            voteAverageDoub,
                            plotSynopsisStr,
                            youtubeKey,
                            favourited);
                    favourite.save();

                    // make certain that image is set to favourited
                    favouritesButton.setImageDrawable(getResources().getDrawable(R.drawable.favourited));
                } else {
                    //Unfavourite
                    favourites.get(0).delete();
                    favouritesButton.setImageDrawable(getResources().getDrawable(R.drawable.unfavourited));
                }
            }
        });

        //If read review is clicked
        readReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Fetch Review data, and start a new activity to show reviews
                // http://api.themoviedb.org/3/movie/83542/reviews?api_key=783ba962d1e5f0bb1f38081b34431edd
                String url = reviewBaseURL + id + "/reviews" + apiKeyURL;
                sendRequest(url, "review");
            }
        });
    }

    private void unPackBundle(Bundle extras) {
        posterPathStr = extras.getString("posterPath");
        titleStr = extras.getString("title");
        favourited = extras.getBoolean("favourited");
        releaseDateStr = extras.getString("releaseDate");
        plotSynopsisStr = extras.getString("plotSynopsis");
        voteAverageDoub = extras.getDouble("voteAverage");
        popularityDoub = extras.getDouble("popularity");
        id = extras.getLong("id");
    }

    private void setView() {
        Picasso.with(getActivity().getApplicationContext()).load(imageBaseURL + posterPathStr + apiKeyURL).into(moviePoster);
        title.setText(titleStr);
        releaseDate.setText(releaseDateStr);
        plotSynopsis.setText(plotSynopsisStr);
        voteAverage.setText(String.valueOf(voteAverageDoub));
        popularity.setText(String.valueOf(popularityDoub));
    }

    public void sendRequest(String URL, final String calling) {
        RequestQueue queue = VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (calling == "youtube") {
                            youtubeParseJSON(response);
                        }
                        if (calling == "review") {
                            reviewParseJSON(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    private void youtubeParseJSON(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray(JSON_ARRAY);
            //There is only one element in array for this response
            youtubeKey = ja.getJSONObject(0).getString(KEY_YOUTUBE);
            String trailerURL = youtubeBaseURL + youtubeKey;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerURL)));

        } catch (JSONException e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);

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
    public static interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

