package com.example.newyorktimesapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import models.ArticleEntity;

@Database(entities = {ArticleEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ArticleDao articleDao();
}
