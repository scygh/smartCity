package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scy.android.smartcity.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 沈程阳
 * created by scy on 2019/1/18 10:04
 * 邮箱：1797484636@qq.com
 */
public class RefreshListView extends ListView {

    private View mHeaderView;
    private View mFooterView;
    int height;

    private static final int STATE_PULLTOREFRESH = 1;
    private static final int STATE_RELEASETOREFRESH = 2;
    private static final int STATE_REFRESHING = 3;
    private int mCurrentState = STATE_PULLTOREFRESH;

    private ImageView mImageView;
    private TextView tvState;
    private TextView tvTime;
    private ProgressBar pb;

    RotateAnimation animation;
    RotateAnimation animationDown;

    private boolean isLoadMore = false;



    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_torefresh, null);
        addHeaderView(mHeaderView);
        mImageView = mHeaderView.findViewById(R.id.iv_arrow);
        tvState = mHeaderView.findViewById(R.id.tv_state);
        tvTime = mHeaderView.findViewById(R.id.tv_time);
        pb = mHeaderView.findViewById(R.id.pb_loading);
        //控件没有绘制完成测量不了
        //int height = mHeaderView.getHeight();
        mHeaderView.measure(0,0);//手动测量， 不参与具体宽高设定，有系统设置
        height = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -height, 0 , 0);
        initArrowAnim();
        setRefreshTime();
    }


    int mFooterViewheight;
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.pull_torefresh_footer, null);
        addFooterView(mFooterView);
        mFooterView.measure(0,0);
        mFooterViewheight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, - mFooterViewheight ,0 ,0 );
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {//空闲状态
                    int lastVissiblePosition = getLastVisiblePosition();
                    if (lastVissiblePosition == getCount()-1 && !isLoadMore) {
                        isLoadMore = true;
                        mFooterView.setPadding(0,0,0,0);
                        setSelection(getCount()-1);
                        if (mListener != null) {
                            mListener.onLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private int startY = -1;
    private int endY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY= (int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {//按下事件被VIewpager 消费重写在获取
                    startY = (int) ev.getY();
                }
                endY = (int) ev.getY();
                int d = endY - startY;

                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }
                int position = this.getFirstVisiblePosition();
                if (d > 0 && position == 0) {
                    int padding = -height + d;
                    if (padding > 0 && mCurrentState != STATE_RELEASETOREFRESH) {
                        mCurrentState = STATE_RELEASETOREFRESH;
                        refreshState();
                    } else if (padding <=0 && mCurrentState != STATE_PULLTOREFRESH){
                        mCurrentState = STATE_PULLTOREFRESH;
                        refreshState();
                    }
                    mHeaderView.setPadding(0, padding, 0 , 0);
                    return true;//消费此事件
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASETOREFRESH) {
                    //切换成正在刷新
                    mCurrentState = STATE_REFRESHING;
                    mHeaderView.setPadding(0, 0, 0 ,0);
                    refreshState();
                    return true;
                } else if (mCurrentState == STATE_PULLTOREFRESH) {
                    //隐藏刷新控件
                    mHeaderView.setPadding(0, -height, 0 ,0);
                }
                break;
                default:
                    break;
        }
        return super.onTouchEvent(ev);
    }

    //根据当前状态切换
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULLTOREFRESH:
                tvState.setText("下拉刷新");
                pb.setVisibility(View.INVISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                mImageView.startAnimation(animationDown);
                break;
            case STATE_RELEASETOREFRESH:
                tvState.setText("松开刷新");
                pb.setVisibility(View.INVISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                mImageView.startAnimation(animation);
                break;
            case STATE_REFRESHING:
                tvState.setText("正在刷新...");
                pb.setVisibility(View.VISIBLE);

                mImageView.clearAnimation();//先要清理动画才能隐藏
                mImageView.setVisibility(View.INVISIBLE);

                if (mListener != null) {
                    mListener.onRefresh();
                }
                break;
                default:
                    break;
        }

    }


    private void initArrowAnim() {

        animation = new RotateAnimation(0, -180,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        animation.setFillAfter(true);


        animationDown = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animationDown.setDuration(300);
        animationDown.setFillAfter(true);
    }

    private OnRefreshListenter mListener;
    public void setOnRefreshListener(OnRefreshListenter listener) {
        mListener = listener;
    }

    public interface OnRefreshListenter {
        void onRefresh();
        void onLoadMore();
    }

    public void onRefreshComplete() {
        if (!isLoadMore) {
            mHeaderView.setPadding(0, -height, 0, 0);
            tvState.setText("下拉刷新");
            pb.setVisibility(View.INVISIBLE);
            mImageView.setVisibility(View.VISIBLE);
            mCurrentState = STATE_PULLTOREFRESH;
            setRefreshTime();
        } else {
            mFooterView.setPadding(0,-mFooterViewheight,0,0);
            isLoadMore = false;
        }
    }

    private void setRefreshTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tvTime.setText(time);
    }
}
