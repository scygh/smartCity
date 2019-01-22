package utiil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

/**
 * 沈程阳
 * created by scy on 2019/1/14 11:37
 * 邮箱：1797484636@qq.com
 */
public class StatusBarUtil {
    public static void setStatusBar(Activity activity, Toolbar toolBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //android:fitsSystemWindows="true" 这个属性在当键盘弹出时，会把toolbar上的东西顶到状态栏
            //直接设置toolbar 的上padding
            if (toolBar != null) {
                int statusBarHeight = getStatusBarHeight(activity);
                toolBar.setPadding(toolBar.getPaddingLeft(),statusBarHeight,toolBar.getPaddingRight(),toolBar.getPaddingBottom());
                toolBar.getLayoutParams().height = statusBarHeight + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, ((Context)activity).getResources().getDisplayMetrics());

            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
    }

    public static int getStatusBarHeight(Activity activity) {
        int height = 0;
        int resourceid = ((Context)activity).getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceid > 0) {
            height = ((Context)activity).getResources().getDimensionPixelSize(resourceid);
        }
        return height;
    }
}
