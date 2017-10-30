package sms.xuesi.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import sms.xuesi.com.myview.ScrollingImageView;

/**
 * Created by XS-PC014 on 2017/10/24.
 */

public class ScrollActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        ScrollingImageView scrollingBackground = (ScrollingImageView) findViewById(R.id.scrolling_background);
//        scrollingBackground.stop();
        scrollingBackground.start();
        ScrollingImageView scrollingForeground = (ScrollingImageView) findViewById(R.id.scrolling_foreground);
//        scrollingBackground.stop();
        scrollingForeground.start();
    }
}
