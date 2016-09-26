package com.example.pkota.nytnews.Activities;

/**
 * Created by pkota on 14-09-2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.pkota.nytnews.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String url = extras.getString("url");
        webView = (WebView) findViewById(R.id.webView );
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        this.finish();
    }
}
