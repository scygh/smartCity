package detailBase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.scy.android.smartcity.MainActivity;
import com.scy.android.smartcity.R;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import bean.ResponseData;
import butterknife.BindView;

/**
 * 沈程阳
 * created by scy on 2019/1/16 22:25
 * 邮箱：1797484636@qq.com
 *
 * 侧边栏新闻详情页
 */
public class NewsMenuDetailpager extends BaseMenuDetailPager {

    ViewPager vpNewsMenuDetail;
    TabLayout mTabLayout;
    List<ResponseData.DataBean.ChildrenBean> mChildrenBeans;
    List<TabDetailPager> mTabDetailPagers;

    public NewsMenuDetailpager(Activity activity, List<ResponseData.DataBean.ChildrenBean> childrenBeanList) {
        super(activity);
        mChildrenBeans = childrenBeanList;
    }

    @Override
    public View initViews() {
       /* TextView textView = new TextView(mActivity);
        textView.setText("新闻");
        textView.setGravity(Gravity.CENTER);
        return textView;*/

        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        vpNewsMenuDetail = view.findViewById(R.id.vp_news_menu_detail);
        mTabLayout = view.findViewById(R.id.tab_layout);
        return view;
    }

    @Override
    public void initData() {
        //初始化服务器的页签数量
        mTabDetailPagers = new ArrayList<>();
        for(int i = 0; i<mChildrenBeans.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mChildrenBeans.get(i));
            mTabDetailPagers.add(pager);
        }
        vpNewsMenuDetail.setAdapter(new NewsMenuDetailPagerAdapter());
        //关联，必须在viewpager setAdapter 之后
        mTabLayout.setupWithViewPager(vpNewsMenuDetail);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int p = tab.getPosition();
                if (p == 0) {
                    setSlidingMenuEnable(true);
                } else {
                    setSlidingMenuEnable(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setSlidingMenuEnable(Boolean slidingMenuEnable) {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (slidingMenuEnable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class NewsMenuDetailPagerAdapter extends PagerAdapter {

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mChildrenBeans.get(position).getTitle();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TabDetailPager pager = mTabDetailPagers.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mChildrenBeans.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }
}
