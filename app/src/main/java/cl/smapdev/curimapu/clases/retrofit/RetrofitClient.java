package cl.smapdev.curimapu.clases.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(6, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3,  TimeUnit.MINUTES)
            .build();



    public static Retrofit getClient(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit ==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://190.13.170.26/curimapu/core/models/android/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
