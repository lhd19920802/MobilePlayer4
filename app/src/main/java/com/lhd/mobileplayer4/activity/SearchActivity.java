package com.lhd.mobileplayer4.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.lhd.mobileplayer4.R;
import com.lhd.mobileplayer4.adapter.SearchAdapter;
import com.lhd.mobileplayer4.domain.SearchBean;
import com.lhd.mobileplayer4.utils.JsonParser;
import com.lhd.mobileplayer4.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SearchActivity extends Activity implements View.OnClickListener
{

    private EditText etInput;
    private ImageView ivVoice;
    private TextView tvSearch;
    private ListView listview;
    private ProgressBar progressBar;
    private TextView tvNodata;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private String encodeText;
    private SearchAdapter adapter;
    private List<SearchBean.ItemsBean> items;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-06-02 14:46:36 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews()
    {
        etInput = (EditText) findViewById(R.id.et_input);
        ivVoice = (ImageView) findViewById(R.id.iv_voice);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        listview = (ListView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvNodata = (TextView) findViewById(R.id.tv_nodata);

        ivVoice.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViews();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_voice:
                //                Toast.makeText(SearchActivity.this, "语音", Toast.LENGTH_SHORT)
                // .show();
                showDialog();
                break;
            case R.id.tv_search:
                //                Toast.makeText(SearchActivity.this, "搜索", Toast.LENGTH_SHORT)
                // .show();
                progressBar.setVisibility(View.VISIBLE);
                serarchText();
                break;
        }
    }

    /**
     * 搜索文字內容
     */
    private void serarchText()
    {
        String text = etInput.getText().toString().trim();
        if (!TextUtils.isEmpty(text))
        {
            if (items != null && items.size() > 0)
            {
                items.clear();
            }
            try
            {
                encodeText = URLEncoder.encode(text, "utf-8");
                getDataFromNet();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();

            }
        }
    }

    private void getDataFromNet()
    {
        RequestParams entity = new RequestParams(Url.SEARCH_URL + encodeText);
        x.http().get(entity, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e("TAG", "网络请求成功==" + result.toString());
                progressBar.setVisibility(View.GONE);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex)
            {
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFinished()
            {
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void processData(String json)
    {


        SearchBean searchBean = parseJson(json);
        //            Log.e("TAG", "解析成功==" + searchBean.getItems().get(1).getItemTitle());
        items = searchBean.getItems();
        if (items != null && items.size() > 0)
        {
        adapter = new SearchAdapter(this, items);


            listview.setAdapter(adapter);
            tvNodata.setVisibility(View.GONE);
        }

        else
        {

            adapter.notifyDataSetChanged();
            tvNodata.setVisibility(View.VISIBLE);
        }
    }

    private SearchBean parseJson(String json)
    {
        return new Gson().fromJson(json, SearchBean.class);

    }

    private void showDialog()
    {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");//普通话
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    class MyInitListener implements InitListener
    {

        @Override
        public void onInit(int i)
        {
            if (i != ErrorCode.SUCCESS)
            {
                Toast.makeText(SearchActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener
    {

        /**
         * @param recognizerResult
         * @param b                是否说话结束
         */
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b)
        {
            String result = recognizerResult.getResultString();
            Log.e("MainActivity", "result ==" + result);
            String text = JsonParser.parseIatResult(result);
            //解析好的
            Log.e("MainActivity", "text ==" + text);

            String sn = null;
            // 读取json结果中的sn字段
            try
            {
                JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                sn = resultJson.optString("sn");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();//拼成一句
            for (String key : mIatResults.keySet())
            {
                resultBuffer.append(mIatResults.get(key));
            }

            etInput.setText(resultBuffer.toString());
            etInput.setSelection(etInput.length());

        }

        /**
         * 出错了
         *
         * @param speechError
         */
        @Override
        public void onError(SpeechError speechError)
        {
            Log.e("MainActivity", "onError ==" + speechError.getMessage());

        }
    }

}
