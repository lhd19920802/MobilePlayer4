package com.lhd.mobileplayer4.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.domain.MediaItem;

import java.util.List;

public class SystemVideoPlayer extends Activity
{
    private static final String TAG = SystemVideoPlayer.class.getSimpleName();
    private VideoView videoview_system;
    //视频集合
    private List<MediaItem> mediaItems;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        initData();
        initView();
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
            }
        });
        videoview_system.setOnErrorListener(new MediaPlayer.OnErrorListener()
        {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra)
            {
                return false;

            }
        });
        videoview_system.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {

            }
        });
        String url = mediaItems.get(position).getData();
        Log.e(TAG, "url=="+url);
        videoview_system.setVideoPath(url);

    }

    private void initView()
    {
        videoview_system = (VideoView)findViewById(R.id.videoview_system);

    }
}
