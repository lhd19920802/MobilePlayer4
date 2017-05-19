package com.lhd.mobileplayer4.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lhd.mobileplayer4.base.BaseFragment;

/**
 * Created by lihuaidong on 2017/5/18 19:58.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class AudioFragment extends BaseFragment
{
    private static final String TAG = AudioFragment.class.getSimpleName();
    private TextView textView;
    @Override
    public View initView()
    {
        textView = new TextView(mContext);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        return textView;

    }

    @Override
    public void initData()
    {
        super.initData();
        textView.setText("我是本地音乐");
        Log.e(TAG, "本地音乐的数据被初始化了");
    }
}
