package com.smt.drc.webtest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
//    private final  String URL="http://www.tuicool.com/articles/IVfmuyZ";
//    private final  String URL="http://www.yujingceping.com/login.html";
    private final  String URL="http://www.yujingceping.com/yitaifront/pages/front/myCenter/person.html#/evaluation-query?ts=1495696035410 ";
    private String TAG="MainActivity";
    private String APP_CACHE_DIRNAME="/webcache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView= (WebView) findViewById(R.id.web);
        findView();
    }

    private void findView() {
        initWebView();

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.i(TAG, "onLoadResource url=" + url);

                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "intercept url=" + url);
                view.loadUrl(url);
                return true;
            }

            // 页面开始时调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成调用
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                Log.e(TAG, "onJsAlert " + message);

                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {
                Log.e(TAG, "onJsConfirm " + message);
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                Log.e(TAG, "onJsPrompt " + url);
                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }
        });

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);   //在当前的webview中跳转到新的url

                return true;
            }
        });
        mWebView.loadUrl(URL);
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()
                + APP_CACHE_DIRNAME;
        Log.i("cachePath", cacheDirPath);
        // 设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath); // API 19 deprecated
        // 设置Application caches缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);

        Log.i("databasepath", mWebView.getSettings().getDatabasePath());
    }

    private void initSetting() {
//        WebSettings webseting  =  webView .getSettings();
////        webseting.setAppCacheMaxSize(1024*1024*8);//设置缓冲大小，我设的是8M
////        String  appCacheDir  =  this .getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
////        webseting.setAppCachePath(appCacheDir);
////        webseting.setAllowFileAccess(true);
////        webseting.setAppCacheEnabled(true);
////        webseting.setCacheMode(WebSettings.LOAD_DEFAULT);
////        webView.setWebChromeClient(m_chromeClient);
//
//        // 开启javascript设置
//        webseting.setJavaScriptEnabled( true );
//        // 设置可以使用localStorage
//        webseting.setDomStorageEnabled( true );
//        // 应用可以有数据库
//        webseting.setDatabaseEnabled( true );
//        String dbPath = this.getApplicationContext().getDir( "database" , Context.MODE_PRIVATE).getPath();
//        webseting.setDatabasePath(dbPath);
//        // 应用可以有缓存
//        webseting.setAppCacheEnabled(true);
//        String appCaceDir = this .getApplicationContext().getDir( "cache" , Context.MODE_PRIVATE).getPath();
//        webseting.setAppCachePath(appCaceDir);
////        webView.setWebViewClient(new WebViewClient(){
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////
////                view.loadUrl("https://www.oschina.net/question/173667_26515");   //在当前的webview中跳转到新的url
////
////                return true;
////            }
////        });
//       webView.loadUrl( "http://blog.csdn.net/wwj_748/article/details/44835865" );
////       webView.loadUrl( "https://www.oschina.net/question/173667_26515" );

    }
}
