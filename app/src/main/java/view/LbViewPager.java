package view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 沈程阳
 * created by scy on 2019/1/17 17:35
 * 邮箱：1797484636@qq.com
 */
public class LbViewPager extends ViewPager {

    public LbViewPager(@NonNull Context context) {
        super(context);
    }

    public LbViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    int startX;
    int startY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int)ev.getX();
                startY = (int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int)ev.getX();
                int endY = (int)ev.getY();
                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dx) > Math.abs(dy)) {
                    int curr = getCurrentItem();

                    if (dx > 0) {
                        //左右滑动到第一个和最后一个的时候就要求parent拦截
                        if (curr == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        if (curr == getAdapter().getCount()-1) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }

                } else {
                    //上下滑动parent拦截
                    getParent().requestDisallowInterceptTouchEvent(false);

                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
