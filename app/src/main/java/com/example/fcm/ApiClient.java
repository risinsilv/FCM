package com.example.fcm;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.calorieninjas.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(chain -> {
                                Request original = chain.request();
                                Request request = original.newBuilder()
                                        .header("X-Api-Key", "G0S6DRtT1E5Zgxm4lsP0i1Zfjcs0KW15U8Pw4dnR") // Replace with your actual API key
                                        .method(original.method(), original.body())
                                        .build();
                                return chain.proceed(request);
                            })
                            .build())
                    .build();
        }
        return retrofit;
    }
}


