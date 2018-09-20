package com.kaidongyuan.app.kdydriver.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.utils.nomalutils.CharacterParser;
import com.kaidongyuan.app.basemodule.utils.nomalutils.Des3;
import com.kaidongyuan.app.basemodule.widget.MLog;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.app.AppContext;
import com.kaidongyuan.app.kdydriver.app.AppManager;
import com.kaidongyuan.app.kdydriver.bean.order.User;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;
import com.kaidongyuan.app.kdydriver.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/6/1.
 */
public class WelcomeActivity extends BaseActivity implements AsyncHttpCallback{
    private ImageView iv_kdy;
    private AnimationSet as;
    private OrderAsyncHttpClient client;
    private final String LOGIN = "login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        startAnimation();
        // 动画播完进入下一个界面 向导界面和主界面
        initEvent();
    }
    @Override
    public void initWindow() {
        //重写为空，针对满屏页面取消沉浸式状态栏
    }
    private void initEvent() {
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (SharedPreferencesUtils.getValueByName(Constants.BasicInfo, Constants.IsUsedApp, 2)){
                    if (SharedPreferencesUtils.getUserId()!=null
                        &&SharedPreferencesUtils.getValueByName(Constants.BasicInfo, Constants.UserName, 0)!=null
                        &&SharedPreferencesUtils.getValueByName(Constants.BasicInfo,Constants.UserPWD,0)!=null){
                        client=new OrderAsyncHttpClient(WelcomeActivity.this,WelcomeActivity.this);
                    try{
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("strUserName", (String) SharedPreferencesUtils.getValueByName(Constants.BasicInfo, Constants.UserName, 0));
                        String strpwd=SharedPreferencesUtils.getValueByName(Constants.BasicInfo,Constants.UserPWD,0);
                        params.put("strPassword", Des3.decode(strpwd));
                        params.put("strLicense", "");
                        client.setShowToast(false);
                        client.sendRequest(Constants.URL.Login,params,LOGIN);
                        return;
                    }catch (Exception e){
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    }
//        params.put("roleNames", tv_select_role.getText()+"");
                    }else {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    }
                }else {
                    startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
                }
                WelcomeActivity.this.finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initView() {
        iv_kdy= (ImageView) findViewById(R.id.welcomeActivty_kdy);
        AppContext.IS_LOGIN=false;
    }

    /**
     * 开始播放动画：旋转，缩放，渐变
     */
    private void startAnimation() {
        // false 代表动画集中每种动画都采用各自的动画插入器(数学函数)
        as = new AnimationSet(false);

        // 旋转动画,锚点
        RotateAnimation ra = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);// 设置锚点为图片的中心点
        // 设置动画播放时间
        ra.setDuration(1500);
        ra.setFillAfter(true);// 动画播放完之后，停留在当前状态

        // 添加到动画集
        as.addAnimation(ra);

        // 渐变动画
        AlphaAnimation aa = new AlphaAnimation(0, 1);// 由完全透明到不透明
        // 设置动画播放时间
        aa.setDuration(1500);
        aa.setFillAfter(true);// 动画播放完之后，停留在当前状态

        // 添加到动画集
        as.addAnimation(aa);

        // 缩放动画
        ScaleAnimation sa = new ScaleAnimation(0.1f, 0.97f, 0.1f, 0.97f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        // 设置动画播放时间
        sa.setDuration(1500);
        sa.setFillAfter(true);// 动画播放完之后，停留在当前状态

        // 添加到动画集
        as.addAnimation(sa);

        // 播放动画
        iv_kdy.startAnimation(as);

    }

    @Override
    public void postSuccessMsg(String msg, String request_tag) {
        if (msg.equals("error")){
            mStartActivity(LoginActivity.class);
            finish();
            return;
        }else if (request_tag.equals(LOGIN)){
            JSONObject jo = JSON.parseObject(msg);
            try {
                List<User> user = JSON.parseArray(jo.getString("result"), User.class);
                MLog.e("user.size():" + user.size() + "IDX:" + user.get(0).getIDX());
                if (user.size() > 0) {
                    // user.get(0).setUSER_CODE(et_username.getText().toString().trim());
                    //   MLog.e("User:"+user.get(0).getIDX());
                    AppContext.getInstance().setUser(user.get(0));
                    AppContext.IS_LOGIN = true;
                }
                SharedPreferencesUtils.saveUserId(AppContext.getInstance().getUser().getIDX());
                AppManager.getAppManager().finishActivity(MainActivity.class);
                mStartActivity(MainActivity.class);
              //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
