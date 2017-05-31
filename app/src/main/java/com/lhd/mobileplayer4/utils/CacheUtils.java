package com.lhd.mobileplayer4.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lihuaidong on 2017/5/26 14:36.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：数据缓存工具类
 */
public class CacheUtils
{

    public static void putString(Context mContext, String key, String value)
    {
        SharedPreferences sp = mContext.getSharedPreferences("net_video", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
    public static String getString(Context mContext,String key)
    {
        SharedPreferences sp = mContext.getSharedPreferences("net_video", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void putInt(Context mContext, String key, int value)
    {
        SharedPreferences sp = mContext.getSharedPreferences("audio", Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }
    public static int getInt(Context mContext,String key)
    {
        SharedPreferences sp = mContext.getSharedPreferences("audio", Context.MODE_PRIVATE);
        return sp.getInt(key,1);
    }
}
