package com.aleksei.moviesapp.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aleksei.moviesapp.R;
import com.aleksei.moviesapp.data.MovieAdapter;
import com.aleksei.moviesapp.models.Movie;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movies;
    private RequestQueue requestQueue;
    private SearchView searchEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movies);
        recyclerView.setAdapter(movieAdapter);

        requestQueue = Volley.newRequestQueue(this);
        getMovies("https://www.omdbapi.com/?apikey=5d676977&s=batman");


        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url = "https://www.omdbapi.com/?apikey=5d676977&s=" + query;

                getMovies(url);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String url = "https://www.omdbapi.com/?apikey=5d676977&s=" + newText;

                getMovies(url);
                return true;
            }
        });

    }

    private void getMovies(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            JSONArray jsonArray = response.optJSONArray("Search");
            if (jsonArray != null) {
                movies.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    Movie movie = new Movie();
                    movie.setTitle(jsonObject.optString("Title"));
                    movie.setPosterUrl(jsonObject.optString("Poster"));
                    movie.setYear(jsonObject.optInt("Year"));

                    movies.add(movie);
                }
            }
            movieAdapter.notifyDataSetChanged();
        }, error -> error.printStackTrace());

        requestQueue.add(request);
    }
}