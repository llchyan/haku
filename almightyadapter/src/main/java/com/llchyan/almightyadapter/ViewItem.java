package com.llchyan.almightyadapter;

/**
 * recyclerview的item数据
 */
public class ViewItem
{

    public static final int DEFAULT_TYPE = 0;
    public static final int FIRST_TYPE = 1;
    public static final int SECOND_TYPE = 2;
    public static final int THREE_TYPE = 3;
    public static final int FOUR_TYPE = 4;
    public static final int FIVE_TYPE = 5;
    public static final int HEAD_TYPE = Integer.MIN_VALUE;
    public static final int FOOT_TYPE = Integer.MAX_VALUE;

    public Object model;
    public int viewType;
    public int nowPostion;

    public ViewItem(int viewType, Object model)
    {
        this.viewType = viewType;
        this.model = model;
    }

    public Object getModel()
    {
        return model;
    }

    public int getNowPostion()
    {
        return nowPostion;
    }

    public void setNowPostion(int nowPostion)
    {
        this.nowPostion = nowPostion;
    }

    @Override
    public String toString()
    {
        return "ViewItem{" +
                "model=" + model +
                ", nowPostion=" + nowPostion +
                '}';
    }
}