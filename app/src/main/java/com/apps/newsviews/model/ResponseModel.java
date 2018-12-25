package com.apps.newsviews.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseModel {

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private ArrayList<ArticleModel> articles = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<ArticleModel> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<ArticleModel> articles) {
        this.articles = articles;
    }
}
