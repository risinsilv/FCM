package com.example.fcm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CalorieNinjasApiService {

    @GET("v1/nutrition")
    Call<NutritionResponse> getNutritionInfo(@Query("query") String query);
}

