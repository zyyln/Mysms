package com.xuesi.sms.app.fragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.xuesi.sms.R;

/**
 * Created by XS-PC014 on 2017/11/6.
 */

public abstract class StackBaseFragmentActivity extends FragmentActivity {
    protected abstract Fragment createLeftFragment();

    protected abstract Fragment createRightFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stackbase);
        FragmentManager fm = getSupportFragmentManager();
        Fragment leftFragment = fm.findFragmentById(R.id.id_leftContent);
        Fragment rightFragment = fm.findFragmentById(R.id.id_rightContent);

//        if (leftFragment == null) {
//            leftFragment = createLeftFragment();
//            fm.beginTransaction().add(R.id.id_leftContent, leftFragment).commit();
//        }
//        if (rightFragment == null) {
//            rightFragment = createRightFragment();
//            fm.beginTransaction().add(R.id.id_rightContent, rightFragment).commit();
//        }
    }
}
