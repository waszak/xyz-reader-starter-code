package com.example.xyzreader.remote;


import com.example.xyzreader.model.Article;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ArticleService {
    String ENDPOINT = "https://go.udacity.com";

    @GET("/xyz-reader-json")
    Observable<List<Article>> getArticles();
}
