package com.water.roll.activity;

import android.content.Intent;

import com.water.roll.BaseActivity;

/**
 * Created by xujiashuai@jovision.com on 2016/5/27.
 */
public abstract class TabActivity extends BaseActivity {

    public void onBackPressed() {
        this.getParent().onBackPressed();
    }

    public void onTabResume() {
    }

    @Override
    protected void onResume() {
        onTabResume();
        super.onResume();
    }

    public void onTabPause() {
    }

    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public abstract String getTabTag();

}
