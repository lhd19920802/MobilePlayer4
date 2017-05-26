package com.lhd.mobileplayer4.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.adapter.NetVideoAdapter;
import com.lhd.mobileplayer4.base.BaseFragment;
import com.lhd.mobileplayer4.domain.MediaItem;
import com.lhd.mobileplayer4.utils.CacheUtils;
import com.lhd.mobileplayer4.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
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
    private ListView listview;
    private TextView tv_nonet;
    private ProgressBar pb_loading;
    private List<MediaItem> mediaItems;
    @Override
    public View initView()
    {

        View view = View.inflate(mContext, R.layout.netvideo_pager,null);
        listview = (ListView) view.findViewById(R.id.listview);
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

        mediaItems = new ArrayList<>();
        parseJson(json);
        Log.e(TAG, "mediaItems==="+mediaItems.get(1).getMovieName());
        listview.setAdapter(new NetVideoAdapter(mContext, mediaItems));
        if(mediaItems!=null&& mediaItems.size()>0) {

            pb_loading.setVisibility(View.GONE);
        }
    }

    private void parseJson(String json)
    {

        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if(jsonArray!=null&&jsonArray.length()>0) {

                for (int i=0;i<jsonArray.length();i++)
                {
                    MediaItem mediaItem = new MediaItem();
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    if(jsonObject1!=null) {

                        mediaItem.setMovieName(jsonObject1.optString("movieName"));
                        mediaItem.setCoverImg(jsonObject1.optString("coverImg"));
                        mediaItem.setUrl(jsonObject1.optString("url"));
                        mediaItem.setVideoTitle(jsonObject1.optString("videoTitle"));

                        mediaItems.add(mediaItem);
                    }
                }


            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }

    }
}
