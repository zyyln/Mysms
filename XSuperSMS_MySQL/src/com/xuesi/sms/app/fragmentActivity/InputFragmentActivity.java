package com.xuesi.sms.app.fragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.xuesi.sms.R;
import com.xuesi.sms.app.fragment.StackFragment;
import com.xuesi.sms.app.fragment.TopFragment;
import com.xuesi.sms.bean.StoreHouse;

/**
 * Created by XS-PC014 on 2017/11/8.
 */
public class InputFragmentActivity extends StackBaseFragmentActivity implements TopFragment.StoreOnItemClick {
    private StackFragment stackFragment;
    private String curStoreType = "FSK";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TopFragment topFragment = (TopFragment) getSupportFragmentManager().findFragmentById(R.id.id_top);
        topFragment.setContent(true, getString(R.string.input));
        topFragment.setCurStoreType(curStoreType);
        topFragment.setStoreOnItemClick(this);
    }

    @Override
    protected Fragment createLeftFragment() {
        return null;
    }

    @Override
    protected Fragment createRightFragment() {
        return null;
    }

    @Override
    public void storeOnItemClick(StoreHouse.ListClass curSelctStore) {
        if (stackFragment == null) {
            stackFragment = StackFragment.newInstance(curStoreType, curSelctStore);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.id_rightContent, stackFragment).commit();
        } else {
            stackFragment = (StackFragment) getSupportFragmentManager().findFragmentById(R.id.id_rightContent);
            stackFragment.updateStackNo(curStoreType, curSelctStore);
        }
    }
}
