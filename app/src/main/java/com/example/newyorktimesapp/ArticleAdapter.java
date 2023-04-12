package com.example.newyorktimesapp;

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

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private OnArticleClickListener onArticleClickListener;

    public ArticleAdapter(List<Article> articles, OnArticleClickListener onArticleClickListener) {
        this.articles = articles;
        this.onArticleClickListener = onArticleClickListener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.headlineTextView.setText(article.getHeadline());
        holder.publicationDateTextView.setText(article.getPublicationDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onArticleClickListener != null) {
                    onArticleClickListener.onArticleClick(article);
                }
            }
        });

        holder.addFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onArticleClickListener != null) {
                    onArticleClickListener.onAddFavoriteArticleClick(article);
                }
            }
        });

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onArticleClickListener != null) {
                    onArticleClickListener.onInfoButtonClick(article);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView headlineTextView;
        TextView publicationDateTextView;
        Button addFavoriteButton;
        Button infoButton;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            headlineTextView = itemView.findViewById(R.id.headline_text_view);
            publicationDateTextView = itemView.findViewById(R.id.publication_date_text_view);
            addFavoriteButton = itemView.findViewById(R.id.add_favorite_button);
            infoButton = itemView.findViewById(R.id.info_button);
        }
    }

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
        void onAddFavoriteArticleClick(Article article);
        void onDeleteFavoriteArticleClick(ArticleEntity articleEntity);
        void onInfoButtonClick(Article article); // Add this line
    }
}
