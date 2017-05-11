package com.ktds.queuing_app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;

import com.ktds.queuing_app.Util.Index;

public class SplashActivity extends ActionBarActivity {
    private Handler handler;
    private ImageView ivSplash;

    private int i;
    private Drawable drawableList[];

    private String excuteType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        excuteType = getIntent().getStringExtra("executeType");
        handler = new Handler();

        ivSplash = (ImageView) findViewById(R.id.ivSplash);
        drawableList = new Drawable[] {
                getResources().getDrawable(R.drawable.camel1),
                getResources().getDrawable(R.drawable.camel2),
                getResources().getDrawable(R.drawable.camel3),
                getResources().getDrawable(R.drawable.camel4),
                getResources().getDrawable(R.drawable.camel5),
                getResources().getDrawable(R.drawable.camel6)};

        new Thread(new Runnable() {

            int delayTime = 0;

            @Override
            public void run() {
                Log.d("DDD", drawableList.length + "");

                for ( int i = 0; i < drawableList.length; i++ ) {
                    final Index tmpIndex = new Index();
                    tmpIndex.setIndex(i);

                    delayTime = i * 200;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DDD", tmpIndex.getIndex() + " / " + SystemClock.currentThreadTimeMillis());
                            ivSplash.setImageDrawable(drawableList[tmpIndex.getIndex()]);
                        }
                    }, delayTime);

                }
                viewNextActivity(delayTime);
            }
        }).start();
    }

    private void viewNextActivity(int delayTime) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (excuteType != null && excuteType.equals("beacon")) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    // 비콘과 연동이 되어있다면 MainActivity로 설정이 되어있어야한다.
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, delayTime + 200);
    }
}