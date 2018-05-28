package com.nemanja97.androidposts.service;

import android.content.Intent;

import com.nemanja97.androidposts.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("posts")
    Call<List<Post>> getAll();

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") Integer id);

    @PUT("posts/{id}")
    Call<Post> updatePost(@Body Post post, @Path("id") Integer id);


}
