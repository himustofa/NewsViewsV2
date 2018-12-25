package com.apps.newsviews.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST //@POST("create"): Url end point
    Call<ResponseBody> mCreate(
            @Field("name") String name,
            @Field("author") String author,
            @Field("title") String title,
            @Field("description") String description,
            @Field("url") String url,
            @Field("urlToImage") String urlToImage,
            @Field("publishedAt") String publishedAt,
            @Field("content") String content
    );
}
