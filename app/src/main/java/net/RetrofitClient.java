package net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 沈程阳
 * created by scy on 2019/1/16 15:04
 * 邮箱：1797484636@qq.com
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static  Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
