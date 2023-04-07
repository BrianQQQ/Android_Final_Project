package com.example.newyorktimesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import models.Article;
import models.ArticleEntity;

import android.content.Intent;
import android.net.Uri;

public class MainActivity extends AppCompatActivity implements ArticleAdapter.OnArticleClickListener, FavoritesAdapter.OnFavoriteArticleClickListener {

    EditText editTextSearch;
    RecyclerView recyclerView;
    ArticleAdapter articleAdapter;

    RecyclerView favoritesRecyclerView;
    FavoritesAdapter favoritesAdapter;

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextSearch = findViewById(R.id.search_edit_text);
        recyclerView = findViewById(R.id.search_results_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = editTextSearch.getText().toString();
                if (!searchTerm.isEmpty()) {
                    performSearch(searchTerm);
                }
            }
        });

        favoritesRecyclerView = findViewById(R.id.favorites_recyclerview);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").allowMainThreadQueries().build();

        updateFavoritesRecyclerView(); // Call updateFavoritesRecyclerView() after initializing favoritesDatabaseHelper
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
                // Show help dialog
                break;
            case R.id.menu_view_topics:
                Intent topicsIntent = new Intent(MainActivity.this, TopicsActivity.class);
                startActivity(topicsIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void performSearch(String searchTerm) {
        String apiKey = "wPg7O3wrO5fPacMGzM0p7pg1nkOOO1PJ";
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + searchTerm + "&api-key=" + apiKey;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Article> articles = parseJsonResponse(response);
                        Log.d("MainActivity", "API onResponse: Articles count = " + articles.size());
                        updateRecyclerView(articles);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity", "API error: " + error.getMessage());
                    }
                });


        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onArticleClick(Article article) {
        // Display a fragment with the article details
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
        startActivity(browserIntent);
    }

    @Override
    public void onAddFavoriteArticleClick(Article article) {
        // Add the article to the favorites list in the database
        addArticleToFavorites(article);

        // Update the favorites RecyclerView
        updateFavoritesRecyclerView();
    }


    private void updateRecyclerView(List<Article> articles) {
        articleAdapter = new ArticleAdapter(articles, this);
        recyclerView.setAdapter(articleAdapter);
    }

    private List<Article> parseJsonResponse(JSONObject response) {
        List<Article> articles = new ArrayList<>();
        try {
            JSONObject responseObject = response.getJSONObject("response");
            JSONArray docsArray = responseObject.getJSONArray("docs");
            for (int i = 0; i < docsArray.length(); i++) {
                JSONObject articleJson = docsArray.getJSONObject(i);
                String headline = articleJson.getJSONObject("headline").getString("main");
                String url = articleJson.getString("web_url");
                String publicationDate = articleJson.getString("pub_date");

                Article article = new Article(headline, url, publicationDate);
                articles.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return articles;
    }

    private void addArticleToFavorites(Article article) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.headline = article.getHeadline();
        articleEntity.url = article.getUrl();
        articleEntity.publicationDate = article.getPublicationDate();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").allowMainThreadQueries().build();
        db.articleDao().insertFavoriteArticle(articleEntity);
    }

    private void removeArticleFromFavorites(ArticleEntity articleEntity) {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").allowMainThreadQueries().build();
        db.articleDao().deleteFavoriteArticle(articleEntity);
    }

    private void updateFavoritesRecyclerView() {
        favoritesRecyclerView = findViewById(R.id.favorites_recyclerview);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the favorites list from the database
        List<ArticleEntity> favoriteArticles = appDatabase.articleDao().getAllFavoriteArticles();

        favoritesAdapter = new FavoritesAdapter(favoriteArticles, this); // Pass 'this' instead of the new listener

        favoritesRecyclerView.setAdapter(favoritesAdapter);
    }

    @Override
    public void onDeleteFavoriteArticleClick(ArticleEntity articleEntity) {
        // Remove the article from the favorites list in the database
        removeArticleFromFavorites(articleEntity);

        // Update the favorites RecyclerView
        updateFavoritesRecyclerView();
    }


}