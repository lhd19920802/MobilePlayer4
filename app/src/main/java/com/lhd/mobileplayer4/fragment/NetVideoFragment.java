package com.lhd.mobileplayer4.fragment;

import android.util.Log;
import android.view.View;

import com.lhd.mobileplayer4.base.BaseFragment;

/**
 * Created by lihuaidong on 2017/5/18 19:58.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class NetVideoFragment extends BaseFragment
{
    private static final String TAG = NetVideoFragment.class.getSimpleName();
    @Override
    public View initView()
    {

        return null;

    }

    @Override
    public void initData()
    {
        super.initData();
        Log.e(TAG, "网络视频的数据被初始化了");
    }
}
