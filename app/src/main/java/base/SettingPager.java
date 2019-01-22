package base;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 沈程阳
 * created by scy on 2019/1/16 10:38
 * 邮箱：1797484636@qq.com
 */
public class SettingPager extends BasePager {

    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setGravity(Gravity.CENTER);
        headerTitle.setText("设置");
        baseFramelayout.addView(textView);
        headerButt.setVisibility(View.INVISIBLE);
    }
}
