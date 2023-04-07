package com.example.newyorktimesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import models.ArticleEntity;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM favorite_articles")
    List<ArticleEntity> getAllFavoriteArticles();

    @Insert
    void insertFavoriteArticle(ArticleEntity article);

    @Delete
    void deleteFavoriteArticle(ArticleEntity article);
}
