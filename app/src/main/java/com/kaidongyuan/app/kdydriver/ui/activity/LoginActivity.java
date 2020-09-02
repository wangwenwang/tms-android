package com.kaidongyuan.app.kdydriver.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.igexin.sdk.PushManager;
import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.utils.nomalutils.MPermissionsUtil;
import com.kaidongyuan.app.basemodule.utils.nomalutils.NetworkUtils;
import com.kaidongyuan.app.basemodule.utils.nomalutils.StringUtils;
import com.kaidongyuan.app.basemodule.widget.MLog;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.bean.Tools;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.serviceAndReceiver.GetuiIntentService;
import com.kaidongyuan.app.kdydriver.serviceAndReceiver.GetuiPushService;
import com.kaidongyuan.app.kdydriver.serviceAndReceiver.TrackingService;
import com.kaidongyuan.app.kdydriver.ui.base.BaseFragmentActivity;
import com.kaidongyuan.app.kdydriver.ui.fragment.MineFragment;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseFragmentActivity implements AsyncHttpCallback {

    public static WebView mWebView;

    public static Context mContext;

    String inputName;

    String appName;

    public static String mAppVersion;

    // 微信开放平台APP_ID
    private static final String APP_ID = Constants.WXLogin_AppID;

    static public IWXAPI mWxApi;

    public final static String DestFileName = "saas-kdy-tms.apk";
    public final static String ZipFileName = "dist.zip";

    String server_App_Version;

    String server_App_Url;

    //检测版本更新
    private final String TAG_CHECKVERSION = "check_version";
    private OrderAsyncHttpClient mClient;
    private NotificationManager mNotificationManager;
    private Handler mHandler;
    private Notification mUpdataNotification;
    private Snackbar pmSnackbar;
    private RemoteViews remoteView;
    private MineFragment minefragment;
    private final int RequestPermission_STATUS_CODE0 = 8800;
    private AlertDialog mUpdataVersionDialog;


    //5.0以下使用
    private ValueCallback<Uri> uploadMessage;
    // 5.0及以上使用
    private ValueCallback<Uri[]> uploadMessageAboveL;
    //图片
    private final static int FILE_CHOOSER_RESULT_CODE = 128;
    //拍照
    private final static int FILE_CAMERA_RESULT_CODE = 129;
    //拍照图片路径
    private String cameraFielPath;
    private Uri mImageUri, mImageUriFromFile;
    private static final String FILE_PROVIDER_AUTHORITY = "com.kaidongyuan.app.kdytms.fileprovider";
    // zip解压路径
    String unZipOutPath;
    private String CURR_ZIP_VERSION = "0.4.5";
    private String WhoCheckVersion;


    private Intent mLocationIntent;

    // 用户类型
    public static String userType;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        Log.d("LM", "程序启动");

        final SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_W_UserInfo_Key, MODE_MULTI_PROCESS);
        // 设置开始上传为NO
        sp.edit().putString(Constants.SP_BeginRequestUploadLng_Key, Constants.SP_BeginRequestUploadLng_Value_NO).apply();
        // 设置第一次加载LoginActive为YES
        sp.edit().putString(Constants.SP_LoginActiveFirstStart_Key, Constants.SP_LoginActiveFirstStart_Value_YES).apply();
        // 设置上次定位为空
        sp.edit().putString("CurrAddrStr", "").apply();
        sp.edit().putString("CurrLongitude", "").apply();
        sp.edit().putString("CurrLatitude", "").apply();
        sp.edit().putInt("CurrLocCode", 13).apply();

        try {
            mAppVersion = getMContext().getPackageManager().getPackageInfo(getMContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //
        unZipOutPath = "/data/data/" + getPackageName() + "/upzip/";

//        Button bt = (Button) findViewById(R.id.button);
//        //1.匿名内部类
//        bt.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Log.i("LM", "点击事件");
//            }
//        });


        // 设置ZIP版本号
        String curr_zip_version = Tools.getAppZipVersion(mContext);
        if (curr_zip_version != null && curr_zip_version.equals("")) {

            Tools.setAppZipVersion(mContext, CURR_ZIP_VERSION);
        }
        curr_zip_version = Tools.getAppZipVersion(mContext);
        Log.d("LM", "本地zip版本号：： " + curr_zip_version);

        appName = getResources().getString(R.string.app_name);

        mWebView = (WebView) findViewById((R.id.lmwebview));
        mWebView.getSettings().setTextZoom(100);

        // disable scroll on touch
//        mWebView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });


        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("LM", "当前位置: " + url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            // js拔打电话
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("LM", "------------------------: ");

                if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
                return true;
            }
        });


        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebChromeClient(new WebChromeClient() {
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }


            // 处理javascript中的alert
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                return false;
            }

            // 处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                return true;
            }

            // 处理定位权限请求
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            //            @Override
//            // 设置网页加载的进度条
//            public void onProgressChanged(WebView view, int newProgress) {
//                TestJSLocation.this.getWindow().setFeatureInt(
//                        Window.FEATURE_PROGRESS, newProgress * 100);
//                super.onProgressChanged(view, newProgress);
//            }
            // 设置应用程序的标题title
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });


        // 获取上次启动记录的版本号
        String lastVersion = Tools.getAppLastTimeVersion(mContext);
        Log.d("LM", "上次启动记录的版本号: " + lastVersion);


        boolean isExists = Tools.fileIsExists("/data/data/" + getPackageName() + "/upzip/dist/index.html");
        if (lastVersion.equals(mAppVersion)) {

            Log.d("LM", "html已存在，无需解压");
        } else {

            Log.d("LM", "html不存在或有新版本，开始解压");
            try {
                Tools.unZip(mContext, "dist.zip", unZipOutPath, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("LM", "解压完成，加载html");
        }
        mWebView.loadUrl("file:///data/data/" + getPackageName() + "/upzip/dist/index.html");
        Tools.setAppLastTimeVersion(mContext);
        lastVersion = Tools.getAppLastTimeVersion(mContext);
        Log.d("LM", "上次启动记录的版本号已设置为: " + lastVersion);

        // 启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setVerticalScrollbarOverlay(true);
//        mWebView.loadUrl("http://163xw.com/jsAlbum.html");

        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new JsInterface(this), "CallAndroidOrIOS");

        mWebView.setLongClickable(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setDrawingCacheEnabled(true);

        // 注册微信登录
        registToWX();

        minefragment = new MineFragment();
        mClient = new OrderAsyncHttpClient(this, this);
        mNotificationManager = (NotificationManager) getMContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(APP_ID);
    }

    @Override
    public void postSuccessMsg(String msg, String request_tag) {

        Log.d("LM", "标签" + request_tag + "请求成功：" + msg);

        if (msg.equals("error")) {
            //下载安装包失败
            if (request_tag.equals(DestFileName)) {
                Message message = mHandler.obtainMessage();
                message.arg1 = -1;
                message.sendToTarget();
                return;
            }

            if (!NetworkUtils.isNetworkAvailable(getMContext())) {
                NetworkUtils.setContactNetDialog(getApplication());
                return;
            }
        } else if (request_tag.equals(TAG_CHECKVERSION)) {
            JSONObject jo = JSON.parseObject(msg);

            String status = jo.getString("status");

            String apkDownloadUrl = null;
            String server_apkVersion = null;
            String zipDownloadUrl = null;
            String server_zipVersion = null;
            if (status.equals("1")) {

                JSONObject dict = jo.getJSONObject("data");
                apkDownloadUrl = dict.getString("downloadUrl");
                server_apkVersion = dict.getString("versionNo");
                zipDownloadUrl = dict.getString("zipDownloadUrl");
                server_zipVersion = dict.getString("zipVersionNo");
            }

//            downUrl = "http://oms.kaidongyuan.com:8888/download/saas-wms.apk";
            if (server_apkVersion != null && apkDownloadUrl != null) {
                try {
                    String current_apkVersion = mAppVersion;
                    MLog.w("server_apkVersion:" + server_apkVersion + "\tcurrent_apkVersion:" + current_apkVersion);

                    int compareVersion = Tools.compareVersion(server_apkVersion, current_apkVersion);
                    if (compareVersion == 1) {

                        createUpdateDialog(current_apkVersion, server_apkVersion, apkDownloadUrl);
                        minefragment.isupdate = true;
                    } else {

                        Log.d("LM", "apk为最新版本");

                        String curr_zipVersion = Tools.getAppZipVersion(mContext);
                        compareVersion = Tools.compareVersion(server_zipVersion, curr_zipVersion);
                        if (compareVersion == 1) {

                            Log.d("LM", "服务器zip版本：" + server_zipVersion + "    " + "本地zip版本：" + CURR_ZIP_VERSION);
                            CURR_ZIP_VERSION = server_zipVersion;
                            Log.d("LM", "更新zip...");
                            showUpdataZipDialog(zipDownloadUrl);
                        } else {

                            Log.d("LM", "zip为最新版本");

                            if(WhoCheckVersion.equals("vue")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("");
                                builder.setMessage("已经是最新版本！");
                                builder.setPositiveButton("确定", null);
                                builder.show();
                            }
                        }
                        checkGpsState();
                    }
                } catch (Exception e) {
                    Log.d("LM", "NameNotFoundException" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 版本更新对话框
     *
     * @param currentVersion 当前版本versionName
     * @param version        最新版本versionName
     * @param downUrl        最新版本安装包下载url
     */
    public void createUpdateDialog(String currentVersion, String version, final String downUrl) {
        if (mUpdataVersionDialog == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getMContext());
            builder.setMessage("当前版本：" + currentVersion + "\n最新版本：" + version);
            builder.setCancelable(false);
            builder.setTitle("更新版本");
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mUpdataVersionDialog.cancel();
                }
            });
            builder.setNegativeButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mUpdataVersionDialog.cancel();
                    MLog.w("update.url:" + downUrl);
                    //以存储文件名为Tag名
                    mClient.sendFileRequest(downUrl, DestFileName);
                }
            });
            mUpdataVersionDialog = builder.create();
        }
        mUpdataVersionDialog.show();
    }

    /**
     * /**
     * 判断 GPS是否开启
     */
    private void checkGpsState() {
        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) && !alm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            MLog.w("MainActivity.checkGpsState:GpsisOff");
            createCheckGpsDialog();
        } else {
            MLog.w("MainActivity.checkGpsState:GpsisOn");
        }
    }

    private void createCheckGpsDialog() {
        showSnackbar("请开启GPS服务", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent;
                myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                LoginActivity.this.startActivity(myIntent);
            }
        }, Snackbar.LENGTH_INDEFINITE);
    }

    private void showSnackbar(String strSnackbar, View.OnClickListener listener, int duration) {

        pmSnackbar = Snackbar.make(findViewById(R.id.acitvity_mainAcitivity), strSnackbar, duration);
        View v = pmSnackbar.getView();
        v.setBackgroundColor(getResources().getColor(R.color.details_text));
        final TextView tv_snackbar = (TextView) v.findViewById(R.id.snackbar_text);
        tv_snackbar.setGravity(Gravity.CENTER);
        tv_snackbar.setTextColor(getResources().getColor(R.color.white));
        pmSnackbar.setAction("设置", listener).show();
    }

    // js调用java
    private class JsInterface extends BaseFragmentActivity {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        // 经纬坐标转地址，抽象函数
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            }
        };

        @JavascriptInterface
        public void callAndroid(String exceName) {

            Log.d("LM", "执行:" + exceName);

            if (exceName.equals("检查版本更新")) {

                // 开启定位服务
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        checkVersion("vue");
                    }
                });
            }
        }

        //在js中调用window.CallAndroidOrIOS.callAndroid(name)，便会触发此方法。
        @JavascriptInterface
        public void callAndroid(String exceName, final String inputName) {

            Log.d("LM", "执行:" + exceName + "    " + "输入框:" + inputName);
            LoginActivity.this.inputName = inputName;

            final SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_W_UserInfo_Key, MODE_MULTI_PROCESS);

            if (exceName.equals("微信登录")) {

                Log.d("LM", "微信登录");

                new Thread() {
                    public void run() {

                        if (!mWxApi.isWXAppInstalled()) {
                            Log.d("LM", "您还未安装微信客户端");
                            return;
                        } else {
                            Log.d("LM", "微信客户端已安装");
                        }
                        SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";//官方固定写法
                        req.state = "wechat_sdk_tms";//自定义一个字串

                        mWxApi.sendReq(req);
                    }
                }.start();
            } else if (exceName.equals("登录页面已加载")) {

                Log.d("LM", "登录页面已加载");

                final String u = sp.getString("UserName", "");
                final String p = sp.getString("Password", "");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String url = "javascript:SetUserNameAndPassword('" + u + "','" + p + "')";
                        LoginActivity.mWebView.loadUrl(url);
                        Log.d("LM", url);

                        url = "javascript:VersionShow('" + "版本:" + Tools.getVerName(mContext) + "')";
                        LoginActivity.mWebView.loadUrl(url);
                        Log.d("LM", url);

                        url = "javascript:Device_Ajax('android')";
                        LoginActivity.mWebView.loadUrl(url);
                        Log.d("LM", url);
                    }
                });
            } else if (exceName.equals("获取当前位置页面已加载")) {

                Log.d("LM", "获取当前位置页面已加载");

                // 上次记录的位置和设备信息
                final String address = sp.getString("CurrAddrStr", "");
                final String lng = sp.getString("CurrLongitude", "");
                final String lat = sp.getString("CurrLatitude", "");
                final int locCode = sp.getInt("CurrLocCode", 0);

                // 如果有经纬坐标，没有地址。进行坐标转地址
                if(!lat.equals("") && !lng.equals("") && address.equals("")) {

                    GeoCoder geoCoder = GeoCoder.newInstance();
                    LatLng latlng = new LatLng(Tools.convertToDouble(lat, 0) ,Tools.convertToDouble(lng, 0));
                    geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
                    geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                        // 经纬度转换成地址
                        @Override
                        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                            if (result == null ||  result.error != SearchResult.ERRORNO.NO_ERROR) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        String url = "javascript:SetCurrAddress('" + address + "','" + lng + "','" + lat + "','" + locCode + "')";
                                        LoginActivity.mWebView.loadUrl(url);
                                        Log.d("LM", url);
                                    }
                                });
                            }else {

                                final String addressGeo = "中国|" + result.getAddress();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        String url = "javascript:SetCurrAddress('" + addressGeo + "','" + lng + "','" + lat + "','" + locCode + "')";
                                        LoginActivity.mWebView.loadUrl(url);
                                        Log.d("LM", url);
                                    }
                                });
                            }
                        }

                        // 把地址转换成经纬度
                        @Override
                        public void onGetGeoCodeResult(GeoCodeResult result) {
                        }
                    });
                }
                // 有经纬坐标，有地址
                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String url = "javascript:SetCurrAddress('" + address + "','" + lng + "','" + lat + "','" + locCode + "')";
                            LoginActivity.mWebView.loadUrl(url);
                            Log.d("LM", url);
                        }
                    });
                }

            } else if (exceName.equals("导航")) {

                Log.d("LM", "导航");

                new Thread() {

                    public void run() {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Tools.ToNavigation(inputName, mContext, appName);
                            }
                        });
                    }
                }.start();
            } else if (exceName.equals("查看路线")) {

                Log.d("LM", "查看路线");

//                new Thread() {
//
//                    public void run() {
//
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                Intent intent2 = new Intent(LoginActivity.mContext, OrderTrackActivity.class);
//                                intent2.putExtra("order_IDX", inputName);
//                                mContext.startActivity(intent2);
//                            }
//                        });
//                    }
//                }.start();
            } else if (exceName.equals("用户类型")) {

                Log.d("LM", "用户类型");
                userType = inputName;
                Log.d("LM", userType);
            }
        }

        @JavascriptInterface
        public void callAndroid(String exceName, String u, String p) {

            Log.d("LM", "执行:" + exceName + "    " + "SetCurrAddressSetCurrAddress名:" + u + "    " + "密码:" + p);

            if (exceName.equals("记住帐号密码")) {

                if (u == null || p == null || u.equals("") || p.equals("")) {
                    String re = Tools.timingTracking1(u, "更新帐号为：" + u + "   密码：" + p, "", "", "", "7", "7", Build.VERSION.RELEASE + "|" + android.os.Build.MODEL + "|" + StringUtils.getVersionName(mContext), mContext);
                }

                if (u != null && p != null && !u.equals("") && !p.equals("")) {

                    SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_W_UserInfo_Key, MODE_MULTI_PROCESS);
                    sp.edit().putString("UserName", u).apply();
                    sp.edit().putString("Password", p).apply();

                    // 开启定位服务
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            startServiceLM();
                        }
                    });
                }
            }
        }

        @JavascriptInterface
        public void callAndroid(String exceName, final String shipmentID, final String shipmentCode, final String shipmentStatus) {

            Log.d("LM", "执行:" + exceName + "    " + "装运ID:" + shipmentID + "    "
                    + "装运编号:" + shipmentCode + "    " + "装运状态:" + shipmentStatus);

            if (exceName.equals("查看路线")) {

                new Thread() {

                    public void run() {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent2 = new Intent(LoginActivity.mContext, OrderTrackActivity.class);
                                intent2.putExtra("order_IDX", shipmentID);
                                intent2.putExtra("shipment_Code", shipmentCode);
                                intent2.putExtra("shipment_Status", shipmentStatus);
                                mContext.startActivity(intent2);
                            }
                        });
                    }
                }.start();
            }
        }
    }

    // 开启定位服务
    private void startServiceLM() {

        initPermission();
        //开启后台定位服务
        if (mLocationIntent == null) {
            mLocationIntent = new Intent(mContext, TrackingService.class);
        }
        Context mm = getApplicationContext();
        getApplicationContext().startService(mLocationIntent);
        initHandler();
        PushManager.getInstance().initialize(getApplicationContext(), GetuiPushService.class);
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), GetuiIntentService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initPermission() {

        Log.d("LM", "申请存储权限");

        try {

            if (Build.VERSION.SDK_INT >= 23) {
                if (MPermissionsUtil.checkAndRequestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , RequestPermission_STATUS_CODE0)) {
                    checkVersion("原生");
                }
            } else {

                checkVersion("原生");
            }
        } catch (Exception e) {

            Log.d("LM", "initPermission: " + e.getMessage());
        }
    }

    public void checkVersion(String who) {
        this.WhoCheckVersion = who;
        Log.d("LM", "检查apk及zip版本");
        Map<String, String> params = new HashMap<>();
        params.put("params", "{\"tenantCode\":\"KDY\"}");
        mClient.sendRequest(Constants.URL.SAAS_API_BASE + "queryAppVersion.do", params, TAG_CHECKVERSION);
    }

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                int percent = message.arg1;
                if (percent == 100) {
                    createNotifaction(percent);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), DestFileName);
                    if (!file.exists()) {
                        Toast.makeText(getMContext(), "升级包不存在", Toast.LENGTH_SHORT).show();
                    } else {

                        try{
                            Uri uri = FileProvider.getUriForFile(mContext, "com.kaidongyuan.app.kdytms.fileprovider", file);
                            String type = "application/vnd.android.package-archive";//.apk 的 mime 名
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(uri, type);
                            startActivity(intent);
                        }catch (Exception e){
                            Log.d("LM", e.getMessage());
                        }
                    }
                    mNotificationManager.cancel(0);
                } else if (percent == -1) {
                    Toast.makeText(LoginActivity.this, "更新失败，服务器异常", Toast.LENGTH_LONG).show();
                    mNotificationManager.cancel(0);
                } else {
                    createNotifaction(percent);
                }
                return false;
            }
        });
    }

    /**
     * 创建下载进度的 notification
     *
     * @param percent 下载进度
     */
    private void createNotifaction(int percent) {
        //自定义 Notification 布局
        if (mUpdataNotification == null) {
            mUpdataNotification = new Notification();
            mUpdataNotification.icon = R.mipmap.ic_launcher;
            mUpdataNotification.tickerText = getResources().getText(R.string.app_name);
        }
        if (remoteView == null) {
            remoteView = new RemoteViews(getMContext().getPackageName(), R.layout.dialog_download);
        }
        remoteView.setTextViewText(R.id.textView_dialog_download, percent + "%");
        remoteView.setProgressBar(R.id.progressBar_dialog_download, 100, percent, false);
        mUpdataNotification.contentView = remoteView;
        mNotificationManager.notify(0, mUpdataNotification);
    }

    @Override
    public void setProgressBarLoading(int progress) {
        // super.setProgressBarLoading(progress);
        //改为更新通知栏进度条
        Message message = mHandler.obtainMessage();
        message.arg1 = progress;
        message.sendToTarget();
    }

    // android 7.0以上手机存储授权后回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        Log.d("LM", "拍照5.8: ");

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("LM", "拍照5.9: ");


        if (requestCode == 101) {


            Log.d("LM", "拍照5.9.1: ");
            takeCameraM();
        }
    }

    private void openImageChooserActivity() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("拍照/相册");
//        builder.setPositiveButton("相册", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                takePhoto();
//            }
//        });
        builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takeCamera();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    //选择图片
    private void takePhoto() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    //拍照
    private void takeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (hasSDCard()) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile;
            try {

                imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
                cameraFielPath = imageFile.getPath();
            } catch (IOException e) {

                e.printStackTrace();
            }
            File outputImage = new File(cameraFielPath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
            startActivityForResult(intent, FILE_CAMERA_RESULT_CODE);
        }
    }

    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    public boolean hasSDCard() {

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private void takeCameraM() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            File imageFile = createImageFile();//创建用来保存照片的文件
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, imageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, FILE_CAMERA_RESULT_CODE);//打开相机
            }
        } else {
        }
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            cameraFielPath = imageFile.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    /**
     * 判断文件是否存在
     */
    public static boolean hasFile(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(Intent intent) {
        Uri[] results = null;
        if (intent != null) {
            String dataString = intent.getDataString();
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                results = new Uri[clipData.getItemCount()];
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    results[i] = item.getUri();
                }
            }
            if (dataString != null)
                results = new Uri[]{Uri.parse(dataString)};
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    /**
     * 返回上一页
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 登录页时不允许返回上一页
            String curURL = mWebView.getUrl();
            String orgURL = mWebView.getOriginalUrl();
            if (curURL.equals("file:///data/data/com.kaidongyuan.app.kdytms/upzip/dist/index.html#/")) {

                Log.d("LM", "禁止返回上一页1：" + curURL);
                return false;
            }

            // 首页
            String Index = "file:///data/data/com.kaidongyuan.app.kdytms/upzip/dist/index.html#/Index";
            // 任务
            String Waybill = "file:///data/data/com.kaidongyuan.app.kdytms/upzip/dist/index.html#/Waybill";
            // 报表
            String ReportForms = "file:///data/data/com.kaidongyuan.app.kdytms/upzip/dist/index.html#/ReportForms";
            // 我的
            String HomeIndex = "file:///data/data/com.kaidongyuan.app.kdytms/upzip/dist/index.html#/HomeIndex";
            // 货源
            String goodsSource = "file:///data/data/com.kaidongyuan.app.kdytms/upzip/dist/index.html#/goodsSource";
            // 车源
            String carSourceList = "file:///data/data/com.kaidongyuan.app.kdytms/upzip/dist/index.html#/carSourceList";
            // 订单
            String od_bid = "file:///data/data/com.kaidongyuan.app.kdytms/upzip/dist/index.html#/od_bid";
            if(userType.equals("driver")){
                od_bid = "kkkk";
            }
            // 发货
            String publishGoods = "file:///data/data/com.kaidongyuan.app.kdytms/upzip/dist/index.html#/publishGoods";

            // 主菜单时不允许返回上一页
            if (
                    curURL.indexOf(Index + "?") != -1 || curURL.equals(Index) ||
                            curURL.indexOf(Waybill + "?") != -1 || curURL.equals(Waybill) ||
                            curURL.indexOf(ReportForms + "?") != -1 || curURL.equals(ReportForms) ||
                            curURL.indexOf(HomeIndex + "?") != -1 || curURL.equals(HomeIndex) ||
                            curURL.indexOf(goodsSource + "?") != -1 || curURL.equals(goodsSource) ||
                            curURL.indexOf(carSourceList + "?") != -1 || curURL.equals(carSourceList) ||
                            curURL.indexOf(od_bid + "?") != -1 || curURL.equals(od_bid) ||
                            curURL.indexOf(publishGoods + "?") != -1 || curURL.equals(publishGoods)
                    ) {

                Log.d("LM", "禁止返回上一页2：" + curURL);
                return false;
            }
            mWebView.goBack();
            Log.d("LM", "curURL: " + curURL);
            Log.d("LM", "orgURL: " + orgURL);
        }
        return false;
    }

    /**
     * 返回桌面
     */
    private void goHomeActivity() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    /******************************************************** HTML版本更新功能 ********************************************************/
    /**
     * 弹出对话框
     */
    protected void showUpdataZipDialog(final String downUrl) {

        downLoadZip(downUrl);
    }

    protected void downLoadZip(final String downUrl) {
        //进度条
        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("");
        pd.show();
        pd.setOnKeyListener(onKeyListener);
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = Tools.getFileFromServer(downUrl, pd);
                    Log.d("LM", "Zip下载完毕，地址：" + file.getPath());

                    // 更新ZIP版本号
                    Tools.setAppZipVersion(mContext, CURR_ZIP_VERSION);
                    Log.d("LM", "zip更新成功，设置版本号为：" + CURR_ZIP_VERSION);

                    Log.d("LM", "取出验证为：" + Tools.getAppZipVersion(mContext));


                    try {
                        Log.d("LM", "SD卡开始解压...");
                        Tools.UnZipFolder("/storage/emulated/0/dist.zip", unZipOutPath);
                        Log.d("LM", "SD卡完成解压...");
                    } catch (Exception e) {
                        Log.d("LM", "SD卡解压异常..." + e.getMessage());
                        e.printStackTrace();
                    }

                    pd.dismiss(); //结束掉进度条对话框

                    new Thread() {
                        public void run() {

                            for (int i = 0; i < 5; i++) {
                                try {
                                    sleep(1 * 300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.d("LM", "开始刷新HTML  " + i);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        mWebView.reload();
                                    }
                                });
                                Log.d("LM", "完成刷新HTML  " + i);
                            }
                        }
                    }.start();
                } catch (Exception e) {

                    Log.d("", "run: ");
                }
            }
        }.start();
    }

    // 下载进度时，点击屏幕不可取消
    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("LM", "onActivityResult: ----");

        super.onActivityResult(requestCode, resultCode, data);

        if (null == uploadMessage && null == uploadMessageAboveL) return;

        if (resultCode != RESULT_OK) {//同上所说需要回调onReceiveValue方法防止下次无法响应js方法

            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
            return;
        }

        Uri result = null;
        if (requestCode == FILE_CAMERA_RESULT_CODE) {

            if (result == null && hasFile(cameraFielPath)) {

                result = Uri.fromFile(new File(cameraFielPath));
            }
            if (uploadMessageAboveL != null) {

                uploadMessageAboveL.onReceiveValue(new Uri[]{result});

                uploadMessageAboveL = null;
            } else if (uploadMessage != null) {

                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {

            if (data != null) {
                result = data.getData();
            }
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    private long getTimeExpend(String startTime, String endTime) {
        long longStart = getTimeMillis(startTime);
        long longEnd = getTimeMillis(endTime);
        return longEnd - longStart;
    }

    private long getTimeMillis(String strTime) {
        long returnMillis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(strTime);
            returnMillis = d.getTime();
        } catch (ParseException e) {
            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return returnMillis;
    }
}
