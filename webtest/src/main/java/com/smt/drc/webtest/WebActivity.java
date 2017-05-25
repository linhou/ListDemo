package com.smt.drc.webtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web= (WebView) findViewById(R.id.webview);
        web.loadUrl("https://www.oschina.net/question/173667_26515");
    }
}
