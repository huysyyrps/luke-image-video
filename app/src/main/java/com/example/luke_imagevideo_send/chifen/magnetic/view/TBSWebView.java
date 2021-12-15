package com.example.luke_imagevideo_send.chifen.magnetic.view;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

public class TBSWebView extends WebView {
    public TBSWebView(Context context) {
        super(context);
        initView(context);
    }
    public TBSWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }
    private void initView(Context context) {
        //开启js脚本支持
        getSettings().setJavaScriptEnabled(true);
        //适配手机大小
        getSettings().setUseWideViewPort(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setSupportZoom(false);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        setWebViewClient(new WVClient());
    }
    /**
     * 使WebView不可滚动
     * */
    @Override
    public void scrollTo(int x, int y){
        super.scrollTo(0,0);
    }
//    private class WVClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            //在当前Activity打开
//            view.loadUrl(url);
//            return true;
//        }
//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            //https忽略证书问题
//            handler.proceed();
//        }
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//        }
//    }
}
