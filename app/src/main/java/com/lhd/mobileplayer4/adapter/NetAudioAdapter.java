package com.lhd.mobileplayer4.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.domain.NetAudioBean;
import com.lhd.mobileplayer4.utils.Utils;

import org.xutils.common.util.DensityUtil;
import org.xutils.x;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by lihuaidong on 2017/6/4 9:40.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：网络音乐的适配器
 */
public class NetAudioAdapter extends BaseAdapter
{

    /**
     * 视频
     */
    private static final int TYPE_VIDEO = 0;

    /**
     * 图片
     */
    private static final int TYPE_IMAGE = 1;

    /**
     * 文字
     */
    private static final int TYPE_TEXT = 2;

    /**
     * GIF图片
     */
    private static final int TYPE_GIF = 3;


    /**
     * 软件推广
     */
    private static final int TYPE_AD = 4;
    private final Context mContext;
    private final List<NetAudioBean.ListBean> list;

    private Utils utils;

    public NetAudioAdapter(Context mContext, List<NetAudioBean.ListBean> list)
    {
        this.mContext = mContext;
        this.list = list;
        utils = new Utils();
    }

    @Override
    public int getCount()
    {
        return list.size();
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
        int itemViewType = getItemViewType(position);
        ViewHolder viewHolder;
        if (convertView == null)
        {

            viewHolder = new ViewHolder();
            switch (itemViewType)
            {
                case TYPE_VIDEO://视频
                    convertView = View.inflate(mContext, R.layout.all_video_item, null);
                    //在这里实例化特有的
                    viewHolder.tv_play_nums = (TextView) convertView.findViewById(R.id
                            .tv_play_nums);
                    viewHolder.tv_video_duration = (TextView) convertView.findViewById(R.id
                            .tv_video_duration);
                    viewHolder.iv_commant = (ImageView) convertView.findViewById(R.id.iv_commant);
                    viewHolder.tv_commant_context = (TextView) convertView.findViewById(R.id
                            .tv_commant_context);
                    viewHolder.jcv_videoplayer = (JCVideoPlayer) convertView.findViewById(R.id
                            .jcv_videoplayer);
                    break;
                case TYPE_IMAGE://图片
                    convertView = View.inflate(mContext, R.layout.all_image_item, null);
                    viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id
                            .iv_image_icon);
                    break;
                case TYPE_TEXT://文字
                    convertView = View.inflate(mContext, R.layout.all_text_item, null);
                    break;
                case TYPE_GIF://gif
                    convertView = View.inflate(mContext, R.layout.all_gif_item, null);
                    viewHolder.iv_image_gif = (GifImageView) convertView.findViewById(R.id
                            .iv_image_gif);
                    break;
                case TYPE_AD://软件广告
                    convertView = View.inflate(mContext, R.layout.all_ad_item, null);
                    viewHolder.btn_install = (Button) convertView.findViewById(R.id.btn_install);
                    viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id
                            .iv_image_icon);
                    break;
            }

            switch (itemViewType)
            {
                case TYPE_VIDEO://视频
                case TYPE_IMAGE://图片
                case TYPE_TEXT://文字
                case TYPE_GIF://gif
                    //加载除开广告部分的公共部分视图
                    //user info
                    viewHolder.iv_headpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
                    viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    viewHolder.tv_time_refresh = (TextView) convertView.findViewById(R.id
                            .tv_time_refresh);
                    viewHolder.iv_right_more = (ImageView) convertView.findViewById(R.id.iv_right_more);
                    //bottom
                    viewHolder.iv_video_kind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
                    viewHolder.tv_video_kind_text = (TextView) convertView.findViewById(R.id
                            .tv_video_kind_text);
                    viewHolder.tv_shenhe_ding_number = (TextView) convertView.findViewById(R.id
                            .tv_shenhe_ding_number);
                    viewHolder.tv_shenhe_cai_number = (TextView) convertView.findViewById(R.id
                            .tv_shenhe_cai_number);
                    viewHolder.tv_posts_number = (TextView) convertView.findViewById(R.id
                            .tv_posts_number);
                    viewHolder.ll_download = (LinearLayout) convertView.findViewById(R.id.ll_download);

                    break;
            }


            //中间公共部分 -所有的都有
            viewHolder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);

            convertView.setTag(viewHolder);
        }

        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         * 绑定数据
         */
        NetAudioBean.ListBean listBean = list.get(position);
        switch (itemViewType) {
            case TYPE_VIDEO://视频
                bindData(viewHolder, listBean);
                //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
                viewHolder.jcv_videoplayer.setUp(listBean.getVideo().getVideo().get(0), listBean.getVideo().getThumbnail().get(0), null);
                viewHolder.tv_play_nums.setText(listBean.getVideo().getPlaycount() + "次播放");
                viewHolder.tv_video_duration.setText(utils.stringForTime(listBean.getVideo().getDuration() * 1000) + "");

                break;
            case TYPE_IMAGE://图片
                bindData(viewHolder, listBean);
                viewHolder.iv_image_icon.setImageResource(R.drawable.bg_item);
                int  height = listBean.getImage().getHeight()<= DensityUtil.getScreenHeight()*0.75?listBean.getImage().getHeight(): (int) (DensityUtil.getScreenHeight() * 0.75);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.getScreenWidth(),height);
                viewHolder.iv_image_icon.setLayoutParams(params);
                if(listBean.getImage() != null &&  listBean.getImage().getBig()!= null&&listBean.getImage().getBig().size() >0){
                    //                    x.image().bind(viewHolder.iv_image_icon, listBean.getImage().getBig().get(0));
                    Glide.with(mContext).load(listBean.getImage().getBig().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.iv_image_icon);
                }
                break;
            case TYPE_TEXT://文字
                bindData(viewHolder, listBean);
                break;
            case TYPE_GIF://gif
                bindData(viewHolder, listBean);
                System.out.println("listBean.getGif().getImages().get(0)" + listBean.getGif().getImages().get(0));
                Glide.with(mContext).load(listBean.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_image_gif);

                break;
            case TYPE_AD://软件广告
                break;
        }


        //设置文本
        viewHolder.tv_context.setText(listBean.getText());

        return convertView;
    }

    /**
     * 绑定头部尾部数据
     * @param viewHolder
     * @param listBean
     */
    private void bindData(ViewHolder viewHolder, NetAudioBean.ListBean listBean)
    {
        if(listBean.getU()!=null&& listBean.getU().getHeader()!=null&&listBean.getU().getHeader().get(0)!=null){
            x.image().bind(viewHolder.iv_headpic, listBean.getU().getHeader().get(0));
        }
        if(listBean.getU() != null&&listBean.getU().getName()!= null){
            viewHolder.tv_name.setText(listBean.getU().getName()+"");
        }

        viewHolder.tv_time_refresh.setText(listBean.getPasstime());

        //设置标签
        List<NetAudioBean.ListBean.TagsBean> tags = listBean.getTags();
        if (tags != null && tags.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < tags.size(); i++) {
                buffer.append(tags.get(i).getName() + " ");
            }
            viewHolder.tv_video_kind_text.setText(buffer.toString());
        }

        //设置点赞，踩,转发
        viewHolder.tv_shenhe_ding_number.setText(listBean.getUp());
        viewHolder.tv_shenhe_cai_number.setText(listBean.getDown() + "");
        viewHolder.tv_posts_number.setText(listBean.getForward()+"");
    }

    static class ViewHolder
    {
        //user_info
        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;
        //bottom
        ImageView iv_video_kind;
        TextView tv_video_kind_text;
        TextView tv_shenhe_ding_number;
        TextView tv_shenhe_cai_number;
        TextView tv_posts_number;
        LinearLayout ll_download;

        //中间公共部分 -所有的都有
        TextView tv_context;


        //Video
        //        TextView tv_context;
        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayer jcv_videoplayer;

        //Image
        ImageView iv_image_icon;
        //        TextView tv_context;

        //Text
        //        TextView tv_context;

        //Gif
        GifImageView iv_image_gif;
        //        TextView tv_context;

        //软件推广
        Button btn_install;
        //        TextView iv_image_icon;
        //TextView tv_context;
    }

    /**
     * 分类型的ListView 要重写两个方法
     */
    @Override
    public int getItemViewType(int position)
    {
        String type = list.get(position).getType();
        int itemViewType = -1;
        if ("video".equals(type))
        {
            itemViewType = TYPE_VIDEO;
        }
        else if ("image".equals(type))
        {
            itemViewType = TYPE_IMAGE;
        }
        else if ("text".equals(type))
        {
            itemViewType = TYPE_TEXT;
        }
        else if ("gif".equals(type))
        {
            itemViewType = TYPE_GIF;
        }
        else
        {
            itemViewType = TYPE_AD;//广播
        }
        return itemViewType;
    }

    @Override
    public int getViewTypeCount()
    {
        return 5;
    }
}
