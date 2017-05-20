package com.lhd.mobileplayer4.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.domain.MediaItem;
import com.lhd.mobileplayer4.utils.Utils;

import java.util.List;

public class SystemVideoPlayer extends Activity implements View.OnClickListener
{
    private static final String TAG = SystemVideoPlayer.class.getSimpleName();
    private static final int PROGRESS = 1;
    private VideoView videoview_system;
    //视频集合
    private List<MediaItem> mediaItems;
    private int position;

    private Utils utils;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwichPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSiwchScreen;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-20 20:56:06 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );
        tvSystemTime = (TextView)findViewById( R.id.tv_system_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwichPlayer = (Button)findViewById( R.id.btn_swich_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvCurrentTime = (TextView)findViewById( R.id.tv_current_time );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvDuration = (TextView)findViewById( R.id.tv_duration );
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnVideoPre = (Button)findViewById( R.id.btn_video_pre );
        btnVideoStartPause = (Button)findViewById( R.id.btn_video_start_pause );
        btnVideoNext = (Button)findViewById( R.id.btn_video_next );
        btnVideoSiwchScreen = (Button)findViewById( R.id.btn_video_siwch_screen );

        btnVoice.setOnClickListener( this );
        btnSwichPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSiwchScreen.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-20 20:56:06 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            // Handle clicks for btnVoice
        } else if ( v == btnSwichPlayer ) {
            // Handle clicks for btnSwichPlayer
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
        } else if ( v == btnVideoPre ) {
            // Handle clicks for btnVideoPre
        } else if ( v == btnVideoStartPause ) {
            // Handle clicks for btnVideoStartPause
            if(videoview_system.isPlaying()) {
                //暂停
                videoview_system.pause();
                //改变按钮状态
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
            }
            else
            {
                //播放
                videoview_system.start();
                //改变按钮状态
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);

            }
        } else if ( v == btnVideoNext ) {
            // Handle clicks for btnVideoNext
        } else if ( v == btnVideoSiwchScreen ) {
            // Handle clicks for btnVideoSiwchScreen
        }
    }


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case PROGRESS :
                    //得到当前视频播放位置
                    int currentPosition = videoview_system.getCurrentPosition();
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));
                    seekbarVideo.setProgress(currentPosition);

                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        utils = new Utils();
        initData();
        initView();
        findViews();
        initListener();
    }

    private void initData()
    {
        Bundle bundle = getIntent().getExtras();
        mediaItems = (List<MediaItem>) bundle.getSerializable("videolist");
        position = getIntent().getIntExtra("position", 0);
        Log.e(TAG, "传入视频的数据"+ mediaItems.get(position));

    }

    private void initListener()
    {
        videoview_system.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                videoview_system.start();

                //获得总时长
                int duration = videoview_system.getDuration();

                tvDuration.setText(utils.stringForTime(duration));
                seekbarVideo.setMax(duration);

                handler.sendEmptyMessage(PROGRESS);

            }
        });
        videoview_system.setOnErrorListener(new MediaPlayer.OnErrorListener()
        {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra)
            {
                Toast.makeText(SystemVideoPlayer.this, "播放出错了", Toast.LENGTH_SHORT).show();
                return false;

            }
        });
        videoview_system.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                Toast.makeText(SystemVideoPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();
            }
        });
        String url = mediaItems.get(position).getData();
        Log.e(TAG, "url==" + url);
        videoview_system.setVideoPath(url);

//        videoview_system.setMediaController(new MediaController(this));

        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if(fromUser) {
                    videoview_system.seekTo(progress);
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
        });

    }

    private void initView()
    {
        videoview_system = (VideoView)findViewById(R.id.videoview_system);

    }
}
