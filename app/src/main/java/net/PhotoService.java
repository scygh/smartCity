package net;

import bean.ResponsePhotoData;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 沈程阳
 * created by scy on 2019/1/19 11:00
 * 邮箱：1797484636@qq.com
 */
public interface PhotoService {

    @GET("photos/photos_1.json")
    Call<ResponsePhotoData> getPhotoAnswers();

}
