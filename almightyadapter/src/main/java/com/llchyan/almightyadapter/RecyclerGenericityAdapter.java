package com.llchyan.almightyadapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.List;

/**
 * Created by LinLin on 2015/10.30 带泛型的RecyclerAdapter
 */
public abstract class RecyclerGenericityAdapter<T> extends RecyclerView.Adapter<RecyclerHolder>
{
    /**
     * 数据源
     */
    private List<T> mDatas;
    /**
     * 布局集合
     */
    private int layoutId;
    /**
     * 默认item type
     */
    protected static final int DEFAULT_TYPE = 0;

    private RecyclerGenericityAdapter()
    {
        throw new IllegalStateException("不允许建立对象");
    }


    public RecyclerGenericityAdapter(@NonNull List<T> mDatas, @LayoutRes int layoutId)
    {
        this.mDatas = mDatas;
        this.layoutId = layoutId;
    }


    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return DEFAULT_TYPE;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return RecyclerHolder.get(parent.getContext(), parent, layoutId);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position)
    {
        T T = mDatas.get(position);
        if (null == T)
        {
            KLog.e("position==" + position + ",数据为空!");
            return;
        }
        convert(holder, T, position);
    }

    public abstract void convert(RecyclerHolder holder, T item, int position);

    public void addItem(int position,@NonNull T item)
    {
        if (position < 0)
        {
            mDatas.add(0, item);
            notifyItemInserted(0);
            return;
        }
        if (position > mDatas.size())
        {
            mDatas.add(item);
            notifyItemInserted(mDatas.size() - 1);
            return;
        }
        mDatas.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(T item)
    {
        mDatas.add(item);
        notifyItemInserted(mDatas.size() - 1);
    }

    public void changeItem(int position,@NonNull T item)
    {
        if (position < 0)
        {
            mDatas.add(0, item);
            notifyItemInserted(0);
            return;
        }
        if (position > mDatas.size())
        {
            position = mDatas.size();
            mDatas.add(position, item);
            notifyItemInserted(position);
            return;
        }
        mDatas.set(position, item);
        notifyItemChanged(position);
    }

    public void addAll(int position,@NonNull List<T> list)
    {
        if (position < 0)
        {
            mDatas.addAll(0, list);
            notifyItemRangeInserted(0, list.size());
            return;
        }
        if (position > mDatas.size())
        {
            int size = mDatas.size();
            mDatas.addAll(list);
            notifyItemRangeInserted(size, list.size());
            return;
        }
        mDatas.addAll(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    public void addAll(List<T> list)
    {
        addAll(getItemCount(), list);
    }

    public void remove(int position)
    {
        if (mDatas.isEmpty())
            return;
        if (position < 0 || position >= mDatas.size())
        {
            notifyDataSetChanged();
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
    }


    public void remove(@NonNull T item)
    {
        if (mDatas.isEmpty())
            return;
        int position = mDatas.indexOf(item);
        if (position < 0)
        {
            notifyDataSetChanged();
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void clearAll()
    {
        if (mDatas.isEmpty())
            return;
        Iterator<T> iterator = mDatas.iterator();
        int size = getItemCount();
        while (iterator.hasNext())
        {
            iterator.next();
            iterator.remove();
        }
        notifyItemRangeRemoved(0, size);
    }

    public void replaceAll(List<T> list)
    {
        if (null == list)
            return;
        mDatas.clear();
        notifyDataSetChanged();
        mDatas.addAll(list);
        notifyItemRangeInserted(0, list.size());
    }

    public List<T> getDataList()
    {
        return mDatas;
    }
}
