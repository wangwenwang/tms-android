package com.kaidongyuan.app.kdydriver.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.app.AppContext;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;


/**
 * 意见反馈的Activity
 * 
 * @author ke
 * 
 */
public class FeedBackActivity extends BaseActivity {

	private SlidingTitleView titleView;
	private EditText contact;
	private EditText content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_layout);
		titleView = (SlidingTitleView) findViewById(R.id.feedback_title_view);
		contact = (EditText) findViewById(R.id.feedback_contact_edit);
		content = (EditText) findViewById(R.id.input_feedback);
		titleView.setText(getString(R.string.feed_back));
		titleView.setMode(SlidingTitleView.MODE_BACK);
		contact.setText(AppContext.getInstance().getUser().getUSER_NAME());
	}

	public void submitFeedBack(View v) {

		/*String contactInfo = contact.getText().toString();
		String feedBack = content.getText().toString();

		if (StringUtils.isBlank(contactInfo)) {
			showToast("请输入您的联系方式", 100);
			return;
		}

		if (!(StringUtils.isMobileNO(contactInfo) || StringUtils.isEmail(contactInfo))) {
			showToast("您的联系方式格式不对哦", 100);
			return;
		}
		if (StringUtils.isBlank(feedBack)) {
			showToast("请说几句吧  ( ^_^ )", 100);
			return;
		}

		CustomResponseHandler mHandler = new CustomResponseHandler(this, true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.getInt("result") > 0) {
						showToast("感谢您的反馈", 100);
						finish();
					} else
						showToast(obj.getString("info"), 100);
				} catch (JSONException e) {
					e.printStackTrace();
					ExceptionHandler.getAppExceptionHandler().saveErrorLog(e);
				}
			}
		};
		HttpClient.submitFeedBack(mHandler, feedBack, contactInfo, AppContext.getInstance().getUser().getUSER_NAME());
	*/}

}
