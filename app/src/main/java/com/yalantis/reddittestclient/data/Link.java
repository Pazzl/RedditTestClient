package com.yalantis.reddittestclient.data;

import com.google.gson.annotations.SerializedName;

import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ak on 01.12.17.
 */

public class Link extends RealmObject {

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String SUBREDDIT = "subreddit";
    private static final String CREATED_UTC = "created_utc";
    private static final String THUMBNAIL = "thumbnail";
    private static final String URL = "url";
    private static final String SCORE = "score";
    private static final String NUM_COMMENTS = "num_comments";

    @PrimaryKey
    @SerializedName(ID)
    private String id;
    @SerializedName(TITLE)
    private String title;
    @SerializedName(AUTHOR)
    private String author;
    @SerializedName(SUBREDDIT)
    private String subReddit;
    @SerializedName(CREATED_UTC)
    private long createdUTC;
    @SerializedName(THUMBNAIL)
    private String thumbnailURL;
    @SerializedName(URL)
    private String url;
    @SerializedName(SCORE)
    private int rating;
    @SerializedName(NUM_COMMENTS)
    private int numberOfComments;

    public Link() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubReddit() {
        return subReddit;
    }

    public void setSubReddit(String subReddit) {
        this.subReddit = subReddit;
    }

    public long getCreatedUTC() {
        return createdUTC;
    }

    public void setCreatedUTC(long createdUTC) {
        this.createdUTC = createdUTC;
    }

    public int getCreatedHours() {
        return (int) TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - createdUTC * 1000L);
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    @Override
    public String toString() {
        return "Link{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", subReddit='" + subReddit + '\'' +
                ", createdUTC=" + createdUTC +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                ", url='" + url + '\'' +
                ", rating=" + rating +
                ", numberOfComments=" + numberOfComments +
                '}';
    }

}
