package com.scy.android.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import utiil.SpUtil;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_rela)
    RelativeLayout splashRela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        View decorderView = getWindow().getDecorView();
        decorderView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        /*RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1200);
        rotateAnimation.setFillAfter(true);*/
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0 ,1, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        AnimationSet set = new AnimationSet(false);
        //set.addAnimation(rotateAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        splashRela.setAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean a = SpUtil.getIsFirstVisit(SplashActivity.this);
                if (!a) {
                    Intent guideIntent2 = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(guideIntent2);
                } else {
                    Intent guideIntent1 = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(guideIntent1);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
