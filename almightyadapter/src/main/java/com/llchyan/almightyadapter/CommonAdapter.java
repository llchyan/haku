package com.llchyan.almightyadapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter
{
    private List<T> mDatas;
    private final int mItemLayoutId;

    public CommonAdapter(@NonNull List<T> mDatas, @LayoutRes int itemLayoutId)
    {
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final CommonViewHolder viewHolder = CommonViewHolder.get(convertView, parent, mItemLayoutId, position);
        convert(viewHolder, position, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(CommonViewHolder helper, int position, T item);


    public void add(int position, @NonNull T item)
    {
        if (position < 0)
            mDatas.add(0, item);
        else if (position > mDatas.size())
            mDatas.add(item);
        else
            mDatas.add(position, item);
        notifyDataSetChanged();
    }

    public void add(@NonNull T item)
    {
        mDatas.add(item);
        notifyDataSetChanged();
    }

    public void set(int position, @NonNull T item)
    {
        if (position < 0)
            mDatas.set(0, item);
        else if (position > mDatas.size())
            mDatas.add(item);
        else
            mDatas.set(position, item);
        notifyDataSetChanged();
    }

    public void addAll(int position, @NonNull List<T> list)
    {
        if (position < 0)
            mDatas.addAll(0, list);
        else if (position > mDatas.size())
            mDatas.addAll(list);
        else
            mDatas.addAll(position, list);
        notifyDataSetChanged();
    }

    public void addAll(@NonNull List<T> list)
    {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position)
    {
        if (mDatas.isEmpty())
            return;
        if (position >= 0 && position < mDatas.size())
            mDatas.remove(position);
        notifyDataSetChanged();
    }


    public void remove(@NonNull T item)
    {
        if (mDatas.isEmpty())
            return;
        int i = mDatas.indexOf(item);
        if (i < 0)
        {
            notifyDataSetChanged();
            return;
        }
        mDatas.remove(item);
        notifyDataSetChanged();
    }


    public void clear()
    {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> list)
    {
        clear();
        addAll(list);
    }
}