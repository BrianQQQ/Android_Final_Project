package com.example.newyorktimesapp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Article;
import models.ArticleEntity;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ArticleViewHolder> {

    private List<ArticleEntity> articles;
    private OnFavoriteArticleClickListener onFavoriteArticleClickListener;

    public FavoritesAdapter(List<ArticleEntity> articles, OnFavoriteArticleClickListener onFavoriteArticleClickListener) {
        this.articles = articles;
        this.onFavoriteArticleClickListener = onFavoriteArticleClickListener;
    }


    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleEntity articleEntity = articles.get(position);
        holder.headlineTextView.setText(articleEntity.headline);

        Log.d("FavoritesAdapter", "onBindViewHolder: Binding data for position " + position);

        holder.deleteFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFavoriteArticleClickListener.onDeleteFavoriteArticleClick(articleEntity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView headlineTextView;
        Button deleteFavoriteButton;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            headlineTextView = itemView.findViewById(R.id.headline_text_view);
            deleteFavoriteButton = itemView.findViewById(R.id.delete_favorite_button);

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


    public interface OnFavoriteArticleClickListener {
        void onDeleteFavoriteArticleClick(ArticleEntity articleEntity);
    }
}
