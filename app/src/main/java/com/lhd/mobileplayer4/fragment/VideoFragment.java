package com.lhd.mobileplayer4.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.activity.SystemVideoPlayer;
import com.lhd.mobileplayer4.adapter.VideoAdapter;
import com.lhd.mobileplayer4.base.BaseFragment;
import com.lhd.mobileplayer4.domain.MediaItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihuaidong on 2017/5/18 19:58.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class VideoFragment extends BaseFragment
{
    private static final String TAG = VideoFragment.class.getSimpleName();
    private ListView listview;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;

    //视频信息的集合
    private List<MediaItem> mediaItems;

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (mediaItems != null && mediaItems.size() > 0)
            {
                listview.setAdapter(new VideoAdapter(mContext, mediaItems, true));

                tv_nomedia.setVisibility(View.GONE);
            }
            else
            {
                tv_nomedia.setVisibility(View.VISIBLE);
            }

            pb_loading.setVisibility(View.GONE);
        }
    };

    @Override
    public View initView()
    {
        View view = View.inflate(mContext, R.layout.video_fragment, null);
        listview = (ListView) view.findViewById(R.id.listview);
        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        //设置ListView的Item的点击事件
        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;

    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener

    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            MediaItem mediaItem = mediaItems.get(position);
            Log.e(TAG, "mediaItem=="+mediaItem.toString());
//            //调起所有的视频播放器
//            Intent intent=new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
//            mContext.startActivity(intent);
            //2.单个视频
//            Intent intent=new Intent(mContext,SystemVideoPlayer.class);
//            intent.setData(Uri.parse(mediaItem.getData()));
//            mContext.startActivity(intent);


            //3.传递列表数据-对象-序列化
            Intent intent=new Intent(mContext,SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", (Serializable) mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position",position);
            mContext.startActivity(intent);
        }
    }

    @Override
    public void initData()
    {
        super.initData();
        Log.e(TAG, "本地视频的数据被初始化了");
        //加载本地视频数据
        getDataFromLocal();
    }

    /**
     * 从本地的sdcard得到数据
     * //1.遍历sdcard,后缀名
     * //2.从内容提供者里面获取视频
     * //3.如果是6.0的系统，动态获取读取sdcard的权限
     */
    private void getDataFromLocal()
    {

        new Thread()
        {
            @Override
            public void run()
            {
                super.run();

                //                isGrantExternalRW((Activity) context);
                //                SystemClock.sleep(2000);
                mediaItems = new ArrayList<>();
                ContentResolver resolver = mContext.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Video.Media.DURATION,//视频总时长
                        MediaStore.Video.Media.SIZE,//视频的文件大小
                        MediaStore.Video.Media.DATA,//视频的绝对地址
                        MediaStore.Video.Media.ARTIST,//歌曲的演唱者

                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null)
                {
                    while (cursor.moveToNext())
                    {

                        MediaItem mediaItem = new MediaItem();

                        mediaItems.add(mediaItem);//写在上面

                        String name = cursor.getString(0);//视频的名称
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);//视频的时长
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);//视频的文件大小
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);//视频的播放地址
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);//艺术家
                        mediaItem.setArtist(artist);


                    }

                    cursor.close();


                }


                //Handler发消息
                handler.sendEmptyMessage(10);


            }
        }.start();

    }
}
