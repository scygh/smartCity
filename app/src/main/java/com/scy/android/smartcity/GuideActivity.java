package com.scy.android.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import utiil.SpUtil;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.guide_view)
    ViewPager guideView;
    @BindView(R.id.guide_butt)
    Button guideButt;
    @BindView(R.id.guide_littlepoint_container)
    LinearLayout guideLittlepointContainer;
    @BindView(R.id.point_selected)
    ImageView pointSelected;

    int d = 0;

    private int[] imageDrawable = {R.drawable.self_guide_01, R.drawable.self_guide_02, R.drawable.self_guide_03};
    private ArrayList<ImageView> mImageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        View decorderView = getWindow().getDecorView();
        decorderView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        initData();
    }


    private void initData() {
        mImageViews = new ArrayList<>();
        for (int i = 0; i < imageDrawable.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageDrawable[i]);
            mImageViews.add(imageView);
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.gudie_point_normal);
            guideLittlepointContainer.addView(point);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                params.leftMargin = 25;
            }
            point.setLayoutParams(params);
        }
        guideView.setAdapter(new guideAdapter());

        guideView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int distance = (int)(d*v + 0.5f + i*d);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pointSelected.getLayoutParams();
                params.leftMargin = distance;
                pointSelected.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {
                if (i == mImageViews.size() - 1) {
                    guideButt.setVisibility(View.VISIBLE);
                } else {
                    guideButt.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        pointSelected.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                d = guideLittlepointContainer.getChildAt(1).getLeft() - guideLittlepointContainer.getChildAt(0).getLeft();
                pointSelected.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        guideButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.setIsFirstVisit(GuideActivity.this,true);
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class guideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = mImageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }
}
