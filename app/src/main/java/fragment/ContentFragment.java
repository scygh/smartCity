package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.scy.android.smartcity.MainActivity;
import com.scy.android.smartcity.R;

import java.util.ArrayList;
import java.util.List;

import base.BasePager;
import base.GovAffairsPager;
import base.HomePager;
import base.NewsCenterPager;
import base.SettingPager;
import base.SmartServicePager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import utiil.StatusBarUtil;
import view.NoScrollerViewPager;

/**
 * 沈程阳
 * created by scy on 2019/1/15 17:29
 * 邮箱：1797484636@qq.com
 */
public class ContentFragment extends BaseFragment {

    @BindView(R.id.main_view)
    NoScrollerViewPager mainView;
    @BindView(R.id.main_radiogroup)
    RadioGroup mainRadiogroup;
    Unbinder unbinder;
    List<BasePager> mBasePagers;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.content_menu, null);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void initData() {
        //初始化五个页面对象
        mBasePagers = new ArrayList<>();
        mBasePagers.add(new HomePager(mActivity));
        mBasePagers.add(new NewsCenterPager(mActivity));
        mBasePagers.add(new SmartServicePager(mActivity));
        mBasePagers.add(new GovAffairsPager(mActivity));
        mBasePagers.add(new SettingPager(mActivity));
        mainView.setAdapter(new ContentAdapter());

        mainRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radio_home:
                        mainView.setCurrentItem(0, false);
                        break;
                    case R.id.radio_news:
                        mainView.setCurrentItem(1, false);
                        break;
                    case R.id.radio_service:
                        mainView.setCurrentItem(2, false);
                        break;
                    case R.id.radio_gov:
                        mainView.setCurrentItem(3, false);
                        break;
                    case R.id.radio_set:
                        mainView.setCurrentItem(4, false);
                        break;

                    default :
                        break;
                }

            }
        });
        //优化viewpager 的初始化数据
        mainView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                BasePager pager = mBasePagers.get(i);
                pager.initData();
                if (i == 1) {
                    setSlidingMenuEnable(true);
                } else {
                    setSlidingMenuEnable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //手动初始化第一页
        mBasePagers.get(0).initData();
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

    class ContentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mBasePagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager pager = mBasePagers.get(position);
            //在这里调用的话会初始化两张页面数据，需要优化
            //pager.initData();
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


    public NewsCenterPager getNewsCenterpager() {
        NewsCenterPager pager = (NewsCenterPager)mBasePagers.get(1);
        return pager;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
