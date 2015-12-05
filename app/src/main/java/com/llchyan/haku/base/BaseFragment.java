package com.llchyan.haku.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by LinLin on 2015/11/19
 */
public abstract class BaseFragment extends Fragment
{

    //第一步,Fragment和Activity建立关联的时候调用。
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    //第二步
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (isBindEventBusHere())
        {
//            EventBus.getDefault().register(this);
        }
        if (getArguments() != null)
        {
            getBundleExtras(getArguments());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (getContentViewLayoutID() != 0)
        {
            return inflater.inflate(getContentViewLayoutID(), container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //第四步
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView();
    }

    //第五步,当Activity中的onCreate方法执行完后调用。
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    //第六步
    @Override
    public void onStart()
    {
        super.onStart();
    }

    //第七步
    @Override
    public void onResume()
    {
        super.onResume();
    }

    //第八步
    @Override
    public void onPause()
    {
        super.onPause();
    }

    //第九步
    @Override
    public void onStop()
    {
        super.onStop();
    }

    //第十步,Fragment中的布局被移除时调用。
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    //第十一步
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (isBindEventBusHere())
        {
//            EventBus.getDefault().unregister(this);
        }
    }

    //第十二步,Fragment和Activity解除关联的时候调用。
    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    protected abstract void getBundleExtras(Bundle extras);

    protected abstract int getContentViewLayoutID();

    protected abstract void initView();

    /**
     * @return is bind eventBus
     */
    protected boolean isBindEventBusHere()
    {
        return false;
    }
}
