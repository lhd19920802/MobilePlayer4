package com.lhd.mobileplayer4.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.base.BaseFragment;
import com.lhd.mobileplayer4.fragment.AudioFragment;
import com.lhd.mobileplayer4.fragment.NetAudioFragment;
import com.lhd.mobileplayer4.fragment.NetVideoFragment;
import com.lhd.mobileplayer4.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity
{

    private FrameLayout fl_main_content;
    private RadioGroup rg_bottom_tag;
    private int selectPosition;
    private List<BaseFragment> fragments;
    //上一次fragment
    private BaseFragment preFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
        rg_bottom_tag.check(R.id.rb_video);
        
    }

    private void initView()
    {
        fl_main_content = (FrameLayout)findViewById(R.id.fl_main_content);
        rg_bottom_tag = (RadioGroup)findViewById(R.id.rg_bottom_tag);
        fragments = new ArrayList<>();
        initFragments();
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    private void initFragments()
    {
        fragments.add(new VideoFragment());
        fragments.add(new AudioFragment());
        fragments.add(new NetVideoFragment());
        fragments.add(new NetAudioFragment());
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener
        
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            switch (checkedId) {
                case R.id.rb_video :
                    selectPosition=0;
                    break;
                case R.id.rb_audio :
                    
                    selectPosition=1;
                    break;
                case R.id.rb_net_video :
                    selectPosition=2;
                    
                    break;
                case R.id.rb_netaudio :
                    selectPosition=3;
                    
                    break;
            }
            BaseFragment fragment = fragments.get(selectPosition);
            switchFragment(preFragment,fragment);
        }
    }

    private void switchFragment(BaseFragment tempFragment, BaseFragment fragment)
    {
        if (tempFragment != fragment)
        {
            preFragment = fragment;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!fragment.isAdded())
            {
                //隐藏tempFragment
                if (tempFragment != null)
                {
                    transaction.hide(tempFragment);
                }
                //添加新的fragment 并提交
                if (fragment != null)
                {
                    transaction.add(R.id.fl_main_content, fragment).commit();
                }


            }
            else
            {//隐藏tempFragment
                if (tempFragment != null)
                {
                    transaction.hide(tempFragment);
                }
                //显示新的fragment 并提交
                if (fragment != null)
                {
                    transaction.show(fragment).commit();
                }


            }
        }

    }

    /**
     * 是否已经退出
     */
    private boolean isExit = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            if(selectPosition != 0){//不是第一页面
                selectPosition = 0;
                rg_bottom_tag.check(R.id.rb_video);//首页
                return true;
            }else  if(!isExit){
                isExit = true;
                Toast.makeText(MainActivity.this, "再按一次推出", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit  = false;
                    }
                },2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
