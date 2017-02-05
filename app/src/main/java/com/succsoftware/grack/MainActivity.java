package com.succsoftware.grack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.succsoftware.grack.R;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import junit.framework.Assert;

import java.util.*;

public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    private static final String CLIENT_ID = "4b93839fe92543e6ac2b291ff440d17f";
    private static final String REDIRECT_URI = "grack://callback";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

    private WebView myWebView;
    private WebViewClient myWebViewClient = new WebViewClient();

    ArrayList<Song> queue = new ArrayList<>();
    Song current;
    public static final String EXTRA_MESSAGE = "com.succsoftware.grack.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addNotificationCallback(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        /*
        if(playerEvent == PlayerEvent.kSpPlaybackNotifyTrackChanged && !queue.isEmpty()){
            mPlayer.pause(null);
            myWebView.destroy();
            current = queue.remove(0);
            current.updateDetails();
            switch (current.getType()){
                case 0:
                    break;
                case 1:
                    String domain = current.getDomain();
                    mPlayer.playUri(null, domain,0,0);
                    break;
                case 2:
                    myWebView = (WebView) findViewById(R.id.webview);
                    myWebView.setWebViewClient(myWebViewClient);
                    WebSettings webSettings = myWebView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    myWebView.loadUrl(current.getDomain());
                break;
            }
        }*/
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(myWebViewClient);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        queue.add(new Song("spotify:track:4DGRyGjqrVYl7b0TMMnjUc"));
        queue.add(new Song("spotify:track:2TpxZ7JUBn3uw46aR7qd6V"));
        queue.add(new Song("https://soundcloud.com/majorleaguewobs/filthy-frank-theme"));
        queue.add(new Song("https://soundcloud.com/rlgrime/tell-me-rl-grime-what-so-not"));
        queue.add(new Song("spotify:track:7riu93M5VsO7kQdMuDwaIX"));
        queue.add(new Song("https://soundcloud.com/chancetherapper/no-problem-feat-lil-wayne-2-chainz"));
        current = queue.remove(0);
        current.updateDetails();
        switch (current.getType()) {
            case 0:
                break;
            case 1:
                String domain = current.getDomain();
                mPlayer.playUri(null, domain, 0, 0);
                break;
            case 2:
                myWebView.loadUrl(current.getDomain());
                break;
        }

    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);

    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.activity_main);
        mPlayer.pause(null);
        myWebView.destroy();
        if (!queue.isEmpty()) {
            current = queue.remove(0);
            current.updateDetails();
            switch (current.getType()) {
                case 0:
                    break;
                case 1:
                    mPlayer.playUri(null, current.getDomain(), 0, 0);
                    break;
                case 2:
                    myWebView = (WebView) findViewById(R.id.webview);
                    myWebView.setWebViewClient(myWebViewClient);
                    WebSettings webSettings = myWebView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    myWebView.loadUrl(current.getDomain());
                    break;
            }
        }
    }

    public void searchSpotify(View view) {
        Intent intent = new Intent(this, searchSpotify.class);
        EditText editText = (EditText) findViewById(R.id.search_bar);
        String searchTermSpotify = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, searchTermSpotify);
        startActivity(intent);

    }

    public void searchSoundcloud(View view) {
        Intent intent = new Intent(this, searchSoundcloud.class);
        EditText editText = (EditText) findViewById(R.id.search_bar);
        String searchTermSoundcloud = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, searchTermSoundcloud);
        startActivity(intent);
    }
}

