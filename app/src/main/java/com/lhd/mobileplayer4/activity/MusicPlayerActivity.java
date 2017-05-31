package com.lhd.mobileplayer4.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lhd.mobileplayer4.IMusicPlayerService;
import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.service.MusicPlayerService;
import com.lhd.mobileplayer4.utils.LyricUtils;
import com.lhd.mobileplayer4.utils.Utils;
import com.lhd.mobileplayer4.view.BaseVisualizerView;
import com.lhd.mobileplayer4.view.ShowLyricView;

import java.io.File;

public class MusicPlayerActivity extends Activity implements View.OnClickListener
{

    private static final int SHOW_LYRIC = 1;
    private static final int PROGRESS = 2;
    private RelativeLayout rlTop;
    private ImageView ivIcon;
    private BaseVisualizerView baseVisualizerView;
    private TextView tvArtist;
    private TextView tvName;
    private LinearLayout llBottom;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnLyrc;
    private ShowLyricView showLyricView;



    private ServiceConnection conn = new ServiceConnection()
    {
        /**
         * 连接成功的时候回调
         * @param name
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder)
        {
            //服务的代理类
            service = IMusicPlayerService.Stub.asInterface(iBinder);
            if (service != null)
            {

                //从列表打开


                try
                {
                    if (!notification)
                    {
                        //从列表打开
                        service.openAudio(position);
                    }
                    else
                    {
                        //从任务栏进入
                        showData();
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();

                }
            }

        }


        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    };
    private IMusicPlayerService service;
    /**
     * 点击音频的位置
     */
    private int position;
    private MyReceiver receiver;
    private Utils utils;
    private boolean notification;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-29 10:51:56 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews()
    {
        rlTop = (RelativeLayout) findViewById(R.id.rl_top);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        //        baseVisualizerView = (BaseVisualizerView)findViewById( R.id.baseVisualizerView );
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvName = (TextView) findViewById(R.id.tv_name);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvTime = (TextView) findViewById(R.id.tv_time);
        seekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        btnAudioPlaymode = (Button) findViewById(R.id.btn_audio_playmode);
        btnAudioPre = (Button) findViewById(R.id.btn_audio_pre);
        btnAudioStartPause = (Button) findViewById(R.id.btn_audio_start_pause);
        btnAudioNext = (Button) findViewById(R.id.btn_audio_next);
        btnLyrc = (Button) findViewById(R.id.btn_lyrc);
        showLyricView = (ShowLyricView) findViewById(R.id.showLyricView);

        btnAudioPlaymode.setOnClickListener(this);
        btnAudioPre.setOnClickListener(this);
        btnAudioStartPause.setOnClickListener(this);
        btnAudioNext.setOnClickListener(this);
        btnLyrc.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-29 10:51:56 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */

    @Override
    public void onClick(View v)
    {
        if (v == btnAudioPlaymode)
        {
            // Handle clicks for btnAudioPlaymode
            setPlayMode();
        }
        else if (v == btnAudioPre)
        {
            // Handle clicks for btnAudioPre
            try
            {
                service.pre();
            }
            catch (RemoteException e)
            {
                e.printStackTrace();

            }
        }
        else if (v == btnAudioStartPause)
        {
            // Handle clicks for btnAudioStartPause
            try
            {
                if (service.isPlaying())
                {
                    try
                    {
                        service.pause();
                        //设置按钮状态
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();

                    }
                }
                else
                {
                    try
                    {
                        service.start();
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();

                    }


                }
            }
            catch (RemoteException e)
            {


            }
        }
        else if (v == btnAudioNext)
        {
            // Handle clicks for btnAudioNext
            try
            {
                service.next();
            }
            catch (RemoteException e)
            {
                e.printStackTrace();

            }
        }
        else if (v == btnLyrc)
        {
            // Handle clicks for btnLyrc
        }
    }

    /**
     * 设置播放模式
     */
    private void setPlayMode()
    {
        try
        {
            int playMode = service.getPlayMode();
            if (playMode == MusicPlayerService.REPEAT_NORMAL)
            {
                playMode = MusicPlayerService.REPEAT_ALL;
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                Toast.makeText(MusicPlayerActivity.this, "全部循环", Toast.LENGTH_SHORT).show();
            }
            else if (playMode == MusicPlayerService.REPEAT_ALL)
            {
                playMode = MusicPlayerService.REPEAT_SINGLE;
                btnAudioPlaymode.setBackgroundResource(R.drawable
                        .btn_audio_playmode_single_selector);
                Toast.makeText(MusicPlayerActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
            }
            else if (playMode == MusicPlayerService.REPEAT_SINGLE)
            {
                playMode = MusicPlayerService.REPEAT_NORMAL;
                btnAudioPlaymode.setBackgroundResource(R.drawable
                        .btn_audio_playmode_normal_selector);
                Toast.makeText(MusicPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
            }
            else
            {
                playMode = MusicPlayerService.REPEAT_NORMAL;
                btnAudioPlaymode.setBackgroundResource(R.drawable
                        .btn_audio_playmode_normal_selector);
                Toast.makeText(MusicPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
            }
            service.setPlayMode(playMode);

        }
        catch (RemoteException e)
        {
            e.printStackTrace();

        }
    }

    /**
     * 校验状态
     */
    private void checkPlaymode()
    {
        try
        {
            int playmode = service.getPlayMode();

            if (playmode == MusicPlayerService.REPEAT_NORMAL)
            {
                btnAudioPlaymode.setBackgroundResource(R.drawable
                        .btn_audio_playmode_normal_selector);
            }
            else if (playmode == MusicPlayerService.REPEAT_SINGLE)
            {
                btnAudioPlaymode.setBackgroundResource(R.drawable
                        .btn_audio_playmode_single_selector);
            }
            else if (playmode == MusicPlayerService.REPEAT_ALL)
            {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            }
            else
            {
                btnAudioPlaymode.setBackgroundResource(R.drawable
                        .btn_audio_playmode_normal_selector);
            }


            //校验播放和暂停的按钮
            if (service.isPlaying())
            {
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
            }
            else
            {
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }


    }

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case SHOW_LYRIC://显示歌词

                    //1.得到当前的进度
                    try
                    {
                        int currentPosition = service.getCurrentPosition();


                        //2.把进度传入ShowLyricView控件，并且计算该高亮哪一句

                        showLyricView.setshowNextLyric(currentPosition);
                        //3.实时的发消息
                        handler.removeMessages(SHOW_LYRIC);
                        handler.sendEmptyMessage(SHOW_LYRIC);
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }

                    break;
                case PROGRESS:
                    try
                    {
                        int currentPosition = service.getCurrentPosition();
                        seekbarAudio.setProgress(currentPosition);
                        //3.时间进度跟新
                        tvTime.setText(utils.stringForTime(currentPosition) + "/" + utils
                                .stringForTime(service.getDuration()));


                        //4.每秒更新一次
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);

                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();

                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        utils = new Utils();
        getData();
        initData();
        findViews();
        initListener();

        bindAndStartService();
    }

    /**
     * 设置监听
     */
    private void initListener()
    {
        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener

    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            if (fromUser)
            {
                if (service != null)
                {
                    try
                    {
                        service.SeekTo(progress);
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();

                    }
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {

        }
    }

    private void initData()
    {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayerService.OPENAUDIO);
        registerReceiver(receiver, filter);
    }

    private void getData()
    {
        notification = getIntent().getBooleanExtra("notification", false);
        if (!notification)
        {

            position = getIntent().getIntExtra("position", 0);
        }
    }

    /**
     * 绑定并启动服务
     */
    private void bindAndStartService()
    {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.lhd.mobileplayer4.OPENAUDIO");

        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);//不至于实例化多个服务
    }

    class MyReceiver extends BroadcastReceiver

    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            showData();
        }
    }

    /**
     * 显示数据
     */
    private void showData()
    {
        //发消息开始歌词同步
        showLyric();
        showViewData();
        checkPlaymode();
        //        setupVisualizerFxAndUi();
    }

    private void showViewData()
    {

        try
        {
            tvArtist.setText(service.getArtist());
            tvName.setText(service.getName());
            //设置进度条的最大值
            seekbarAudio.setMax(service.getDuration());

            //发消息
            handler.sendEmptyMessage(PROGRESS);

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

    }

    private void showLyric()
    {
        //解析歌词
        LyricUtils lyricUtils = new LyricUtils();

        try
        {
            String path = service.getAudioPath();//得到歌曲的绝对路径

            //传歌词文件
            //mnt/sdcard/audio/beijingbeijing.mp3
            //mnt/sdcard/audio/beijingbeijing.lrc
            path = path.substring(0, path.lastIndexOf("."));
            File file = new File(path + ".lrc");
            if (!file.exists())
            {
                file = new File(path + ".txt");
            }
            lyricUtils.readLyricFile(file);//解析歌词

            showLyricView.setLyrics(lyricUtils.getLyrics());

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }


        if (lyricUtils.isExistsLyric())
        {
            handler.sendEmptyMessage(SHOW_LYRIC);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(receiver!=null) {
            unregisterReceiver(receiver);
            receiver=null;
        }
        if(conn!=null) {
            unbindService(conn);
            conn = null;
        }
    }
}
