package net;

import bean.ResponseData;
import bean.ResponseDetailData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * 沈程阳
 * created by scy on 2019/1/16 15:09
 * 邮箱：1797484636@qq.com
 */
public interface LBService {

    @GET
    Call<ResponseDetailData> getAnswers(@Url String cate);

}
