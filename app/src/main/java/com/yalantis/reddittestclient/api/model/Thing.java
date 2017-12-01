package com.yalantis.reddittestclient.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ak on 01.12.17.
 */

public class Thing<T> {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("kind")
    private String kind;

    @SerializedName("data")
    private T data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
