package com.lhd.mobileplayer4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * Created by lihuaidong on 2017/5/22 20:14.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：自定义VideoView 实现屏幕的缩放
 */
public class MyVideoView extends VideoView
{
    public MyVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    /**
     *  设置视频的尺寸
     * @param videoWidth
     * @param videoHeight
     */
    public void setVideoSize(int videoWidth,int videoHeight)
    {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width=videoWidth;
        params.height = videoHeight;

        setLayoutParams(params);
    }
}
