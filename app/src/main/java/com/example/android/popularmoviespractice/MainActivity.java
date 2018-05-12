package com.example.android.popularmoviespractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmoviespractice.api.MovieApiClient;
import com.example.android.popularmoviespractice.api.MovieApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private String sort_param;
    private RecyclerView recyclerView;
    private ArrayList<String> picturePathList;
    private ArrayList<Movie> movies;
    private MovieAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            populateGrid();
        }
        else {
            movies = savedInstanceState.getParcelableArrayList("movies");
            Log.v("Movies: ", String.valueOf(movies));
            picturePathList = savedInstanceState.getStringArrayList("pictures");
            Log.v("Picture paths: ", String.valueOf(picturePathList));
            movieAdapter = new MovieAdapter(MainActivity.this, picturePathList, movies);
            recyclerView.setAdapter(movieAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movies);
        outState.putStringArrayList("pictures", picturePathList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.most_popular) {
            sort_param = "popular";
            populateGrid();
        } else if(item.getItemId() == R.id.highest_rated) {
            sort_param = "top_rated";
            populateGrid();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void populateGrid() {

        final String your_api_key = "f3a1a00157a6bc8750d0d6a07bfd9811";
        sort_param = "popular";

        recyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        MovieApiInterface movieApiInterface = MovieApiClient.getClient().create(MovieApiInterface.class);

        Call<MovieResults> call = movieApiInterface.getMovies(sort_param, your_api_key);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                movies = (ArrayList<Movie>) response.body().getResults();
                picturePathList = new ArrayList<>();
                for (int i = 0; i < movies.size(); i++) {
                    picturePathList.add(IMAGE_BASE_URL + movies.get(i).getPosterPath());
                }
                movieAdapter = new MovieAdapter(MainActivity.this, picturePathList, movies);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

