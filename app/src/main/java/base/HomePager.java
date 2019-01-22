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
public class HomePager extends BasePager {

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView textView = new TextView(mActivity);
        textView.setText("首页");
        textView.setGravity(Gravity.CENTER);
        headerTitle.setText("智慧城市");
        baseFramelayout.addView(textView);
        headerButt.setVisibility(View.INVISIBLE);
    }
}
