package net;

import bean.ResponseData;
import bean.ResponsePhotoData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 沈程阳
 * created by scy on 2019/1/16 15:09
 * 邮箱：1797484636@qq.com
 */
public interface SOService {

    @GET("categories.json")
    Call<ResponseData> getAnswers();
}
