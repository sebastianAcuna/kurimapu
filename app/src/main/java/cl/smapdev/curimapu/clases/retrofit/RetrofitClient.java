package cl.smapdev.curimapu.clases.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.tablas.Config;
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

    public static Retrofit getClient(String direccion){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit ==null){
            String url = "http://"+direccion+"/curimapu/core/models/android/";
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
