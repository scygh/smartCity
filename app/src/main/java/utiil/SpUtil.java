package utiil;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 沈程阳
 * created by scy on 2019/1/13 16:54
 * 邮箱：1797484636@qq.com
 */
public class SpUtil {
    private static final String ISFIRSTVISIT  = "isFirstVisit";
    private static final String LVID  = "LvId";
    private static final String SPNAME  = "spname";

    public static void setIsFirstVisit(Context x, Boolean value) {
        SharedPreferences sp = x.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(ISFIRSTVISIT, value).commit();
    }

    public static boolean getIsFirstVisit(Context x) {
        SharedPreferences sp = x.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        return sp.getBoolean(ISFIRSTVISIT, false);
    }

    public static void setLvId(Context x, String value) {
        SharedPreferences sp = x.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        sp.edit().putString(LVID, value).commit();
    }

    public static String getLvId(Context x, String defaultvalue) {
        SharedPreferences sp = x.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        return sp.getString(LVID, defaultvalue);
    }
}
