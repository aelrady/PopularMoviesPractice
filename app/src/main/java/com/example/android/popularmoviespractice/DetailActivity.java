package com.example.android.popularmoviespractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Movie data unavailable.", Toast.LENGTH_SHORT).show();
    }
}
