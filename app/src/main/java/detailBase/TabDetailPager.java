package detailBase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scy.android.smartcity.NewsDetailActivity;
import com.scy.android.smartcity.R;

import net.ApiUtils;
import net.LBService;


import java.util.List;

import bean.ResponseData;
import bean.ResponseDetailData;
import bean.ResponseDetailData.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utiil.SpUtil;
import view.LbViewPager;
import view.RefreshListView;


/**
 * 沈程阳
 * created by scy on 2019/1/17 09:39
 * 邮箱：1797484636@qq.com
 *
 * 页签详情页
 */
public class TabDetailPager{
    private static final String TAG = "TabDetailPager";
    //当前页签的网络数据
    private ResponseData.DataBean.ChildrenBean mBean;
    Activity mActivity;
    View mRootView;
    private LbViewPager mViewPager;
    private LBService mLBService;
    private String url;
    private String moreurl;
    private ResponseDetailData mResponseDetailData;
    private TextView lb_tv;
    private RefreshListView mListView;
    private List<DataBean.NewsBean> newsBean;
    private NewsAdapter adapter;
    private Handler mHandler;
    private LbAdapter lbAdapter;


    public TabDetailPager(Activity activity, ResponseData.DataBean.ChildrenBean childrenBean) {
        mBean = childrenBean;
        mActivity = activity;
        mRootView = initViews();
    }

    public View initViews() {
       View view = View.inflate(mActivity, R.layout.lb_viewpager_container, null);
        final View headerView = View.inflate(mActivity, R.layout.listview_header, null);
       mViewPager = headerView.findViewById(R.id.lb_vp);
       lb_tv = headerView.findViewById(R.id.lb_Tv);
       mListView = view.findViewById(R.id.lb_lv);
       mListView.addHeaderView(headerView);

       mListView.setOnRefreshListener(new RefreshListView.OnRefreshListenter() {
           @Override
           public void onRefresh() {
               loadAnswers();
           }

           @Override
           public void onLoadMore() {
                loadMoreAnswers();
           }
       });

       mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               int headerViewCount = mListView.getHeaderViewsCount();
               position -= headerViewCount;
               DataBean.NewsBean news = newsBean.get(position);

               String lvid = SpUtil.getLvId(mActivity,"");
               if (!lvid.contains(news.getId()+",")){
                   lvid = lvid + news.getId() + ",";
                   SpUtil.setLvId(mActivity, lvid);
               }
               //adapter.notifyDataSetChanged();
                //使用局部刷新
               TextView title = mListView.findViewById(R.id.item_news_title);
               title.setTextColor(Color.GRAY);

               Intent intent = new Intent(mActivity, NewsDetailActivity.class);
               intent.putExtra("url", news.getUrl());
               intent.putExtra("image", news.getListimage());
               intent.putExtra("title", news.getTitle());
               mActivity.startActivity(intent);
           }
       });
       return view;
    }

    public void initData() {
        mLBService = ApiUtils.getLbService();
        url = mBean.getUrl();
        url = url.substring(1);
        Log.d(TAG, "onResponse: " + url);
        loadAnswers();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                lb_tv.setText(mResponseDetailData.getData().getTopnews().get(i).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setDelayLb();
    }

    public void loadAnswers() {
        mLBService.getAnswers(url).enqueue(new Callback<ResponseDetailData>() {
            @Override
            public void onResponse(Call<ResponseDetailData> call, Response<ResponseDetailData> response) {
                mResponseDetailData = response.body();
                Log.d(TAG, "onResponse: " + mResponseDetailData);
                Log.d(TAG, "onResponse: " +mResponseDetailData.getData().getTopnews().size());
                setData();
                //请求成功，刷新结束，隐藏下拉刷新控件
                mListView.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<ResponseDetailData> call, Throwable t) {
                Toast.makeText(mActivity, "网络请求失败！", Toast.LENGTH_SHORT).show();
                mListView.onRefreshComplete();
            }
        });
    }

    public void loadMoreAnswers() {
        moreurl = mResponseDetailData.getData().getMore();
        if (!TextUtils.isEmpty(moreurl)) {
            moreurl = moreurl.substring(1);
            mLBService.getAnswers(moreurl).enqueue(new Callback<ResponseDetailData>() {
                @Override
                public void onResponse(Call<ResponseDetailData> call, Response<ResponseDetailData> response) {
                    mResponseDetailData = response.body();
                    setMoreData();
                    mListView.onRefreshComplete();
                }

                @Override
                public void onFailure(Call<ResponseDetailData> call, Throwable t) {
                    Toast.makeText(mActivity, "网络请求失败！", Toast.LENGTH_SHORT).show();
                    mListView.onRefreshComplete();
                }
            });
        } else {
            Toast.makeText(mActivity, "亲没有更多数据了！", Toast.LENGTH_SHORT).show();
            mListView.onRefreshComplete();
        }

    }

    public void setData() {
        lbAdapter = new LbAdapter();
        mViewPager.setAdapter(lbAdapter);
        lb_tv.setText(mResponseDetailData.getData().getTopnews().get(0).getTitle());
        newsBean = mResponseDetailData.getData().getNews();
        if (newsBean != null) {
            adapter = new NewsAdapter();
            mListView.setAdapter(adapter);
        }
    }

    public void setMoreData() {
        List<DataBean.NewsBean> morenewsBean = mResponseDetailData.getData().getNews();
        if (morenewsBean != null) {
           newsBean.addAll(morenewsBean);
           adapter.notifyDataSetChanged();
           lbAdapter.notifyDataSetChanged();
        }
    }

    public void setDelayLb() {
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    int curr = mViewPager.getCurrentItem();
                    if (curr < mResponseDetailData.getData().getTopnews().size()-1) {
                        curr++;
                    } else {
                        curr = 0;
                    }
                    mViewPager.setCurrentItem(curr);
                    mHandler.sendEmptyMessageDelayed(0, 2000);
                }
            };

            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mHandler.sendEmptyMessageDelayed(0, 2000);
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessageDelayed(0, 2000);
                        break;
                    default :
                        break;
                }
                return false;

            }
        });
    }

    class LbAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mResponseDetailData.getData().getTopnews().size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String iamgeurl = mResponseDetailData.getData().getTopnews().get(position).getTopimage();
            Glide.with(mActivity)
                    .load(iamgeurl)
                    .into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

    //新闻列表适配器
    class NewsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return newsBean.size();
        }

        @Override
        public DataBean.NewsBean getItem(int position) {
            return newsBean.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView= View.inflate(mActivity, R.layout.list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mImage = convertView.findViewById(R.id.item_news_icon);
                viewHolder.mTitle = convertView.findViewById(R.id.item_news_title);
                viewHolder.mDate = convertView.findViewById(R.id.item_news_date);

                convertView.setTag(viewHolder);
            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            DataBean.NewsBean newsBean = getItem(position);
            viewHolder.mTitle.setText(newsBean.getTitle());
            viewHolder.mDate.setText(newsBean.getPubdate());
            Glide.with(mActivity).load(newsBean.getListimage()).into(viewHolder.mImage);

            String readIds = SpUtil.getLvId(mActivity, "");
            if (readIds.contains(newsBean.getId() + ",")) {
                viewHolder.mTitle.setTextColor(Color.GRAY);
            } else {
                viewHolder.mTitle.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView mImage;
        public TextView mTitle;
        public TextView mDate;
    }
}
