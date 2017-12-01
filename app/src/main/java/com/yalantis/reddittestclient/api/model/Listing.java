package com.yalantis.reddittestclient.api.model;

import com.google.gson.annotations.SerializedName;
import com.yalantis.reddittestclient.data.Link;

import java.util.List;

/**
 * Created by ak on 01.12.17.
 */

public class Listing {

    @SerializedName("before")
    private String before;

    @SerializedName("after")
    private String after;

    @SerializedName("modhash")
    private String modhash;

    @SerializedName("children")
    private List<Thing<Link>> childrens;

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getModhash() {
        return modhash;
    }

    public void setModhash(String modhash) {
        this.modhash = modhash;
    }

    public List<Thing<Link>> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Thing<Link>> childrens) {
        this.childrens = childrens;
    }
}
