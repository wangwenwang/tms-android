package com.kaidongyuan.app.kdydriver.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.network.BaseAsyncHttpClient;
import com.kaidongyuan.app.basemodule.utils.nomalutils.StringUtils;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.app.AppContext;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 更新密码
 */
public class UpdatePwdActivity extends BaseActivity implements AsyncHttpCallback {

	private EditText oldPswEditText;
	private EditText newPswEditText;
	private EditText repeatPswEditText;
	private String oldPassword;
	private BaseAsyncHttpClient mClient;
	private final String TAG_UPDATE_PWD = "update_pwd";
	private String newPassword;
	private String repeatPassword;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_pwd_layout);

		SlidingTitleView titleView;
		titleView = (SlidingTitleView) findViewById(R.id.update_pwd_title_view);
		titleView.setText("修改密码");
		titleView.setMode(SlidingTitleView.MODE_BACK);
		oldPswEditText = (EditText) findViewById(R.id.old_psw_edit);
		newPswEditText = (EditText) findViewById(R.id.new_psw_edit);
		repeatPswEditText = (EditText) findViewById(R.id.repeat_psw_edit);
		mClient = new OrderAsyncHttpClient(this, this);

		findViewById(R.id.btn_update_pwd).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkInput()){
					updatePwdSubmit();
				}
			}
		});
	}

	/**
	 * 根据已有的密码重设密码，需要输入原有密码
	 */
	private boolean checkInput(){
		oldPassword = oldPswEditText.getText().toString();
		newPassword = newPswEditText.getText().toString();
		repeatPassword = repeatPswEditText.getText().toString();
		if (StringUtils.isBlank(oldPassword)) {
			showToast("请输入原密码", 100);
			return false;
		}
		if (StringUtils.isBlank(newPassword)) {
			showToast("请输入新密码", 100);
			return false;
		}
		if (StringUtils.isBlank(repeatPassword)) {
			showToast("请再输入一次新密码", 100);
			return false;
		}
		if (!repeatPassword.equals(newPassword)) {
			showToast("两次输入密码不一致", 100);
			return false;
		}
		if (newPassword.length() < 6) {
			showToast("密码不能少于6位数字和字母", 100);
			return false;
		}
		return true;
	}

	public void updatePwdSubmit() {
		oldPassword = oldPswEditText.getText().toString();
		repeatPassword = repeatPswEditText.getText().toString();
		Map<String, String> params = new HashMap<String, String>();
		params.put("strUserName", AppContext.getInstance().getUser().getUSER_CODE());//传入的是用户手机号，而非用户名
		params.put("strPassword", oldPassword);
		params.put("strNewPassword", repeatPassword);
		params.put("strLicense", "");
		mClient.sendRequest(Constants.URL.ModifyPassword, params, TAG_UPDATE_PWD, false);
	}

	@Override
	protected void onDestroy() {
		mClient.cancleRequest(TAG_UPDATE_PWD);
		super.onDestroy();
	}

	@Override
	public void postSuccessMsg(String msg, String request_tag) {
		if (msg.equals("error"))return;
		if (request_tag.equals(TAG_UPDATE_PWD)){
			JSONObject jo= JSON.parseObject(msg);
			//修改密码成功则销毁updatePwdActivity
			if (jo.getString("type").equals("1")){
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				UpdatePwdActivity.this.finish();
			}
		}
	}
}
