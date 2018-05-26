package com.nemanja97.androidposts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Tag implements Serializable {

    @SerializedName(value="id")
    @Expose
    private int id;
    @SerializedName(value="name")
    @Expose
    private String name;
    private List<Post> posts;

    public Tag(int id, String name, List<Post> posts) {
        this.id = id;
        this.name = name;
        this.posts = posts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", posts=" + posts +
                '}';
    }
}
