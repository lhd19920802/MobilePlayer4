package com.lhd.mobileplayer4.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.lhd.mobileplayer4.R;

/**
 * Created by lihuaidong on 2017/5/21 14:38.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：电量变化的广播接收器
 */
public class MyBateryReceiver extends BroadcastReceiver
{

    private final ImageView ivBattery;


    public MyBateryReceiver(ImageView ivBattery)
    {
        this.ivBattery = ivBattery;
    }

    /**
     * 电量变化在intent中
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        int level = intent.getIntExtra("level", 0);
        setBattery(level);
    }

    private void setBattery(int level)
    {
        if (level <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }
}
