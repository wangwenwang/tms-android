package com.kaidongyuan.app.kdydriver.ui.fragment;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaidongyuan.app.basemodule.ui.fragment.BaseLifecyclePrintFragment;
import com.kaidongyuan.app.basemodule.utils.nomalutils.StringUtils;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.app.AppContext;
import com.kaidongyuan.app.kdydriver.app.AppManager;
import com.kaidongyuan.app.kdydriver.ui.activity.AboutActivity;
import com.kaidongyuan.app.kdydriver.ui.activity.FeedBackActivity;
import com.kaidongyuan.app.kdydriver.ui.activity.LoginActivity;
import com.kaidongyuan.app.kdydriver.ui.activity.MainActivity;
import com.kaidongyuan.app.kdydriver.ui.activity.OfflineMapActivity;
import com.kaidongyuan.app.kdydriver.ui.activity.UpdatePwdActivity;

/**
 * Created by Administrator on 2016/9/7.
 */
public class MineFragment extends BaseLifecyclePrintFragment implements View.OnClickListener {

    private View parent;
    private static MineFragment mFragment;
    private Context mContext;
    private final int LOGIN_REQUEST_CODE = 10;
    private Intent mLocationIntent;
    private Button btn_location;
    public boolean isupdate;
    //版本升级功能用到的
    private RelativeLayout mCheckVersionRLayout;
    private Handler mHandler;
    private NotificationManager mNotificationManager;
    private Notification mUpdataNotification;
    private AlertDialog mUpdataVersionDialog;
    private int mPreviousDownloadPercent;
    private long mCurrentDownloadSize;
    private TextView versionName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_mine, container, false);
        mContext = getActivity();
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //版本升级功能用到的
//        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        initHandler();

        initViews();
        //保证程序运行就开启后台定位功能，将此注释代码移动至MineFragment中 2016-03-09
//        if (mLocationIntent == null) {
//            mLocationIntent = new Intent(getActivity(), TrackingService.class);
//        }
//        getActivity().startService(mLocationIntent);
//        btn_location.setSelected(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isupdate){
            final MainActivity activity= (MainActivity) getActivity();
            versionName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.checkVersion();
                }
            });
            versionName.setText("有新版本！");
            versionName.setTextColor(getResources().getColor(R.color.red));
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (requestQueue!=null) {
//            requestQueue.cancelAll("checkVersion");
//        }
    }

    public static MineFragment getInstance() {
        if (mFragment == null) {
            mFragment = new MineFragment();
        }
        return mFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void stopTrackService(){
        Intent intent = new Intent();
        //设置Intent的action属性
        intent.setAction("com.kaidongyuan.app.kdydriver.stopposition");
        //发出广播
        getActivity().sendBroadcast(intent);
    }

    private void initViews() {
        SlidingTitleView titleView;
        if (AppContext.getInstance().getUser() != null) {
            ((TextView) parent.findViewById(R.id.tv_name)).setText(AppContext.getInstance().getUser().getUSER_NAME());
            ((TextView) parent.findViewById(R.id.tv_contact)).setText(AppContext.getInstance().getUser().getUSER_CODE());
        }

        //修改实时间 2016-03-12 隐藏开启定位功能按钮
//        btn_location = (Button) parent.findViewById(R.id.btn_location);
//        btn_location.setOnClickListener(this);
//        btn_location.setSelected(false);
//        btn_location.setSelected(isWorked("com.kaidongyuan.app.kdydriver.service.TrackingService"));

        titleView = (SlidingTitleView) parent.findViewById(R.id.setting_title_view);
        titleView.setText(getString(R.string.mine));
        titleView.setMode(SlidingTitleView.MODE_NULL);
        versionName = (TextView) parent.findViewById(R.id.version_name_text);
        versionName.setText("v" + StringUtils.getVersionName(getActivity()));

        //意见反馈， 检查更新，关于
        mCheckVersionRLayout = (RelativeLayout)parent.findViewById(R.id.rl_check_version);
        //mCheckVersionRLayout.setOnClickListener(this);
        parent.findViewById(R.id.rl_feed_back).setOnClickListener(this);
        parent.findViewById(R.id.rl_about).setOnClickListener(this);
        parent.findViewById(R.id.exit_btn).setOnClickListener(this);// 退出按钮
        parent.findViewById(R.id.rl_manage_info).setOnClickListener(this);//管理信息
        parent.findViewById(R.id.rl_update_pwd).setOnClickListener(this);//修改密码
        parent.findViewById(R.id.rl_my_evaluate).setOnClickListener(this);//我的评价
        parent.findViewById(R.id.rl_check_report).setOnClickListener(this);//查看报表
        parent.findViewById(R.id.rl_offlinemap).setOnClickListener(this);//离线地图
        // parent.findViewById(R.id.rl_tools).setOnClickListener(this);//工具
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_btn:
//                getActivity().finish();
                stopTrackService();
                //修改时间 2016-03-12 用户点击退出按钮后跳转到登陆界面
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
                AppContext.IS_LOGIN=false;
                AppManager.getAppManager().AppExit(getActivity());
                try{
                    AppManager.getAppManager().finishAllActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_update_pwd:
                startActivity(new Intent(getActivity(), UpdatePwdActivity.class));
                break;
            case R.id.rl_manage_info:
                showToastMsg("管理信息");
                break;
            case R.id.rl_offlinemap:
                startActivity(new Intent(getActivity(),OfflineMapActivity.class));
                break;
            case R.id.rl_my_evaluate:
                showToastMsg("我的评价");
                break;
//            case R.id.btn_location:
//                if (mLocationIntent == null) {
//                    mLocationIntent = new Intent(getActivity(), TrackingService.class);
//                }
//                if (!v.isSelected()) {
//                    showToastMsg("开启定位...");
//                    getActivity().startService(mLocationIntent);
//                } else {
//                    showToastMsg("关闭定位...");
//     //               getActivity().stopService(mLocationIntent);
//                    stopTrackService();
//                }
//                v.setSelected(!v.isSelected());
//                break;
            case R.id.rl_feed_back:
                Intent intent = new Intent();
                intent.setClass(getActivity(), FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_about:
                about();
                break;
//          case R.id.rl_check_version:
//              checkVersion();
        }
    }


    //************************************************ 检查版本更新开始 ***********************************************************
//    private RequestQueue requestQueue;
//    /**
//     * 我的信息 Fragment的检查更新 这里只是显示了当前版本号
//     */
//    public void checkVersion() {
//
//        requestQueue = Volley.newRequestQueue(mContext);
//        final StringRequest request = new StringRequest(StringRequest.Method.POST, Constants.URL.CheckVersion, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                mCheckVersionRLayout.setEnabled(true);
//                MLog.w( "版本升级.onResponse.response:"+response);
//                try {
//                    com.alibaba.fastjson.JSONObject jsResponse = JSON.parseObject(response);
//                    int type = jsResponse.getInteger("type");
//                    if (type==1){
//                        JSONArray arr = jsResponse.getJSONArray("result");
//                        int size = arr.size();
//                        String version = null;
//                        String downUrl = null;
//                        com.alibaba.fastjson.JSONObject obj;
//                        String netDownUrl;
//                        int startIndex;
//                        for (int i=0; i<size; i++){
//                            obj = arr.getJSONObject(i);
//                            netDownUrl = obj.getString("DownLoadAddress");
//                            startIndex = netDownUrl.indexOf('/')+1;
//                            String appName = netDownUrl.substring(startIndex, netDownUrl.length());
//                            if ("kdydriver.apk".equals(appName)){
//                                version = obj.getString("VersionCode");
//                                downUrl = "http://oms.kaidongyuan.com:8888/" + netDownUrl;
//                                break;
//                            }
//                        }
//                        if (version!=null && downUrl!=null) {
//                            compareAppVersion(version, downUrl);
//                        }
//                    }else {
//                        Toast.makeText(mContext, "获取最新版本失败", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(mContext, "获取最新版本异常", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mCheckVersionRLayout.setEnabled(true);
//                Toast.makeText(mContext, "连接异常", Toast.LENGTH_SHORT).show();
//                MLog.w( "版本升级.ErrorListener.error:"+error);
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                return params;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
//        requestQueue.add(request);
//        request.setTag("checkVersion");
//        mCheckVersionRLayout.setEnabled(false);
//    }
    //************************************************ 检查版本更新结束 ***********************************************************

    public void about() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AboutActivity.class);
        startActivity(intent);
    }

//    private boolean isWorked(String className) {
//        ActivityManager myManager = (ActivityManager) getActivity().getApplicationContext().getSystemService(
//                Context.ACTIVITY_SERVICE);
//        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
//                .getRunningServices(30);
//        for (int i = 0; i < runningService.size(); i++) {
//            if (runningService.get(i).service.getClassName().toString()
//                    .equals(className)) {
//                return true;
//            }
//        }
//        return false;
//    }


    //******************************************************************************************************************
    //以下为版本升级用到的

//    private void initHandler(){
//        mHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                int percent = msg.arg1;
//                //msg 中的 what 值如果是 -1 的话表示下载完成，开启安装界面，否则改变 dialog 中的状态
//                if (percent==100){
//                    createNotifaction(percent);
//                    String fileName = Environment.getExternalStorageDirectory() + "/downtemp/kdydriver.apk";
//                    File file = new File(fileName);
//                    if (!file.exists()) {
//                        Toast.makeText(mContext, "升级包不存在", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Uri uri = Uri.fromFile(file);
//                        String type = "application/vnd.android.package-archive";//.apk 的 mime 名
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setDataAndType(uri, type);
//                        startActivity(intent);
//                    }
//                    mNotificationManager.cancel(0);
//                    mCheckVersionRLayout.setEnabled(true);
//                } else if (percent==-1) {
//                    mCheckVersionRLayout.setEnabled(true);
//                    Toast.makeText(getActivity(), "下载更新失败", Toast.LENGTH_LONG).show();
//                    mNotificationManager.cancel(0);
//                } else{
//                    createNotifaction(percent);
//                }
//                return false;
//            }
//        });
//    }
//
//
//    /**
//     * 比较 app 当前版本和网络的版本是不是同一个版本，不是同一个版本就开始下载更新
//     * @param version 网络获取的版本号
//     * @param url 网络版本 app 下载路径
//     */
//    private void compareAppVersion(String version, String url){
//        try {
//
//            String currentVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
//            MLog.w( "version:"+version+"\tcurrentVersion:"+currentVersion);
//            if (!currentVersion.equals(version)) {
//                showDialog(currentVersion, version, url);
//            } else {
//                Toast.makeText(mContext, "当前版本是最新的版本", Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 提示用户升级的对话框
//     * @param currentVersion 当前 app 版本
//     * @param netVersion 网络 app 版本
//     * @param url 下载新版本 app 的地址
//     */
//    private void showDialog(String currentVersion, String netVersion, final String url){
//        if (mUpdataVersionDialog==null) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setTitle("更新版本");
//            builder.setMessage("当前版本：" + currentVersion+"\n最新版本："+netVersion);
//            builder.setCancelable(false);
//            builder.setNegativeButton("下载", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mUpdataVersionDialog.cancel();
//                    mCheckVersionRLayout.setEnabled(false);
//                    MLog.w( "PushTestBroadcastReceiver.update.url:" + url);
//                    updataApp(url);
//                }
//            });
//            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mUpdataVersionDialog.cancel();
//                }
//            });
//            mUpdataVersionDialog = builder.create();
//        }
//        mUpdataVersionDialog.show();
//    }
//
//    /**
//     * 下载更新版本的 app 文件
//     * @param url 网络 app 文件地址
//     */
//    private void updataApp(final String url){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //另起线程执行下载，安卓最新sdk规范，网络操作不能再主线程。
//                    final Download l = new Download(url);
//                    //初始化进度条，累计下载长度
//                    mPreviousDownloadPercent = 0;
//                    mCurrentDownloadSize = 0L;
//                    createNotifaction(1);
//                    /**
//                     * 下载文件到sd卡，虚拟设备必须要开始设置sd卡容量
//                     * downhandler是Download的内部类，作为回调接口实时显示下载数据
//                     */
//                    int status = l.down2sd("downtemp/", "kdydriver.apk", l.new downhandler() {
//                        @Override
//                        public void setSize(int size) {
//                            Message msg = mHandler.obtainMessage();
//                            mCurrentDownloadSize += size;
//                            int currentDownloadPercent = (int) (mCurrentDownloadSize * 100 / l.getLength());
//                            if ((currentDownloadPercent - mPreviousDownloadPercent) >= 5 || currentDownloadPercent == 100 || size == -1) {
//                                mPreviousDownloadPercent = currentDownloadPercent;
//                                if (size == -1) {
//                                    msg.arg1 = -1;
//                                } else {
//                                    msg.arg1 = currentDownloadPercent;
//                                }
//                                msg.sendToTarget();
//                            }
//                        }
//                    });
//                    MLog.w("MineFragment.连接网络app文件状态码" + Integer.toString(status));
//                    if (status != 1) {
//                        return;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 创建下载进度的 notification
//     * @param percent 下载进度
//     */
//    private void createNotifaction(int percent){
//        mUpdataNotification = new Notification();
//        mUpdataNotification.icon = R.drawable.ic_launcher;
//        //自定义 Notification 布局
//        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.dialog_download);
//        remoteView.setTextViewText(R.id.textView_dialog_download, percent+"%");
//        remoteView.setProgressBar(R.id.progressBar_dialog_download, 100, percent, false);
//        mUpdataNotification.contentView = remoteView;
//        mNotificationManager.notify(0, mUpdataNotification);
//    }

}
