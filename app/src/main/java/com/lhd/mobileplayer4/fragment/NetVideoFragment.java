package com.lhd.mobileplayer4.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.activity.SystemVideoPlayer;
import com.lhd.mobileplayer4.adapter.NetVideoAdapter;
import com.lhd.mobileplayer4.base.BaseFragment;
import com.lhd.mobileplayer4.domain.MediaItem;
import com.lhd.mobileplayer4.utils.CacheUtils;
import com.lhd.mobileplayer4.utils.Url;
import com.lhd.mobileplayer4.utils.Utils;
import com.lhd.mobileplayer4.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lihuaidong on 2017/5/18 19:58.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class NetVideoFragment extends BaseFragment
{
    private static final String TAG = NetVideoFragment.class.getSimpleName();
    private XListView listview;
    private TextView tv_nonet;
    private ProgressBar pb_loading;
    private List<MediaItem> mediaItems;
    private Utils utils;

    private SimpleDateFormat sd;
    /**
     * 标记加载更多是否成功
     */
    private boolean isLoadMore=false;
    private NetVideoAdapter adapter;

    @Override
    public View initView()
    {

        utils = new Utils();
        View view = View.inflate(mContext, R.layout.netvideo_pager, null);
        listview = (XListView) view.findViewById(R.id.listview);
        listview.setPullLoadEnable(true);
        listview.setXListViewListener(new MyIXListViewListener());
        tv_nonet = (TextView) view.findViewById(R.id.tv_nonet);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);


        return view;

    }


    @Override
    public void initData()
    {
        super.initData();
        Log.e(TAG, "网络视频的数据被初始化了");
        String saveJson = CacheUtils.getString(mContext, Url.NET_VIDEO_URL);
        if(!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();

    }

    private void onLoad() {
        listview.stopRefresh();
        listview.stopLoadMore();
        listview.setRefreshTime("刷新时间" + getSystemTime());
    }

    private String getSystemTime()
    {
        sd = new SimpleDateFormat("MM-dd HH:mm");
        return sd.format(new Date());
    }

    class MyIXListViewListener implements XListView.IXListViewListener

    {
        @Override
        public void onRefresh()
        {
            getDataFromNet();
            onLoad();
        }

        @Override
        public void onLoadMore()
        {
            getMoreDataFromNet();
            onLoad();
        }
    }

    /**
     * 加载更多的数据
     */
    private void getMoreDataFromNet()
    {
        RequestParams entity=new RequestParams(Url.NET_VIDEO_URL);
        x.http().get(entity, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e("TAG", "联网成功==" + result);
                CacheUtils.putString(mContext, Url.NET_VIDEO_URL, result);
                isLoadMore=true;
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                Log.e("TAG", "联网失败=="+ex.getMessage());
                isLoadMore=false;
            }

            @Override
            public void onCancelled(CancelledException cex)
            {

                isLoadMore=false;
            }

            @Override
            public void onFinished()
            {

                isLoadMore=false;
            }
        });
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener

    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent(mContext, SystemVideoPlayer.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("videolist", (Serializable) mediaItems);

            intent.putExtras(bundle);
            intent.putExtra("position", position-1);
            startActivity(intent);
        }
    }

    private void getDataFromNet()
    {
        RequestParams entity=new RequestParams(Url.NET_VIDEO_URL);
        x.http().get(entity, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e("TAG", "联网成功==" + result);
                CacheUtils.putString(mContext, Url.NET_VIDEO_URL, result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                Log.e("TAG", "联网失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex)
            {

            }

            @Override
            public void onFinished()
            {

            }
        });
    }

    /**
     * 解析json并显示
     * @param json
     */
    private void processData(String json)
    {

        if(!isLoadMore) {

            mediaItems =parseJson(json);
            Log.e(TAG, "mediaItems===" + mediaItems.get(1).getName());
            adapter = new NetVideoAdapter(mContext, mediaItems);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new MyOnItemClickListener());
            if(mediaItems!=null&& mediaItems.size()>0) {

                pb_loading.setVisibility(View.GONE);
            }
        }
        else
        {
            isLoadMore=false;
            List<MediaItem> moreData = parseJson(json);
            mediaItems.addAll(moreData);
            adapter.notifyDataSetChanged();
        }
    }

    private List<MediaItem> parseJson(String json)
    {

        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            List<MediaItem> mediaItems = new ArrayList<>();
            if(jsonArray!=null&&jsonArray.length()>0) {

                for (int i=0;i<jsonArray.length();i++)
                {
                    MediaItem mediaItem = new MediaItem();
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    if(jsonObject1!=null) {

                        mediaItem.setName(jsonObject1.optString("movieName"));
                        mediaItem.setCoverImg(jsonObject1.optString("coverImg"));
                        mediaItem.setData(jsonObject1.optString("hightUrl"));
                        mediaItem.setVideoTitle(jsonObject1.optString("videoTitle"));

                        mediaItems.add(mediaItem);
                    }
                }


            }
            return mediaItems;

        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }

        return null;
    }
}
