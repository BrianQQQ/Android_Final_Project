package com.example.newyorktimesapp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.ArticleEntity;

public class TopicsAdapter extends FavoritesAdapter {

    private List<ArticleEntity> articles;
    private OnTopicsArticleClickListener onTopicsArticleClickListener;

    public TopicsAdapter(List<ArticleEntity> articles, OnTopicsArticleClickListener onTopicsArticleClickListener) {
        super(articles, null);
        this.articles = articles;
        this.onTopicsArticleClickListener = onTopicsArticleClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleEntity articleEntity = articles.get(position);
        holder.headlineTextView.setText(articleEntity.headline);

        Log.d("TopicsAdapter", "onBindViewHolder: Binding data for position " + position);

        holder.deleteFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTopicsArticleClickListener.onDeleteTopicsArticleClick(articleEntity);
            }
        });

    }

    public interface OnTopicsArticleClickListener {
        void onDeleteTopicsArticleClick(ArticleEntity articleEntity);
    }

    @Override
    @NonNull
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item, parent, false);
        return new TopicsArticleViewHolder(view);
    }

    class TopicsArticleViewHolder extends ArticleViewHolder {
        public TopicsArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ArticleEntity articleEntity = articles.get(position);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleEntity.url));
                        itemView.getContext().startActivity(browserIntent);
                    }
                }
            });
        }
    }
}
