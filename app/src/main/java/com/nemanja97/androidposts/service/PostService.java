package com.nemanja97.androidposts.service;

import com.nemanja97.androidposts.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface PostService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("posts")
    Call<List<Post>> getAll();

}
