package base;

import android.app.Activity;
import android.content.ClipData;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.scy.android.smartcity.MainActivity;

import net.ApiUtils;
import net.SOService;

import java.util.ArrayList;
import java.util.List;

import bean.ResponseData;
import detailBase.BaseMenuDetailPager;
import detailBase.InteractMenuDetailpager;
import detailBase.NewsMenuDetailpager;
import detailBase.PhotosMenuDetailpager;
import detailBase.TopicMenuDetailpager;
import fragment.LeftMenuFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 沈程阳
 * created by scy on 2019/1/16 10:38
 * 邮箱：1797484636@qq.com
 */
public class NewsCenterPager extends BasePager {
    private static final String TAG = "NewsCenterPager";
    //retrofit
    private SOService mService;
    private List<BaseMenuDetailPager> mBaseMenuDetailPagers;
    private ResponseData data;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        headerTitle.setText("新闻");
        getDataFromServer();
    }

    private void getDataFromServer() {
        mService = ApiUtils.getSOSService();
        loadAnswers();
    }

    public void loadAnswers() {
        mService.getAnswers().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                data = response.body();
                setDataToLeftMenu(data);
                //初始化四个菜单详情页
                setDetailPager();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(mActivity, "没有连接服务器", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDetailPager() {
        mBaseMenuDetailPagers = new ArrayList<>();
        mBaseMenuDetailPagers.add(new NewsMenuDetailpager(mActivity, data.getData().get(0).getChildren()));
        mBaseMenuDetailPagers.add(new TopicMenuDetailpager(mActivity));
        mBaseMenuDetailPagers.add(new PhotosMenuDetailpager(mActivity, picc));
        mBaseMenuDetailPagers.add(new InteractMenuDetailpager(mActivity));
        setMenuDetailpager(0);
    }


    //塞给侧边栏数据
    private void setDataToLeftMenu(ResponseData data){
        MainActivity activity = (MainActivity) mActivity;
        LeftMenuFragment fragment = (LeftMenuFragment) activity.getLeftMenuFragment();
        fragment.getDataFromNewsCenter(data.getData());
    }

    //给侧边栏点击跳转调用的方法
    public void setMenuDetailpager(int position) {
        BaseMenuDetailPager pager = mBaseMenuDetailPagers.get(position);
        if (pager instanceof PhotosMenuDetailpager) {
            picc.setVisibility(View.VISIBLE);
        } else {
            picc.setVisibility(View.INVISIBLE);
        }
        baseFramelayout.removeAllViews();
        baseFramelayout.addView(pager.mRootView);
        pager.initData();
        headerTitle.setText(data.getData().get(position).getTitle());
    }


}
