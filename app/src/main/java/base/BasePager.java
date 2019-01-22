package base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.scy.android.smartcity.MainActivity;
import com.scy.android.smartcity.R;

import butterknife.BindView;

/**
 * 沈程阳
 * created by scy on 2019/1/15 18:26
 * 邮箱：1797484636@qq.com
 */
public class BasePager {

    public Activity mActivity;

    TextView headerTitle;
    ImageButton headerButt;
    ImageButton picc;
    FrameLayout baseFramelayout;
    public View mRootView;

    public BasePager(Activity activity) {
        mActivity = activity;
        mRootView = initViews();
    }

    public View initViews() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        headerTitle = view.findViewById(R.id.header_title);
        headerButt = view.findViewById(R.id.header_butt);
        picc = view.findViewById(R.id.header_pic_c);
        baseFramelayout = view.findViewById(R.id.base_framelayout);

        headerButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return  view;
    }

    private void toggle() {
        MainActivity activity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = activity.getSlidingMenu();
        slidingMenu.toggle();
    }

    public void initData() {

    }
}
