package com.example.groupproject1;


import java.io.Serializable;
import java.util.Objects;

public class ArticlesInformation implements Serializable {

    private String title;
    private String description;
    private String link;
    private String date;
    private int id;

    public ArticlesInformation() {
    }

    public ArticlesInformation(int id, String description, String link, String date) {
        this.id = id;
        this.description = description;
        this.link = link;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticlesInformation that = (ArticlesInformation) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(link, that.link) &&
                Objects.equals(date, that.date) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, link, id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String headline) {
        this.title = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
