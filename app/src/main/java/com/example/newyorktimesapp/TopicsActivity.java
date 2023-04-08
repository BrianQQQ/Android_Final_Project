package com.example.newyorktimesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

import models.Article;
import models.ArticleEntity;

public class TopicsActivity extends AppCompatActivity {
    RecyclerView topicsRecyclerView;
    TopicsAdapter topicsAdapter;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        Toolbar toolbar = findViewById(R.id.topics_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        topicsRecyclerView = findViewById(R.id.topics_recyclerview);
        topicsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").allowMainThreadQueries().build();

        updateTopicsRecyclerView();
    }

    private void updateTopicsRecyclerView() {
        List<ArticleEntity> favoriteArticles = appDatabase.articleDao().getAllFavoriteArticles();

        topicsAdapter = new TopicsAdapter(favoriteArticles, new TopicsAdapter.OnTopicsArticleClickListener() {
            @Override
            public void onDeleteTopicsArticleClick(ArticleEntity articleEntity) {
                // Remove the article from the favorites list in the database
                appDatabase.articleDao().deleteFavoriteArticle(articleEntity);

                // Update the RecyclerView
                favoriteArticles.remove(articleEntity);
                topicsAdapter.notifyDataSetChanged();
            }
        });

        topicsRecyclerView.setAdapter(topicsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
