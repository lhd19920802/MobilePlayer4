package com.lhd.mobileplayer4.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.domain.SearchBean;

import org.xutils.x;

import java.util.List;

/**
 * Created by lihuaidong on 2017/6/2 16:05.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：搜索的适配器
 */
public class SearchAdapter extends BaseAdapter
{

    private final Context mContext;
    /**
     * 请求到的数据
     */
    private final List<SearchBean.ItemsBean> items;

    public SearchAdapter(Context mContext, List<SearchBean.ItemsBean> items)
    {
        this.mContext=mContext;
        this.items = items;
    }

    @Override
    public int getCount()
    {
        return items.size();
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
        if(convertView ==null){
            convertView = View.inflate(mContext, R.layout.item_search,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据position得到列表中对应位置的数据
        SearchBean.ItemsBean itemsBean = items.get(position);
        viewHolder.tv_name.setText(itemsBean.getItemTitle());
        viewHolder.tv_desc.setText(itemsBean.getKeywords());
        //1.使用xUtils3请求图片
                x.image().bind(viewHolder.iv_icon,itemsBean.getItemImage().getImgUrl1());
        //2.使用Glide请求图片
        //        Glide.with(context).load(mediaItem.getImageUrl())
        //                .diskCacheStrategy(DiskCacheStrategy.ALL)
        //                .placeholder(R.drawable.video_default)
        //                .error(R.drawable.video_default)
        //                .into(ViewHolder.iv_icon);

//        //3.使用Picasso 请求图片
//        Picasso.with(context).load(mediaItem.getItemImage().getImgUrl1())
//                //                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.video_default)
//                .error(R.drawable.video_default)
//                .into(ViewHolder.iv_icon);


        return convertView;
    }

    static class ViewHolder

    {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }
}
