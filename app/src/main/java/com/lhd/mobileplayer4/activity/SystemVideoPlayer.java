package com.lhd.mobileplayer4.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.domain.MediaItem;
import com.lhd.mobileplayer4.receiver.MyBateryReceiver;
import com.lhd.mobileplayer4.utils.Utils;
import com.lhd.mobileplayer4.view.MyVideoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SystemVideoPlayer extends Activity implements View.OnClickListener
{
    private static final String TAG = SystemVideoPlayer.class.getSimpleName();
    private static final int PROGRESS = 1;
    private static final int SYSTEM_TIME = 2;
    private static final int HIDE_MEDIA_CONTROLLER = 3;
    private static final int DEFAULT_SCREEN = 1;
    private static final int FULL_SCREEN = 2;
    private MyVideoView videoview_system;
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


    private LinearLayout ll_loading;
    /**
     * 电量变化的广播
     */
    private BroadcastReceiver receiver;
    /**
     * 单个的视频地址
     */
    private Uri uri;
    /**
     * 手势识别器
     */
    private GestureDetector detector;

    /**
     * 控制面板
     */
    private RelativeLayout media_controller;
    //屏幕的宽高
    private int screenWidth;
    private int screenHeight;
    private int videoWidth;
    private int videoHeight;

    private AudioManager audioManager;
    private int currentVolume;
    private int maxVolume;

    /**
     * 判断是否是网络资源 网络资源在handler里面为其设置缓冲进度
     */
    private boolean isNetUri;
    /**
     * 是否使用自己定义的判断视频卡
     */
    private boolean isUseSystem=false;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-20 20:56:06 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews()
    {
        videoview_system = (MyVideoView) findViewById(R.id.videoview_system);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnSwichPlayer = (Button) findViewById(R.id.btn_swich_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnVideoPre = (Button) findViewById(R.id.btn_video_pre);
        btnVideoStartPause = (Button) findViewById(R.id.btn_video_start_pause);
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);
        btnVideoSiwchScreen = (Button) findViewById(R.id.btn_video_siwch_screen);


        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

        media_controller = (RelativeLayout) findViewById(R.id.media_controller);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        ll_loading.setVisibility(View.GONE);

        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener()
        {
            /**
             * 长按
             * @param e
             */
            @Override
            public void onLongPress(MotionEvent e)
            {
                super.onLongPress(e);
                //                Toast.makeText(SystemVideoPlayer.this, "我被长按了", Toast
                // .LENGTH_SHORT).show();
                startAndPause();
            }

            /**
             * 双击
             * @param e
             * @return
             */
            @Override
            public boolean onDoubleTap(MotionEvent e)
            {
                //                Toast.makeText(SystemVideoPlayer.this, "我被双击了", Toast
                // .LENGTH_SHORT).show();
                isFullOrDefault();
                return super.onDoubleTap(e);
            }

            /**
             * 单击
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e)
            {
                //                Toast.makeText(SystemVideoPlayer.this, "我被单击了", Toast
                // .LENGTH_SHORT).show();


                if (isshowMediaController)
                {
                    //隐藏
                    hideMediaController();
                    //把隐藏消息移除
                    handler.removeMessages(HIDE_MEDIA_CONTROLLER);
                }
                else
                {
                    //显示
                    showMediaController();
                    //发消息隐藏
                    handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });

        btnVoice.setOnClickListener(this);
        btnSwichPlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnVideoSiwchScreen.setOnClickListener(this);


    }

    //默认为隐藏
    private boolean isshowMediaController = false;


    /**
     * 显示控制面板
     */
    private void showMediaController()
    {
        media_controller.setVisibility(View.VISIBLE);
        isshowMediaController = true;
    }


    /**
     * 隐藏控制面板
     */
    private void hideMediaController()
    {
        media_controller.setVisibility(View.GONE);
        isshowMediaController = false;
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-20 20:56:06 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v)
    {
        if (v == btnVoice)
        {
            // Handle clicks for btnVoice
//            isMute = !isMute;
            updateVolume(currentVolume,true);

        }
        else if (v == btnSwichPlayer)
        {
            // Handle clicks for btnSwichPlayer
        }
        else if (v == btnExit)
        {
            // Handle clicks for btnExit
            finish();
        }
        else if (v == btnVideoPre)
        {
            // Handle clicks for btnVideoPre
            playPreVideo();
        }
        else if (v == btnVideoStartPause)
        {
            // Handle clicks for btnVideoStartPause
            startAndPause();
        }
        else if (v == btnVideoNext)
        {
            // Handle clicks for btnVideoNext
            playNextVideo();
        }
        else if (v == btnVideoSiwchScreen)
        {
            // Handle clicks for btnVideoSiwchScreen
            isFullOrDefault();
        }

        handler.removeMessages(HIDE_MEDIA_CONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 2000);
    }

    private void isFullOrDefault()
    {
        if (isFullScreen)
        {
            //设置为默认
            setVideoType(DEFAULT_SCREEN);
        }
        else
        {
            //设置为全屏
            setVideoType(FULL_SCREEN);
        }
    }

    private void startAndPause()
    {
        if (videoview_system.isPlaying())
        {
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
    }


    private int prePosition;
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case PROGRESS:
                    //得到当前视频播放位置
                    int currentPosition = videoview_system.getCurrentPosition();
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));
                    seekbarVideo.setProgress(currentPosition);

                    if(isNetUri)
                    {
                        int bufferPercentage = videoview_system.getBufferPercentage();

                        int secondaryProgress = bufferPercentage*seekbarVideo.getMax();
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    }else
                    {
                        seekbarVideo.setSecondaryProgress(0);
                    }

                    if(!isUseSystem) {

                        int dPosition=currentPosition-prePosition;
                        if(dPosition<5000&&videoview_system.isPlaying()) {
                            //卡了
                            ll_loading.setVisibility(View.GONE);

                        }
                        else{
                            //卡結束
                            ll_loading.setVisibility(View.VISIBLE);
                        }
                        prePosition=currentPosition;
                    }


                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
                case SYSTEM_TIME:
                    String systemTime = getSystemTime();

                    tvSystemTime.setText(systemTime);
                    handler.removeMessages(SYSTEM_TIME);
                    handler.sendEmptyMessageDelayed(SYSTEM_TIME, 1000);
                    break;

                case HIDE_MEDIA_CONTROLLER:
                    hideMediaController();
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
        findViews();

        initData();
        getData();
        setData();
        initListener();

    }

    private void getData()
    {
//        Log.e(TAG, "传入视频的数据" + mediaItems.get(position));
        uri = getIntent().getData();//文件夹，图片浏览器，QQ空间
        Log.e(TAG, "uri==" + uri.toString());
//        Bundle bundle = getIntent().getExtras();
//        mediaItems = (List<MediaItem>) bundle.getSerializable("videolist");
        mediaItems = (List<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
    }

    private void initData()
    {



        //        注册监听电量变化广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);//监听电量变化

        receiver = new MyBateryReceiver(ivBattery);
        registerReceiver(receiver, filter);

        //显示并更新系统时间

        handler.sendEmptyMessage(SYSTEM_TIME);

        setButtonState();


        //得到视频的宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;


    }

    /**
     * 是否是全屏模式
     */

    private boolean isFullScreen = false;

    /**
     * 设置屏幕的大小
     *
     * @param screenSize
     */
    private void setVideoType(int screenSize)
    {
        switch (screenSize)
        {
            case FULL_SCREEN:
                //视频设置为屏幕大小

                videoview_system.setVideoSize(screenWidth, screenHeight);
                //设置按钮的状态
                btnVideoSiwchScreen.setBackgroundResource(R.drawable
                        .btn_video_siwch_screen_default_selector);
                isFullScreen = true;
                break;
            case DEFAULT_SCREEN:
                //视频等比例拉伸

                //视频真实的宽高
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;

                //屏幕的宽高
                int height = screenHeight;
                int width = screenWidth;
                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight)
                {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                }
                else if (mVideoWidth * height > width * mVideoHeight)
                {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                //修改后的width,height设置到自定义的VideoView中
                videoview_system.setVideoSize(width, height);
                //设置按钮的状态
                btnVideoSiwchScreen.setBackgroundResource(R.drawable
                        .btn_video_siwch_screen_full_selector);
                isFullScreen = false;
                break;

        }
    }


    //得到系统时间
    private String getSystemTime()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(new Date());
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

                seekbarVoice.setMax(maxVolume);

                handler.sendEmptyMessage(PROGRESS);

                hideMediaController();

                //                videoview_system.setVideoSize(100, 100);
                //                videoview_system.setVideoSize(mp.getVideoWidth(), mp
                // .getVideoHeight());


                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                Log.e(TAG, "videoWidth==" + videoWidth + " videoHeight==" + videoHeight);


                videoview_system.setVideoSize(videoWidth, videoHeight);

                //设置屏幕为默认尺寸
                setVideoType(DEFAULT_SCREEN);
            }
        });
        videoview_system.setOnErrorListener(new MediaPlayer.OnErrorListener()
        {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra)
            {
                Toast.makeText(SystemVideoPlayer.this, "播放出错了", Toast.LENGTH_SHORT).show();
                //出错后不弹出对话框
                return true;

            }
        });
        videoview_system.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                Toast.makeText(SystemVideoPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();

                playNextVideo();
            }
        });
//        String url = mediaItems.get(position).getData();
//        Log.e(TAG, "url==" + url);
//        videoview_system.setVideoPath(url);

        //        videoview_system.setMediaController(new MediaController(this));

        //视频进度拖动条
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                {
                    videoview_system.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //移除隐藏控制面板的消息
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //发消息
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 2000);
            }
        });

        //声音拖动条
        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                {
                    updateVolume(progress,false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 2000);
            }
        });

        if(isUseSystem) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                videoview_system.setOnInfoListener(new MediaPlayer.OnInfoListener()
                {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra)
                    {
                        switch (what)
                        {
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                //                            Toast.makeText(SystemVideoPlayer.this, "卡了", Toast.LENGTH_SHORT).show();
                                ll_loading.setVisibility(View.VISIBLE);
                                break;
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                //                            Toast.makeText(SystemVideoPlayer.this, "卡结束了", Toast.LENGTH_SHORT).show();
                                ll_loading.setVisibility(View.GONE);
                                break;
                        }
                        return false;
                    }
                });
            }
        }

    }

    private void setData()
    {

        if (mediaItems != null && mediaItems.size() > 0)
        {
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());//设置视频的名称
            isNetUri = utils.isNetUri(mediaItem.getData());
            videoview_system.setVideoPath(mediaItem.getData());

        }
        else if (uri != null)
        {
            tvName.setText(uri.toString());//设置视频的名称
            isNetUri = utils.isNetUri(uri.toString());
            videoview_system.setVideoURI(uri);
        }
        else
        {
            Toast.makeText(SystemVideoPlayer.this, "帅哥你没有传递数据", Toast.LENGTH_SHORT).show();
        }
        setButtonState();
    }

//    private boolean isMute = false;

    /**
     * 更新声音进度条
     */
    private void updateVolume(int volume,boolean isMute)
    {
        if (isMute)
        {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);
        }
        else
        {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            seekbarVoice.setProgress(volume);
        }

        currentVolume = volume;
    }

    /**
     * 播放上一个视频
     */
    private void playPreVideo()
    {
        if (mediaItems != null && mediaItems.size() > 0)
        {
            //播放上一个视频
            position--;
            if (position >= 0)
            {
                //                ll_loading.setVisibility(View.INVISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
                videoview_system.setVideoPath(mediaItem.getData());

                //                设置按钮状态
                setButtonState();
            }
        }
        else if (uri != null)
        {
            //设置按钮状态-上一个和下一个按钮设置灰色并且不可以点击
            setButtonState();
        }
    }

    /**
     * 播放下一个视频
     */
    private void playNextVideo()
    {
        if (mediaItems != null && mediaItems.size() > 0)
        {
            //播放下一个
            position++;
            if (position < mediaItems.size())
            {
                //                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
                videoview_system.setVideoPath(mediaItem.getData());

                //设置按钮状态
                setButtonState();
            }
        }
        else if (uri != null)
        {
            //设置按钮状态-上一个和下一个按钮设置灰色并且不可以点击
            setButtonState();
        }
    }

    /**
     * 设置按钮的状态
     */
    private void setButtonState()
    {
        if (mediaItems != null && mediaItems.size() > 0)
        {
            if (mediaItems.size() == 1)
            {
                btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnVideoPre.setEnabled(false);
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnVideoNext.setEnabled(false);
            }
            //             else if(mediaItems.size()==2) {
            //                 if(position==0) {
            //                     //            btnVideoPre.setBackgroundResource(R.drawable
            // .btn_pre_pressed);
            //                     btnVideoPre.setEnabled(false);
            //                     //             btnVideoNext.setBackgroundResource(R.drawable
            // .btn_next_pressed);
            //                     btnVideoNext.setEnabled(true);
            //                 }else if(position==1) {
            //                     //            btnVideoPre.setBackgroundResource(R.drawable
            // .btn_pre_pressed);
            //                     btnVideoPre.setEnabled(true);
            //                     //             btnVideoNext.setBackgroundResource(R.drawable
            // .btn_next_pressed);
            //                     btnVideoNext.setEnabled(false);
            //                 }
            //             }
            else
            {
                if (position == 0)
                {

                    btnVideoPre.setEnabled(false);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoNext.setEnabled(true);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                }
                else if (position == mediaItems.size() - 1)
                {
                    btnVideoPre.setEnabled(true);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoNext.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);

                }
                else
                {
                    btnVideoPre.setEnabled(true);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoNext.setEnabled(true);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                }
            }
        }
        else if (uri != null)
        {
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoPre.setEnabled(false);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
        }
    }

    private float startY;
    private float startX;
    /**
     * 屏幕的高
     */
    private float touchRang;

    /**
     * 当一按下的音量
     */
    private int mVol;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //把事件传递给手势识别器进行解析
        detector.onTouchEvent(event);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN://手指按下
                //1.按下记录值
                startY = event.getY();
                startX = event.getX();
                mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRang = Math.min(screenHeight, screenWidth);//screenHeight
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);

                break;
            case MotionEvent.ACTION_MOVE://手指移动
                //2.移动的记录相关值
                float endY = event.getY();
                float endX = event.getX();
                float distanceY = startY - endY;

                if (endX < screenWidth / 2)
                {
                    //左边屏幕-调节亮度
                    final double FLING_MIN_DISTANCE = 0.5;
                    final double FLING_MIN_VELOCITY = 0.5;
                    if (distanceY > FLING_MIN_DISTANCE && Math.abs(distanceY) > FLING_MIN_VELOCITY)
                    {
                        //                        Log.e(TAG, "up");
                        setBrightness(20);
                    }
                    if (distanceY < FLING_MIN_DISTANCE && Math.abs(distanceY) > FLING_MIN_VELOCITY)
                    {
                        //                        Log.e(TAG, "down");
                        setBrightness(-20);
                    }
                }
                else
                {
                    //右边屏幕-调节声音
                    //改变声音 = （滑动屏幕的距离： 总距离）*音量最大值
                    float delta = (distanceY / touchRang) * maxVolume;
                    //最终声音 = 原来的 + 改变声音；
                    int voice = (int) Math.min(Math.max(mVol + delta, 0), maxVolume);
                    if (delta != 0)
                    {
//                        isMute = false;
                        updateVolume(voice,false);
                    }

                }


                //                startY = event.getY();//不要加
                break;
            case MotionEvent.ACTION_UP://手指离开
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 2000);
                break;
        }
        return super.onTouchEvent(event);
    }

    private Vibrator vibrator;

    /*
    *
    * 设置屏幕亮度 lp = 0 全暗 ，lp= -1,根据系统设置， lp = 1; 最亮
    */
    public void setBrightness(float brightness)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // if (lp.screenBrightness <= 0.1) {
        // return;
        // }
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1)
        {
            lp.screenBrightness = 1;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = {10, 200}; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, -1);
        }
        else if (lp.screenBrightness < 0.2)
        {
            lp.screenBrightness = (float) 0.2;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = {10, 200}; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, -1);
        }
        //        Log.e(TAG, "lp.screenBrightness= " + lp.screenBrightness);
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            currentVolume--;
            updateVolume(currentVolume,false);
            showMediaController();
            handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 2000);
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            currentVolume++;
            updateVolume(currentVolume,false);
            showMediaController();
            handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 2000);
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }
}
