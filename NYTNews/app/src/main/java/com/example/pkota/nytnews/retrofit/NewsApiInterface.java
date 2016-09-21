package com.example.pkota.nytnews.retrofit;

import com.example.pkota.nytnews.utils.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by pkota on 07-09-2016.
 */
public interface NewsApiInterface {


    @GET("nyt/all/all.json")
    Call<News> getAllNews();

    @GET("nyt/arts/arts.json")
    Call<News> getAllArtNews();

    @GET("nyt/business/business.json")
    Call<News> getBusinessNews();

    @GET("nyt/sports/sports.json")
    Call<News> getSportsNews();

    @GET("nyt/health/health.json")
    Call<News> getHealthNews();

    @GET("nyt/{title}/{specificID}")
    Call<News> getSpecificNews(@Path("title") String title,@Path("specificID") String taskId);
}
