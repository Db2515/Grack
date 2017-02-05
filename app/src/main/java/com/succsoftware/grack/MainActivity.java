package com.succsoftware.grack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.*;
import java.io.*;


public class MainActivity extends AppCompatActivity {
    ArrayList<Song> queue = new ArrayList<Song>();
    Song current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        queue.add(new Song("https://soundcloud.com/majorleaguewobs/filthy-frank-theme"));

        queue.add(new Song("https://soundcloud.com/rlgrime/tell-me-rl-grime-what-so-not"));
        queue.add(new Song("https://soundcloud.com/chancetherapper/no-problem-feat-lil-wayne-2-chainz"));
        queue.add(new Song("spotify:track:2TpxZ7JUBn3uw46aR7qd6V"));
        //while(true){
            current = queue.remove(0);
            current.getDetails();
            switch (current.getType()){
                case 0:

                case 1:
                    mPlayer.playUri(null, current.getDomain());
                case 2:
                    myWebView.loadUrl(current.getDomain());
            }
        //}

    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.activity_main);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        current = queue.remove(0);
        current.getDetails();
        switch (current.getType()){
            case 0:

            case 1:

            case 2:
                myWebView.loadUrl(current.getDomain());
        }
    }


}
