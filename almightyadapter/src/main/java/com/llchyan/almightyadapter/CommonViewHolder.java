package com.llchyan.almightyadapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

public class CommonViewHolder
{
    private final SparseArray<View> mViews;
    private View mConvertView;

    private CommonViewHolder(ViewGroup parent, int layoutId, int position)
    {
        this.mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     */
    public static CommonViewHolder get(View convertView, ViewGroup parent, int layoutId, int position)
    {
        CommonViewHolder holder = null;
        try
        {
            if (null == convertView)
            {
                holder = new CommonViewHolder(parent, layoutId, position);
            } else
            {
                holder = (CommonViewHolder) convertView.getTag();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            KLog.e("CommonViewHolder异常 layoutId" + layoutId);
        }
        return holder;
    }

    public View getConvertView()
    {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if (null == view)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        if (null == view)
        {
            KLog.e("获取View失败,viewId==0x" + Integer.toHexString(viewId));
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     */
    public CommonViewHolder setText(int viewId, String text)
    {
        TextView view = getView(viewId);
        if (null == view)
        {
            KLog.e("获取TextView失败,viewId==0x" + Integer.toHexString(viewId));
            return this;
        }
        if (TextUtils.isEmpty(text))
            text = "";
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public CommonViewHolder setImageResource(int viewId, int drawableId)
    {
        android.widget.ImageView view = getView(viewId);
        if (null == view)
        {
            KLog.e("获取ImageView失败,viewId==0x" + Integer.toHexString(viewId));
            return this;
        }
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public CommonViewHolder setImageBitmap(int viewId, Bitmap bm)
    {
        android.widget.ImageView view = getView(viewId);
        if (null == view)
        {
            KLog.e("获取ImageView失败,viewId==0x" + Integer.toHexString(viewId));
            return this;
        }
        view.setImageBitmap(bm);
        return this;
    }

    public CommonViewHolder setImageFresco(int viewId, String url)
    {
        final SimpleDraweeView view = getView(viewId);
        if (view == null || TextUtils.isEmpty(url))
        {
            KLog.e("获取SimpleDraweeView失败,viewId==0x" + Integer.toHexString(viewId));
            return this;
        }
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url));
        view.setController(builder.setAutoPlayAnimations(url.endsWith(".gif") || url.endsWith(".webp")).build());
        return this;
    }

    /**
     * 设置控件点击事件
     */
    public CommonViewHolder setOnClickListener(int viewId, View.OnClickListener listener)
    {
        final View view = getView(viewId);
        if (null == view)
        {
            KLog.e("获取view失败,viewId==0x" + Integer.toHexString(viewId));
            return this;
        }
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置父容器点击事件
     */
    public CommonViewHolder setOnConvertViewClickListener(View.OnClickListener listener)
    {
        mConvertView.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置控件隐藏
     */
    public CommonViewHolder setVisibility(int viewId, int visibility)
    {
        final View view = getView(viewId);
        if (null == view)
        {
            KLog.e("获取view失败,viewId==0x" + Integer.toHexString(viewId));
            return this;
        }
        view.setVisibility(visibility);
        return this;
    }
}
