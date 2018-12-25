package com.apps.newsviews.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //https://medium.com/@sraju432/news-application-using-retrofit-2-be8d052c7aca
    //private static final String BASE_URL = "https://newsapi.org/v2/everything?q=bitcoin&from=2018-11-25&sortBy=publishedAt&apiKey=9688fe5fdd5148c4bcee81fcf94e1837";
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static RetrofitClient mInstance;
    private static Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) //"https://api.github.com"
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //For Singleton
    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
