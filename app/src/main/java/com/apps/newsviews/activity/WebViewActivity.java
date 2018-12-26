package com.apps.newsviews.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.apps.newsviews.R;
import com.apps.newsviews.utility.ConstantKey;

public class WebViewActivity extends AppCompatActivity {

    private ProgressDialog progress;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        String url = getIntent().getStringExtra(ConstantKey.WEB_URL);
        if (url != null) {
            myWebView = (WebView) findViewById(R.id.web_view_id);
            startWebView(url);
        }
    }

    private void startWebView(String url) {

        WebSettings settings = myWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);

        progress = new ProgressDialog(this);
        //progress.setTitle(getResources().getString( R.string.loading_title));
        progress.setMessage(getResources().getString( R.string.progress));
        //progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        myWebView.loadUrl(url);
    }
}
