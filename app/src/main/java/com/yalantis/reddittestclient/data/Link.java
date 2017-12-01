package com.yalantis.reddittestclient.data;

import com.google.gson.annotations.SerializedName;

import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ak on 01.12.17.
 */

public class Link extends RealmObject {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String SUBREDDIT = "subreddit";
    public static final String CREATED_UTC = "created_utc";
    public static final String THUMBNAIL = "thumbnail";
    public static final String URL = "url";
    public static final String SCORE = "score";
    public static final String NUM_COMMENTS = "num_comments";

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

    public int getCreatedDays() {
        return (int) TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - createdUTC);
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
}
