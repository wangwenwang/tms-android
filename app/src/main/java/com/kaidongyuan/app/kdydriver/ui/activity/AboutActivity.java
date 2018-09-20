package com.kaidongyuan.app.kdydriver.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;


import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;


/**
 * 关于Activity
 * 
 * @author lian
 * 
 */
public class AboutActivity extends BaseActivity {

	private SlidingTitleView titleView;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);


		titleView = (SlidingTitleView) findViewById(R.id.about_us_title_view);
		mWebView = (WebView) findViewById(R.id.about_webview);
		titleView.setText(getString(R.string.about));
		initData();
	}


	public void loadDataWithWebview(String source) {
		mWebView.loadDataWithBaseURL(null, source, "text/html", "utf-8", null);
	}

	private void initData() {

		/*CustomResponseHandler mHandler = new CustomResponseHandler(this, false) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.getInt("result") == 1) {
						loadDataWithWebview(obj.getString("info"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		HttpClient.queryAbout(mHandler);*/
	}

}
