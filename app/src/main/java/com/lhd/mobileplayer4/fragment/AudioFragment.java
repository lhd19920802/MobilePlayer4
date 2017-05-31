package com.lhd.mobileplayer4.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import com.lhd.mobileplayer4.activity.MusicPlayerActivity;
import com.lhd.mobileplayer4.adapter.VideoAdapter;
import com.lhd.mobileplayer4.base.BaseFragment;
import com.lhd.mobileplayer4.domain.MediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihuaidong on 2017/5/18 19:58.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class AudioFragment extends BaseFragment
{
    private static final String TAG = AudioFragment.class.getSimpleName();
    private ListView listview;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;

    /**
     * 音乐对象的集合
     *
     */
    private List<MediaItem> mediaItems;
    @Override
    public View initView()
    {
        View view = View.inflate(mContext, R.layout.audio_fragment, null);
        listview = (ListView) view.findViewById(R.id.listview);
        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);

        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;

    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener

    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent(mContext, MusicPlayerActivity.class);

            intent.putExtra("position", position);
            startActivity(intent);
        }
    }
    @Override
    public void initData()
    {
        super.initData();
        Log.e(TAG, "本地音乐的数据被初始化了");
        //加载本地音频
        getDataFromLocal();
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(mediaItems != null && mediaItems.size() >0){
                //有数据
                //设置适配器
                VideoAdapter adapter = new VideoAdapter(mContext,mediaItems,false);
                listview.setAdapter(adapter);
                //把文本隐藏
                tv_nomedia.setVisibility(View.GONE);
            }else{
                //没有数据
                //文本显示
                tv_nomedia.setVisibility(View.VISIBLE);
                tv_nomedia.setText("没有发现音频....");
            }


            //ProgressBar隐藏
            pb_loading.setVisibility(View.GONE);
        }
    };
    private void getDataFromLocal()
    {
       new Thread(){
           public void run()
           {
               mediaItems = new ArrayList<MediaItem>();
               ContentResolver resolver = mContext.getContentResolver();
               Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

               String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                       MediaStore.Audio.Media.DURATION,//视频总时长
                       MediaStore.Audio.Media.SIZE,//视频的文件大小
                       MediaStore.Audio.Media.DATA,//视频的绝对地址
                       MediaStore.Audio.Media.ARTIST,//歌曲的演唱者
               };
               Cursor cursor = resolver.query(uri, projection, null, null, null);
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
               }

               cursor.close();
               handler.sendEmptyMessage(10);
           }
       }.start();
    }
}
