package com.water.roll.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.water.roll.BaseActivity;
import com.water.roll.R;

public class LiveActivity extends BaseActivity implements OnClickListener{

    private static final String TAG = LiveActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

    }

    @Override
    public void onClick(View view) {

    }
}
