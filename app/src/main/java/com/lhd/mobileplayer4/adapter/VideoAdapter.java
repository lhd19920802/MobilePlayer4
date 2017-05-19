package com.lhd.mobileplayer4.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.domain.MediaItem;
import com.lhd.mobileplayer4.utils.Utils;

import java.util.List;

/**
 * Created by lihuaidong on 2017/5/18 20:58.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class VideoAdapter extends BaseAdapter
{
    private final Context mContext;
    private final List<MediaItem> mediaItems;
    private final boolean isVideo;
    private Utils utils;

    public VideoAdapter(Context mContext, List<MediaItem> mediaItems, boolean isVideo)
    {
        this.mContext=mContext;
        this.mediaItems=mediaItems;
        this.isVideo = isVideo;
        utils = new Utils();
    }

    @Override
    public int getCount()
    {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mediaItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHoder viewHoder;
        if(convertView ==null){
            convertView = View.inflate(mContext, R.layout.item_video_fragment,null);
            viewHoder = new ViewHoder();
            viewHoder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHoder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHoder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHoder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

            convertView.setTag(viewHoder);
        }else{
            viewHoder = (ViewHoder) convertView.getTag();
        }

        //根据position得到列表中对应位置的数据
        MediaItem mediaItem = mediaItems.get(position);
        viewHoder.tv_name.setText(mediaItem.getName());
        viewHoder.tv_size.setText(Formatter.formatFileSize(mContext, mediaItem.getSize()));
        viewHoder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));

        if(!isVideo){
            //音频
            viewHoder.iv_icon.setImageResource(R.drawable.music_default_bg);
        }

        return convertView;
    }


    static class ViewHoder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_size;
    }
}
