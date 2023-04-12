package com.example.newyorktimesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.SharedPreferences;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import models.Article;
import models.ArticleEntity;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ArticleAdapter.OnArticleClickListener, FavoritesAdapter.OnFavoriteArticleClickListener {

    EditText editTextSearch;
    RecyclerView recyclerView;
    ArticleAdapter articleAdapter;

    RecyclerView favoritesRecyclerView;
    FavoritesAdapter favoritesAdapter;

    private AppDatabase appDatabase;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SEARCH_TERM = "searchTerm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextSearch = findViewById(R.id.search_edit_text);

        // Retrieve the previously saved search term from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String savedSearchTerm = sharedPreferences.getString(SEARCH_TERM, "");
        editTextSearch.setText(savedSearchTerm);

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
                    // Save the search term to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SEARCH_TERM, searchTerm);
                    editor.apply();

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
                AlertDialog.Builder helpDialogBuilder = new AlertDialog.Builder(this);
                helpDialogBuilder.setTitle(getString(R.string.help))
                        .setMessage(getString(R.string.help_message))
                        .setPositiveButton(getString(R.string.ok), null);
                AlertDialog helpDialog = helpDialogBuilder.create();
                helpDialog.show();
                break;
            case R.id.menu_view_topics:
                Intent topicsIntent = new Intent(MainActivity.this, TopicsActivity.class);
                startActivity(topicsIntent);
                break;
//            case R.id.mainPage: //back to main page
//                Intent intent_main = new Intent(MarsRoverPhotosView.this, MainActivity.class);
//                startActivity(intent_main);
//                break;
//            case R.id.gallery:
//                Intent intent1 = new Intent(MarsRoverPhotosView.this, GalleryActivity.class);
//
//                startActivity(intent1);
//
//                break;
//
//            case R.id.tokitten:
//                Intent intent = new Intent(MarsRoverPhotosView.this, KittenImages.class);
//
//                startActivity(intent);
//                break;
//            case R.id.toweather:
//
//                Intent intent5 = new Intent(MarsRoverPhotosView.this, WeatherStack.class);
//
//                startActivity(intent5);
//                break;
//            case R.id.tomarsphoto:
//
//                Intent intent6 = new Intent(MarsRoverPhotosView.this, MarsRoverPhotosView.class);
//
//                startActivity(intent6);
//                break;
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
        long insertedArticleId = addArticleToFavorites(article);

        // Update the favorites RecyclerView
        updateFavoritesRecyclerView();

        // Show a Toast message
        Toast.makeText(MainActivity.this, "Article added to favorites", Toast.LENGTH_SHORT).show();

        // Show an AlertDialog
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Article added")
                .setMessage("The article has been added to your favorites.")
                .setPositiveButton("OK", null)
                .show();

        // Show a Snackbar with an undo action
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Article added to favorites", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the article from the favorites list in the database
                removeArticleFromFavorites(insertedArticleId);

                // Update the favorites RecyclerView
                updateFavoritesRecyclerView();

                // Show a confirmation message
                Snackbar.make(findViewById(android.R.id.content), "Article removed from favorites", Snackbar.LENGTH_SHORT).show();
            }
        });
        snackbar.show();
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

    private long addArticleToFavorites(Article article) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.headline = article.getHeadline();
        articleEntity.url = article.getUrl();
        articleEntity.publicationDate = article.getPublicationDate();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").allowMainThreadQueries().build();
        long insertedArticleId = db.articleDao().insertFavoriteArticle(articleEntity);
        return insertedArticleId;
    }

    private void removeArticleFromFavorites(long articleId) {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").allowMainThreadQueries().build();
        ArticleEntity articleEntity = db.articleDao().getFavoriteArticleById(articleId);
        if (articleEntity != null) {
            db.articleDao().deleteFavoriteArticle(articleEntity);
        }
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
        removeArticleFromFavorites(articleEntity.id);

        // Update the favorites RecyclerView
        updateFavoritesRecyclerView();
    }

    @Override
    public void onInfoButtonClick(Article article) {
        // Create a new instance of ArticleInfoFragment with the article information
        ArticleInfoFragment infoFragment = ArticleInfoFragment.newInstance(article.getHeadline(), article.getPublicationDate());

        // Add the fragment to the activity
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, infoFragment)
                .addToBackStack(null)
                .commit();
    }


}