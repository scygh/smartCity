package detailBase;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 沈程阳
 * created by scy on 2019/1/16 22:25
 * 邮箱：1797484636@qq.com
 */
public class TopicMenuDetailpager extends BaseMenuDetailPager {

    public TopicMenuDetailpager(Activity activity) {
        super(activity);
    }

    @Override
    public View initViews() {
        TextView textView = new TextView(mActivity);
        textView.setText("专题");
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
