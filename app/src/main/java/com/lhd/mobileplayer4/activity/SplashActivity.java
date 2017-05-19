package com.lhd.mobileplayer4.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import com.lhd.mobileplayer4.R;

public class SplashActivity extends Activity
{

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startMainActivity();
            }
        }, 2000);
    }

    private boolean isEnter = false;

    private void startMainActivity()
    {
        if (!isEnter)
        {
            isEnter = true;
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.e("TAG", "event==" + event.getAction());
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
