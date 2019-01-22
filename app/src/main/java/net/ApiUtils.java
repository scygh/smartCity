package net;

import utiil.ClobalConstants;

/**
 * 沈程阳
 * created by scy on 2019/1/16 15:14
 * 邮箱：1797484636@qq.com
 */
public class ApiUtils {

    public static final String BASE_URL = ClobalConstants.SERVER_URL;

    public static SOService getSOSService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }

    public static LBService getLbService() {
        return RetrofitClient.getClient(BASE_URL).create(LBService.class);
    }

    public static PhotoService getPhotoService() {
        return RetrofitClient.getClient(BASE_URL).create(PhotoService.class);
    }
}
