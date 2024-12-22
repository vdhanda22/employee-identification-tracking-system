package com.karzansoft.fastvmi.Network;


import com.karzansoft.fastvmi.BuildConfig;
import com.karzansoft.fastvmi.Utils.Constant;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yasir on 3/21/2016.
 */
public class WebServiceFactory {

    private static WebService service;
    private static Retrofit retrofit;

    public static WebService getInstance() {
        if (service == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(5, TimeUnit.MINUTES);
            httpClient.readTimeout(5, TimeUnit.MINUTES);

            if (BuildConfig.DEBUG)
            {
               /* httpClient.connectionSpecs(Arrays.asList(
                        ConnectionSpec.CLEARTEXT));*/
                httpClient.addInterceptor(logging);

            } else {
                httpClient.connectionSpecs(Arrays.asList(
                        ConnectionSpec.CLEARTEXT));
//                httpClient.connectionSpecs(Arrays.asList(
//                        ConnectionSpec.MODERN_TLS,
//                        ConnectionSpec.COMPATIBLE_TLS));
            }


             retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            service = retrofit.create(WebService.class);
        }

        return service;

    }

    public static Retrofit getRetrofit()// need to handle null object case later
    {
        return retrofit;
    }
}
