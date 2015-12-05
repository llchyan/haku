package com.llchyan.almightyadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by LinLin on 2015/7/18. RecyclerHolder
 */
public class RecyclerHolder extends RecyclerView.ViewHolder
{
    private SparseArray<View> mViews = null;
    private View mConvertView;
    private static final String TAG = RecyclerHolder.class.getSimpleName();

    private RecyclerHolder(View itemView)
    {
        super(itemView);
        this.mConvertView = itemView;
        this.mViews = new SparseArray<>();
    }

    private RecyclerHolder(Context context, ViewGroup parent, int layoutId)
    {
        this(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    public static RecyclerHolder get(Context context, ViewGroup parent, int layoutId)
    {
        return new RecyclerHolder(context, parent, layoutId);
    }

    public static RecyclerHolder get(View itemView)
    {
        return new RecyclerHolder(itemView);
    }

    public View getConvertView()
    {
        return mConvertView;
    }


    /**
     *
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
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     */
    public RecyclerHolder setText(int viewId, String text)
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
    public RecyclerHolder setImageResource(int viewId, int drawableId)
    {
        ImageView view = getView(viewId);
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
    public RecyclerHolder setImageBitmap(int viewId, Bitmap bmp)
    {
        ImageView view = getView(viewId);
        if (null == view || null == bmp)
        {
            KLog.e("获取ImageView失败,viewId==0x" + Integer.toHexString(viewId));
            return this;
        }
        view.setImageBitmap(bmp);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public RecyclerHolder setImageFresco(int viewId, String url)
    {
        final SimpleDraweeView view = getView(viewId);
        if (null == view || TextUtils.isEmpty(url))
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
    public RecyclerHolder setOnClickListener(int viewId, View.OnClickListener listener)
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
    public RecyclerHolder setOnConvertViewClickListener(View.OnClickListener listener)
    {
        mConvertView.setOnClickListener(listener);
        return this;
    }

    public RecyclerHolder setVisibility(int viewId, int visibility)
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
