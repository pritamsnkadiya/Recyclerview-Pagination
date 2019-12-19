package com.example.demoproject.api;

import com.example.demoproject.model.MovieModel;
import com.example.demoproject.model.RequestModel;
import com.example.demoproject.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("/rest/V1/integration/admin/token")
    Call<String> operatorsLogin(@Body RequestModel requestModel);

    @Headers("Content-Type: application/json")
    @GET("/rest/V1/categories/list")
    Call<ResponseModel> getAllCategory(@Query("searchCriteria[pageSize]=") int index);

    @Headers("Content-Type: application/json")
    @GET("/demos/api/itemList.php")
    Call<List<MovieModel>> getMovies(@Query("index") int index);

/*
    @Headers("Content-Type: application/json")
    @GET("/v1/operators/{user_id}")
    Call<ResponsModel> getAllList(@Path("user_id") String userId);*/

}
