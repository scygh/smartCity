package com.scy.android.smartcity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.news_image_view)
    ImageView newsImageView;
    @BindView(R.id.coll)
    CollapsingToolbarLayout coll;
    @BindView(R.id.appBarl)
    AppBarLayout appBarl;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.fb)
    FloatingActionButton fb;
    private String url;
    private String imageurl;
    private String title;
    WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //StatusBarUtil.setStatusBar(this, toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        url = getIntent().getStringExtra("url");
        imageurl = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");

        coll.setTitle(title);
        Glide.with(this).load(imageurl).into(newsImageView);

        settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        /*settings.setBuiltInZoomControls(true);//放大缩小 不支持
        settings.setUseWideViewPort(true);//双击缩放*/


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //所有跳转强制在当前页面跳转，不跳游览器
                webview.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.INVISIBLE);
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });

        webview.loadUrl(url);


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeTextSize:
                showChooseDialog();
                break;
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return true;

    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("你好呀，丁诗仪");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("这是我的app分享测试");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.jl));
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("挺好的！");
        // 启动分享GUI
        oks.show(this);
    }



    private int p;
    private int mCurr =2;
    public void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体大小");

        String[] item = {"超大字体","大字体","正常字体"};

        builder.setSingleChoiceItems(item, mCurr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                p = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (p) {
                    case 0:
                        settings.setTextZoom(150);
                        break;
                    case 1:
                        settings.setTextZoom(120);
                        break;
                    case 2:
                        settings.setTextZoom(90);
                        break;
                        default:
                            break;
                }
                mCurr = p;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
        }
    }
}
