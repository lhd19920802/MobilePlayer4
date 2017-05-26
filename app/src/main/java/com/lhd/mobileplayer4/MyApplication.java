package com.lhd.mobileplayer4;

import android.app.Application;

import org.xutils.x;

/**
 * Created by lihuaidong on 2017/5/25 19:56.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
