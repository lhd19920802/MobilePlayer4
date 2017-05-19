package com.lhd.mobileplayer4.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lihuaidong on 2017/5/18 19:55.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：各个fragment的基类
 */
public abstract class BaseFragment extends Fragment
{
    protected Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState)
    {
        return initView();

    }

    public abstract View initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initData()
    {

    }
}
