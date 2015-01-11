package com.com.teamupapps.searchsms;

import com.com.teamupapps.searchsms.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
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

}
