package models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_articles")
public class ArticleEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String headline;
    public String url;
    public String publicationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }
}
