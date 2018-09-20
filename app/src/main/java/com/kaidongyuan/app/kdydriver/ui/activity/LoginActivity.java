package com.kaidongyuan.app.kdydriver.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.LocationClient;
import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.utils.nomalutils.MPermissionsUtil;
import com.kaidongyuan.app.basemodule.utils.nomalutils.NetworkUtils;
import com.kaidongyuan.app.basemodule.widget.LMLog;
import com.kaidongyuan.app.basemodule.widget.MLog;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.bean.Tools;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.serviceAndReceiver.TrackingService;
import com.kaidongyuan.app.kdydriver.ui.base.BaseFragmentActivity;
import com.kaidongyuan.app.kdydriver.ui.fragment.MineFragment;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseFragmentActivity implements AsyncHttpCallback {

    public static WebView mWebView;

    public static Context mContext;

    String inputName;

    String appName;

    // 微信开放平台APP_ID
    private static final String APP_ID = "wx4c368e3f56d8ace2";

    static public IWXAPI mWxApi;

    public final static String DestFileName = "tms_0.0.1.apk";

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
    private final int RequestPermission_STATUS_CODE0=8800;
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
    private static final String FILE_PROVIDER_AUTHORITY = "com.cy_scm.wms_android.fileprovider";


    private Intent mLocationIntent;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("LM", "程序启动");


        mContext = this;

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

        appName = getResources().getString(R.string.app_name);

        mWebView = (WebView) findViewById((R.id.lmwebview));


        LocationClient mLocationClient = new LocationClient(getApplicationContext());
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
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
                return true;
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

        mLocationClient.start();
//        mLocationClient.enableAssistantLocation(mWebView);

        // 启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.loadUrl("file:///android_asset/www/index.html");
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
        initPermission();

        //开启后台定位服务
        if (mLocationIntent == null) {
            mLocationIntent = new Intent(this, TrackingService.class);
        }
        getApplicationContext().startService(mLocationIntent);

        initHandler();
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(APP_ID);
    }

    @Override
    public void postSuccessMsg(String msg, String request_tag) {

        Log.d("LM", "postSuccessMsg msg: " + msg);
        Log.d("LM", "postSuccessMsg request_tag: " + request_tag);

        if (msg.equals("error")){
            //下载安装包失败
            if (request_tag.equals(DestFileName)){
                Message message=mHandler.obtainMessage();
                message.arg1=-1;
                message.sendToTarget();
                return;
            }

            if (!NetworkUtils.isNetworkAvailable(getMContext())){
                NetworkUtils.setContactNetDialog(getApplication());
                return;
            }
        }else if (request_tag.equals(TAG_CHECKVERSION)){
            JSONObject jo= JSON.parseObject(msg);

            String status = jo.getString("status");

            String downloadUrl = null;
            String versionNo = null;
            if(status.equals("1")) {

                JSONObject dict = jo.getJSONObject("data");
                downloadUrl = dict.getString("downloadUrl");
                versionNo = dict.getString("versionNo");
                Log.d("LM", "postSuccessMsg: downloadUrl" + downloadUrl);
                Log.d("LM", "postSuccessMsg: versionNo" + versionNo);
            }

            String downUrl=downloadUrl;
            String version=versionNo;
//            downUrl = "http://oms.kaidongyuan.com:8888/download/saas-wms.apk";
            if (version!=null && downUrl!=null) {
                try {
                    String currentVersion = getMContext().getPackageManager().getPackageInfo(getMContext().getPackageName(), 0).versionName;
                    MLog.w( "version:"+version+"\tcurrentVersion:"+currentVersion);

                    int compareVersion = Tools.compareVersion(version, currentVersion);
                    if (compareVersion == 1) {

                        createUpdateDialog(currentVersion, version, downUrl);
                        minefragment.isupdate=true;
                    } else {

                        checkGpsState();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.d("LM","NameNotFoundException" + e.getMessage());
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * 版本更新对话框
     * @param currentVersion 当前版本versionName
     * @param version 最新版本versionName
     * @param downUrl 最新版本安装包下载url
     */
    public void createUpdateDialog(String currentVersion, String version, final String downUrl) {
        if (mUpdataVersionDialog == null) {

            Log.d("LM", "---------------0");
            AlertDialog.Builder builder = new AlertDialog.Builder(getMContext());
            Log.d("LM", "---------------1.1");
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

                    Log.d("LM", "---------------3");
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

    /**
     * 判断 GPS是否开启
     */
    private void checkGpsState() {
        LocationManager alm= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if( !alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER )&&!alm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ) {
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

    private void showSnackbar(String strSnackbar, View.OnClickListener listener,int duration) {

        pmSnackbar = Snackbar.make(findViewById(R.id.acitvity_mainAcitivity),strSnackbar,duration);
        View v= pmSnackbar.getView();
        v.setBackgroundColor(getResources().getColor(R.color.details_text));
        final TextView tv_snackbar= (TextView) v.findViewById(R.id.snackbar_text);
        tv_snackbar.setGravity(Gravity.CENTER);
        tv_snackbar.setTextColor(getResources().getColor(R.color.white));
        pmSnackbar.setAction("设置",listener).show();
    }

    // js调用java
    private class JsInterface extends Activity {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.CallAndroidOrIOS.callAndroid(name)，便会触发此方法。
        @JavascriptInterface
        public void callAndroid(String exceName, final String inputName) {

            Log.d("LM", "执行:" + exceName + "    " + "输入框:" + inputName);
            LoginActivity.this.inputName = inputName;

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

                SharedPreferences readLatLng = mContext.getSharedPreferences("w_UserInfo", MODE_MULTI_PROCESS);
                final String u = readLatLng.getString("UserName", "");
                final String p = readLatLng.getString("Password", "");

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

                SharedPreferences readLatLng = mContext.getSharedPreferences("CurrLatLng", MODE_MULTI_PROCESS);
                final String address = readLatLng.getString("w_address", "");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String url = "javascript:SetCurrAddress('" + address + "')";
                        LoginActivity.mWebView.loadUrl(url);
                        Log.d("LM", url);
                    }
                });
            }  else if (exceName.equals("导航")) {

                Log.d("LM", "导航");

                new Thread() {

                    public void run() {

                        Tools.ToNavigation(inputName, mContext, appName);
                    }
                }.start();
            }
        }

        @JavascriptInterface
        public void callAndroid(String exceName, String u, String p) {

            Log.d("LM", "执行:" + exceName + "    " + "用户名:" + u + "    " + "密码:" + p);

            // 当前时间
            String curDate = Tools.getCurrDate();

            if (exceName.equals("记住帐号密码")) {

                if (u != null && p != null) {

                    SharedPreferences crearPre = mContext.getSharedPreferences("w_UserInfo", MODE_PRIVATE);
                    crearPre.edit().putString("UserName", u).commit();
                    crearPre.edit().putString("Password", p).commit();
                    crearPre.edit().putString("Set_User_Pass_Time", curDate).commit();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initPermission() {

        try {

            if (Build.VERSION.SDK_INT>=23){
                if (MPermissionsUtil.checkAndRequestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        ,RequestPermission_STATUS_CODE0)){
                    checkVersion();
                }
            }else {

                checkVersion();
            }
        }catch (Exception e) {

            Log.d("LM", "initPermission: " + e.getMessage());
        }
    }

    public void checkVersion() {
        Log.d("LM", "checkVersion: ");
        Map<String, String> params = new HashMap<>();
        params.put("strLicense", "");
        mClient.sendRequest("http://zwlttest.3322.org:8081/tmsApp/queryAppVersion.do?params={tenantCode:%27KDY%27}", params, TAG_CHECKVERSION);
    }
    private void initHandler() {
        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                int percent=message.arg1;
                if (percent==100){
                    createNotifaction(percent);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),DestFileName);
                    if (!file.exists()) {
                        Toast.makeText(getMContext(), "升级包不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        Uri uri = Uri.fromFile(file);
                        String type = "application/vnd.android.package-archive";//.apk 的 mime 名
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, type);
                        startActivity(intent);
                    }
                    mNotificationManager.cancel(0);
                } else if (percent==-1) {
                    Toast.makeText(LoginActivity.this, "下载失败，请查看是否授权App存储权限", Toast.LENGTH_LONG).show();
                    mNotificationManager.cancel(0);
                } else{
                    createNotifaction(percent);
                }
                return false;
            }
        });
    }

    /**
     * 创建下载进度的 notification
     * @param percent 下载进度
     */
    private void createNotifaction(int percent){
        //自定义 Notification 布局
        if (mUpdataNotification==null) {
            mUpdataNotification = new Notification();
            mUpdataNotification.icon =R.mipmap.ic_launcher;
            mUpdataNotification.tickerText =getResources().getText(R.string.app_name);
        }
        if (remoteView==null) {
            remoteView = new RemoteViews(getMContext().getPackageName(), R.layout.dialog_download);
        }
        remoteView.setTextViewText(R.id.textView_dialog_download, percent+"%");
        remoteView.setProgressBar(R.id.progressBar_dialog_download, 100, percent, false);
        mUpdataNotification.contentView = remoteView;
        mNotificationManager.notify(0, mUpdataNotification);
    }

    @Override
    public void setProgressBarLoading(int progress) {
        // super.setProgressBarLoading(progress);
        //改为更新通知栏进度条
        Message message=mHandler.obtainMessage();
        message.arg1=progress;
        message.sendToTarget();
    }

    // android 7.0以上手机存储授权后回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {

            takeCameraM();
        }
    }


    private void openImageChooserActivity() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("拍照/相册");
        builder.setPositiveButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto();
            }
        });
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
            String imageFileName = "JPEG_"+timeStamp+"_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile;
            try {
                imageFile = File.createTempFile(imageFileName,".jpg",storageDir);
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
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    private void takeCameraM() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if(takePhotoIntent.resolveActivity(getPackageManager())!=null){//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            File imageFile = createImageFile();//创建用来保存照片的文件
            if(imageFile!=null){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this,FILE_PROVIDER_AUTHORITY,imageFile);
                }else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, FILE_CAMERA_RESULT_CODE);//打开相机
            }
        }
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName,".jpg",storageDir);
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
     * 两次点击返回按钮小于两秒退出程序到桌面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("LM", "onKeyDown: " + KeyEvent.KEYCODE_BACK);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goHomeActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回桌面
     */
    private void goHomeActivity(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("LM", "onActivityResult: ----");

        super.onActivityResult(requestCode, resultCode, data);
        if (null == uploadMessage && null == uploadMessageAboveL) return;
        if (resultCode != RESULT_OK) {//同上所说需要回调onReceiveValue方法防止下次无法响应js方法

            Log.d("LM", "onActivityResult: ----1");
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

        Log.d("LM", "onActivityResult: ----2");
        Uri result = null;
        if (requestCode == FILE_CAMERA_RESULT_CODE) {
            Log.d("LM", "onActivityResult: ----3" + data);
            if (null != data && null != data.getData())

                Log.d("LM", "onActivityResult: ----3.2.0" + result);
            Log.d("LM", "onActivityResult: ----3.2.1" + cameraFielPath);
            if (result == null && hasFile(cameraFielPath)) {
                Log.d("LM", "onActivityResult: ----3.2");
                result = Uri.fromFile(new File(cameraFielPath));
            }
            if (uploadMessageAboveL != null) {
                Log.d("LM", "onActivityResult: ----3.3");
                Log.d("LM", "onActivityResult: ----3.3.0" + result);
                uploadMessageAboveL.onReceiveValue(new Uri[]{result});
                Log.d("LM", "onActivityResult: ----3.3.1");
                uploadMessageAboveL = null;
                Log.d("LM", "onActivityResult: ----3.3.2");
            } else if (uploadMessage != null) {

                Log.d("LM", "onActivityResult: ----3.4");
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {

            Log.d("LM", "onActivityResult: ----4");
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
}
