package com.com.teamupapps.searchsms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.teamupapps.searchsms.R;


public class FullscreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void keywordClicked(View view){
        Intent intent = new Intent(FullscreenActivity.this, MainActivty.class);
        startActivity(intent);
    }

    public void linkClicked(View view){
        Intent intent = new Intent (FullscreenActivity.this, SearchLinkActivity.class);
        startActivity(intent);
    }

    public void imageClicked(View view){
        Intent intent = new Intent (FullscreenActivity.this, SearchImageActivity.class);
        startActivity(intent);
    }
}
