package com.example.newyorktimesapp;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;

import models.ArticleEntity;

public class TopicsAdapter extends FavoritesAdapter {

    private List<ArticleEntity> articles;
    private OnFavoriteArticleClickListener onFavoriteArticleClickListener;

    public TopicsAdapter(List<ArticleEntity> articles, OnFavoriteArticleClickListener onFavoriteArticleClickListener) {
        super(articles, onFavoriteArticleClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleEntity articleEntity = articles.get(position);
        holder.headlineTextView.setText(articleEntity.headline);

        Log.d("TopicsAdapter", "onBindViewHolder: Binding data for position " + position);

        holder.deleteFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFavoriteArticleClickListener.onDeleteFavoriteArticleClick(articleEntity);
            }
        });

    }
}

