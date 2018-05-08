package net.ali.rhein.mvpbase.network;

import net.ali.rhein.mvpbase.R;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rhein on 3/27/18.
 */

public class NetworkClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        //192.168.10.10
        String base_url = "http://36.89.95.234/project/dis/";
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            OkHttpClient okHttpClient = builder.build();

            retrofit = new Retrofit.Builder().baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}