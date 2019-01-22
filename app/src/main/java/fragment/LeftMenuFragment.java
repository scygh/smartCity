package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.scy.android.smartcity.MainActivity;
import com.scy.android.smartcity.R;

import java.util.List;

import base.NewsCenterPager;
import bean.ResponseData;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 沈程阳
 * created by scy on 2019/1/15 17:29
 * 邮箱：1797484636@qq.com
 */
public class LeftMenuFragment extends BaseFragment {

    @BindView(R.id.leftmenu_listview)
    ListView leftmenuListview;
    Unbinder unbinder;
    LeftMenuAdapter mLeftMenuAdapter;
    List<ResponseData.DataBean> dataBeans;
    private int mCurr;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.left_menu, null);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    public void getDataFromNewsCenter(List<ResponseData.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
        mCurr = 0;
        mLeftMenuAdapter = new LeftMenuAdapter();
        leftmenuListview.setAdapter(mLeftMenuAdapter);

        leftmenuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurr = position;
                mLeftMenuAdapter.notifyDataSetChanged();
                toggle();

                setMenuDetailPager(position);
            }
        });
    }

    private void setMenuDetailPager(int position) {
        MainActivity activity = (MainActivity) mActivity;
        ContentFragment fragment = (ContentFragment) activity.getContentFragment();
        NewsCenterPager newsCenterPager = fragment.getNewsCenterpager();
        newsCenterPager.setMenuDetailpager(position);
    }

    private void toggle() {
        MainActivity activity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = activity.getSlidingMenu();
        slidingMenu.toggle();
    }

    class LeftMenuAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return dataBeans.size();
        }

        @Override
        public ResponseData.DataBean getItem(int position) {
            return dataBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.left_menu_list_item, null);
            TextView textView = view.findViewById(R.id.leftmenu_textview);
            ResponseData.DataBean dataBean = getItem(position);
            textView.setText(dataBean.getTitle());
            if (mCurr == position) {
                textView.setEnabled(true);
            } else {
                textView.setEnabled(false);
            }
            return view;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
