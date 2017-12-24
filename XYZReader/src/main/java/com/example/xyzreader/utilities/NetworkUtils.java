package com.example.xyzreader.utilities;

import android.content.Context;

import com.example.xyzreader.remote.ArticleService;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.Cache;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    private static Retrofit retrofit;

    public static ArticleService buildRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ArticleService.ENDPOINT)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ArticleService.class);
    }

    private static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static boolean sIsPicassoSingletonSet;

    public static Picasso getPicasso(Context context) {
        if (!sIsPicassoSingletonSet) {
            Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);
            okhttp3.OkHttpClient okHttp3Client = new okhttp3.OkHttpClient.Builder().cache(cache).build();
            OkHttp3Downloader downloader = new OkHttp3Downloader(okHttp3Client);
            Picasso picasso = new Picasso.Builder(context).downloader(downloader).build();
            Picasso.setSingletonInstance(picasso);
            sIsPicassoSingletonSet = true;
        }
        return Picasso.with(context);
    }

}