package com.lhd.mobileplayer4.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.domain.MediaItem;

import org.xutils.x;

import java.util.List;

/**
 * Created by lihuaidong on 2017/5/25 20:07.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：网络视频的适配器
 */
public class NetVideoAdapter extends BaseAdapter
{
    private final Context mContext;
    private final List<MediaItem> mediaItems;


    public NetVideoAdapter(Context mContext, List<MediaItem> mediaItems)
    {
        this.mContext=mContext;
        this.mediaItems=mediaItems;
    }

    @Override
    public int getCount()
    {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if(convertView==null)
        {
            convertView = View.inflate(mContext, R.layout.net_video_item, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MediaItem mediaItem = mediaItems.get(position);
        x.image().bind(viewHolder.iv_icon,mediaItem.getCoverImg());
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_desc.setText(mediaItem.getVideoTitle());

        return convertView;
    }

    static class ViewHolder

    {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }
}
