package models;

public class Article {
    private String headline;
    private String url;
    private String publicationDate;

    public Article(String headline, String url, String publicationDate) {
        this.headline = headline;
        this.url = url;
        this.publicationDate = publicationDate;
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

