package com.kaidongyuan.app.kdydriver.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;


import com.kaidongyuan.app.kdydriver.bean.order.Notify;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 咨询通知 Activity
 */
public class NotifyActivity extends BaseActivity implements AsyncHttpCallback {

    private SlidingTitleView titleView;
    private TextView tv_content;
    private TextView tv_title;
    private TextView tv_info;
    private static final String Tag_Detail = "Tag_Detail";
    private OrderAsyncHttpClient client;
    //    private static final String TEST = "<p style=\\\"text-indent:28px;\\\"><span style=\\\"color:#FF0000;\\\"><b>特价1：购买100桶怡宝纯净水（13元/桶）赠送20桶（仅限部分商业用户）；</b></span></p><span style=\\\"font-size:14px;\\\"><p style=\\\"text-indent:28px;\\\"><span style=\\\"color:#FF0000;\\\"><b>特价2：购买 50桶怡宝纯净水（13元/桶）赠送 5桶（仅限部分商业用户）；</b></span></p></span>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        titleView = (SlidingTitleView) findViewById(R.id.title_notify);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_info = (TextView) findViewById(R.id.tv_info);
        titleView.setMode(SlidingTitleView.MODE_NULL);
        findViewById(R.id.btn_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleView.setText("资讯信息");
        if (getIntent()!=null&&getIntent().hasExtra("id")){
            client = new OrderAsyncHttpClient(this, this);
            Map<String, String> params = new HashMap<>();
            params.put("strLicense", "");
            params.put("strIDX",getIntent().getStringExtra("id"));
            client.sendRequest(Constants.URL.GetMessageDetils, params, Tag_Detail);
        }



//		mWebView.loadDataWithBaseURL(null, source, "text/html", "utf-8", null);
        /*if (url != null) mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                cancelLoadingDialog();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                cancelLoadingDialog();
            }
        });

        WebSettings settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setJavaScriptEnabled(true);*/
    }

    @Override
    protected void onDestroy() {
        client.cancleRequest(Tag_Detail);
        super.onDestroy();

    }

    @Override
    public void postSuccessMsg(String msg, String request_tag) {
        if (msg.equals("error")){
            tv_content.setText("数据丢失，请返回重载~");
            return;
        }

        if (request_tag.equals(Tag_Detail)) {
            JSONObject jo = JSON.parseObject(msg);
            List<Notify> notifies = JSON.parseArray(jo.getString("result"), Notify.class);
            if (notifies.size() > 0) {
                Notify notify = notifies.get(0);
                tv_title.setText(notify.getTITLE());
                tv_content.setText("\t\t"+notify.getMESSAGE());
            }
        }
    }
}
