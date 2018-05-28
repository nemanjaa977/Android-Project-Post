package com.nemanja97.androidposts.service;

import com.nemanja97.androidposts.model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("comment")
    Call<List<Comment>> getAll();

    @GET("comment/post/{id}")
    Call<List<Comment>> getAllComment(@Path("id")Integer id);

    @POST("comment")
    Call<Comment> createComment(@Body Comment comment);

    @DELETE("comment/{id}")
    Call<Void> deleteComment(@Path("id") Integer id);

}
