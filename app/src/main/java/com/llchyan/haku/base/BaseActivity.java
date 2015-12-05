package com.llchyan.haku.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.llchyan.haku.R;
import com.llchyan.rxlifecycle.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by LinLin on 2015/12/5.
 */
public abstract class BaseActivity extends RxAppCompatActivity
{
    // 上下文实例
    protected Context mContext;
    // 应用全局的实例
    public BaseApplication application;
    private View rootView;
    protected int mScreenWidth;// 获取屏幕分辨率宽度
    protected int mScreenHeight;
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //    setSupportActionBar(mToolbar);
    //    ActionBar actionBar = getSupportActionBar();
    //    if (null==actionBar)
    //    {
    //        return;
    //    }
    //    actionBar.setDisplayHomeAsUpEnabled(true);
    //    actionBar.setTitle("扫一扫");


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        application = (BaseApplication) this.getApplication();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        if (isBindEventBusHere())
        {
            //            EventBus.getDefault().register(this);
        }


        if (getContentViewLayoutID() != 0)
        {
            setContentView(getContentViewLayoutID());
        } else
        {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        getBundleExtras(getIntent().getExtras());
        initView();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        //        StatusBarCompat.compat(this);
        ButterKnife.bind(this);

        rootView = this.findViewById(android.R.id.content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        // set a custom navigation bar resource
        //        tintManager.setNavigationBarTintResource(Color.TRANSPARENT);
        tintManager.setNavigationBarTintResource(R.color.colorPrimaryDark);
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //        MobclickAgent.onResume(this);//友盟统计
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //        MobclickAgent.onPause(this);//友盟统计
    }


    @Override
    protected void onStop()
    {
        super.onStop();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (isBindEventBusHere())
        {
            //            EventBus.getDefault().unregister(this);
        }
        ButterKnife.unbind(this);

    }


    protected abstract
    @LayoutRes
    int getContentViewLayoutID();

    protected abstract void initView();


    protected boolean isBindEventBusHere()
    {
        return false;
    }

    protected void getBundleExtras(@NonNull Bundle extras)
    {
    }

    protected void showSnackbar(String msg)
    {
        if (null == rootView)
        {
            synchronized (this)
            {
                if (null == rootView)
                {
                    rootView = getWindow().getDecorView();
                }
            }
        }
        Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
