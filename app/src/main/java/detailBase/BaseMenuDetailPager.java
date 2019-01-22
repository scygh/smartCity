package detailBase;

import android.app.Activity;
import android.view.View;


/**
 * 沈程阳
 * created by scy on 2019/1/16 22:22
 * 邮箱：1797484636@qq.com
 */
public abstract class BaseMenuDetailPager {

    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initViews();
    }

    public abstract View initViews();


    public void initData() {

    }
}
