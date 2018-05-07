package com.example.android.popularmoviespractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.popularmoviespractice.api.MovieApiClient;
import com.example.android.popularmoviespractice.api.MovieApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateGrid();
    }

    public void populateGrid() {

        final String sort_param = "popular";
        final String your_api_key = "api_key";

        final RecyclerView recyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        MovieApiInterface movieApiInterface = MovieApiClient.getClient().create(MovieApiInterface.class);

        Call<MovieResults> call = movieApiInterface.getMovies(sort_param, your_api_key);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                List<Movie> movies = response.body().getResults();
                ArrayList<String> picturePathList = new ArrayList<>();
                for (int i = 0; i < movies.size(); i++) {
                    picturePathList.add(IMAGE_BASE_URL + movies.get(i).getPosterPath());
                }
                MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, picturePathList);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

