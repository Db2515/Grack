package com.succsoftware.grack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.succsoftware.grack.R;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.succsoftware.grack.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
