package com.llchyan.haku;

import android.view.Menu;

import com.llchyan.haku.base.BaseActivity;

public class MainActivity extends BaseActivity
{


    @Override
    protected int getContentViewLayoutID()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void initView()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
