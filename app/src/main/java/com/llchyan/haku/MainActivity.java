package com.llchyan.haku;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.llchyan.haku.base.BaseActivity;

import butterknife.Bind;

public class MainActivity extends BaseActivity
{
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

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
