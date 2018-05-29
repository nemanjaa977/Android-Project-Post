package com.nemanja97.androidposts.service;

import com.nemanja97.androidposts.model.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TagService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("tag")
    Call<List<Tag>> getAll();

    @POST("tag/{id}")
    Call<Tag> createTag(@Body Tag tag);


}
