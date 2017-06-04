package com.lhd.mobileplayer4.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.adapter.NetAudioAdapter;
import com.lhd.mobileplayer4.base.BaseFragment;
import com.lhd.mobileplayer4.domain.NetAudioBean;
import com.lhd.mobileplayer4.utils.CacheUtils;
import com.lhd.mobileplayer4.utils.Url;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by lihuaidong on 2017/5/18 19:58.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：网络音乐
 */
public class NetAudioFragment extends BaseFragment
{
    private static final String TAG = NetAudioFragment.class.getSimpleName();
    private ListView listview;
    private TextView tv_nonet;
    private ProgressBar pb_loading;

    @Override
    public View initView()
    {
        View view =View.inflate(mContext, R.layout.net_audio_fragment,null);
        listview = (ListView) view.findViewById(R.id.listview);
        tv_nonet = (TextView) view.findViewById(R.id.tv_nonet);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);

        return view;

    }

    @Override
    public void initData()
    {
        super.initData();
        Log.e(TAG, "网络音乐的数据被初始化了");
        String saveJson = CacheUtils.getString(mContext, Url.ALL_RES_URL);
        if(!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();
    }

    private void getDataFromNet()
    {
        RequestParams entity=new RequestParams(Url.ALL_RES_URL);
        x.http().get(entity, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e(TAG, "请求数据成功==" + result);
                CacheUtils.putString(mContext,Url.ALL_RES_URL,result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                Log.e(TAG, "请求数据失败==" + ex.getMessage());
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

    private void processData(String json)
    {
        NetAudioBean netAudioBean=parseJson(json);
        Log.e(TAG, "数据解析成功==" + netAudioBean.getList().get(1).getText());
        List<NetAudioBean.ListBean> list = netAudioBean.getList();
        if(list!=null&&list.size()>0) {
         listview.setAdapter(new NetAudioAdapter(mContext, list));
            tv_nonet.setVisibility(View.GONE);
        }
        else
        {
            tv_nonet.setVisibility(View.VISIBLE);

        }
        pb_loading.setVisibility(View.GONE);
    }

    /**
     * 解析json
     * @param json
     * @return
     */

    private NetAudioBean parseJson(String json)
    {
        return new Gson().fromJson(json,NetAudioBean.class);

    }
}
