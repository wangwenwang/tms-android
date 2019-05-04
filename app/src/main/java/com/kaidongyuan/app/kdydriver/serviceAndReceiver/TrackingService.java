package com.kaidongyuan.app.kdydriver.serviceAndReceiver;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import com.kaidongyuan.app.basemodule.utils.nomalutils.DateUtil;
import com.kaidongyuan.app.basemodule.utils.nomalutils.StringUtils;
import com.kaidongyuan.app.basemodule.widget.MLog;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.app.AppContext;
import com.kaidongyuan.app.kdydriver.bean.Tools;
import com.kaidongyuan.app.kdydriver.bean.order.LocationContineTime;
import com.kaidongyuan.app.kdydriver.bean.order.User;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.ui.activity.LoginActivity;
import com.kaidongyuan.app.kdydriver.utils.LocationFileHelper;
import com.kaidongyuan.app.kdydriver.utils.SharedPreferencesUtils;
import com.kaidongyuan.app.kdydriver.utils.baidumapUtils.DataUtil;
import com.kaidongyuan.app.kdydriver.utils.baidumapUtils.UploadCacheLocationUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kaidongyuan.app.kdydriver.constants.Constants.SP_WhoStartTrackingService_Value_Default;

/**
 * 后台定位服务
 *
 * @author ke
 */
public class TrackingService extends Service {

    private static final String TAG = "upLoad position ------ ";
    private final static String prefName = "configs";// shareprence中的配置名
    private Context mContext = this;
    //  public static final String Action_Tracking_Service = "com.kaidongyuan.app.kdydriver.service.TrackingService";
    public LocationClient mLocationClient;// 百度定位客户端
    public MyLocationListener myListener;// 百度定位监听
    private String tempcoor = "bd09ll";// 百度地图的编码模式
    private double mLat = 0, mLng = 0;
    //高精度模式
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    //仅GPS定位模式
//    private LocationMode tempMode = LocationMode.Device_Sensors;
    boolean isLoop = true;
    private static final double Min_Distance = 100;  // 上传时判断的最小距离
    //测试 2016.07.18
    //    private  static final double Min_Distance=-1;
    private RequestQueue mRequestQueue;
    private final static String Tag_Upload_Position = "Tag_Upload_Position";
    private boolean needClose = false;
    private String mUserId;
    private static StopReceiver mReceiver;
    private String mFileName;
    //上传数据线程用到
    private long mNetNotConnetTime = 0;
    private android.os.Handler mHandler;
    private int mScanSpanTime = Constants.scanSpan;
    private Thread mThread;
    private boolean mLocationThreadRunning;
    //2016.3.25
    private String locationaddress;//被定义为百度定位返回code码字段
    private boolean againBoolean = true;
    // 上传通道
    private boolean uploadChannel = true;
    // private PowerManager.WakeLock wakeLock = null;

    @Override
    public IBinder onBind(Intent intent) {
        MLog.i("\tTrackinSerice.onstart");
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        MLog.i("\tTrackinSerice.onstart");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("time", "TrackingService.onCreate");
        locationaddress = "BDCode";
        mContext = this;
        mLocationClient = new LocationClient(this);
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        initLocationClient();
        showNotification();
        try {
            mUserId = AppContext.getInstance().getUser().getIDX();
            if (mUserId == null || mUserId.isEmpty()) {
                mUserId = SharedPreferencesUtils.getUserId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mFileName = getApplicationContext().getFilesDir().getAbsolutePath()
                + File.separator + "Location" + File.separator + mUserId + ".log";


        //开启子线程，定时上传数据
        mLocationThreadRunning = true;
        if (mThread == null) {
            mThread = new LocationThread();
            mThread.setName("TrackingServiceThread.stander");
            mThread.setPriority(Thread.MAX_PRIORITY);
        }
        alamManagersend();
        mHandler = new android.os.Handler(new android.os.Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    setContactNetDialog();
                }
                if (msg.what == 2) {
                    mNetDialog.cancel();
                }
                return false;
            }
        });
        mThread.start();
    }

    private void alamManagersend() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        } else {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), TrackingService.class);
            intent.putExtra("AM", "alarmManager");
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            Long triggerAtime = System.currentTimeMillis() + 1000 * 60 * 3;
            //针对不同版本的AndroidSDK,采用不同方法的闹钟唤醒定位服务
            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtime, pendingIntent);
            }

        }
    }

    /**
     * 定时上传点位置信息的线程类
     */
    int times = 0;
    private class LocationThread extends Thread {
        @Override
        public void run() {
            while (mLocationThreadRunning) {
                try {
                    startLocate();
//                    stopLocate();
                    if (!isNetworkAvailable()) {
                        mNetNotConnetTime += mScanSpanTime;
                        //超过 2 分钟为联网就提示用户，弹出系统 Dialog
                        if (mNetNotConnetTime >= 28 * 60 * 1000) {
                            mHandler.sendEmptyMessage(1);
                            mNetNotConnetTime = 0;
                        }
                    } else {
                        mNetNotConnetTime = 0;
                        if (mNetDialog != null) {
                            mHandler.sendEmptyMessage(2);
                        }
                    }

                    times ++;
                    Log.d("LM", "定时器执行次数：" + times);
                    Thread.currentThread().sleep(1000 * 60 * 6);
//                    Thread.currentThread().sleep(mScanSpanTime);

                    if (mLocationClient != null && mLocationClient.isStarted()) {
                        mLocationClient.requestLocation();
                    }

//                    stopLocate();
//                    mLocationClient = null;
//                    mLocationClient = new LocationClient(TrackingService.this);
//                    myListener = new MyLocationListener();
//                    mLocationClient.registerLocationListener(myListener);
//                    initLocationClient();
//                    mLocationClient.start();

                    againBoolean = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /*
     * 服务开始启动
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        final String display = Tools.GetDisplayStatus(mContext);
//        final String charging = Tools.GetChargingStatus(mContext);
//
//        new Thread() {
//            public void run() {
//
//                String re = Tools.timingTracking1("13726027405", "中国广东省深圳市宝安区振兴路29号", "114.045309",
//                        "22.626074", "161", display, charging, "ANDROID", mContext);
//            }
//        }.start();

        // acquireWakeLock();
        try {
            if (intent.hasExtra("AM")) {
                MLog.e("AlarmManager唤醒定位服务：\tTrackingService.onStartCommand()");
                //  SharedPreferencesUtils.WriteSharedPreferences("TestDatabase",DataUtil.getStringTime(System.currentTimeMillis()),"AlarmManager唤醒定位服务");
                alamManagersend();
                if (mUserId == null || mFileName == null) {
                    mUserId = SharedPreferencesUtils.getUserId();
                    mFileName = getApplicationContext().getFilesDir().getAbsolutePath()
                            + File.separator + "Location" + File.separator + mUserId + ".log";
                }
//            if (mThread!=null){
//                mThread.notify();
//            }
                startLocate();
            } else {
                MLog.e("TrackingService.onStartCommand()");
                mUserId = SharedPreferencesUtils.getUserId();
                mFileName = getApplicationContext().getFilesDir().getAbsolutePath()
                        + File.separator + "Location" + File.separator + mUserId + ".log";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }


    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }


    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }


    /**
     * 定位SDK监听函数
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {

            String addressACode = getlocationReturnCode(location.getLocType(), location.getAddrStr());
            double distance = DistanceUtil.getDistance(new LatLng(mLat, mLng), new LatLng(location.getLatitude(), location.getLongitude()));
            Log.d("LM", "进入定位函数: " + location.getLongitude() + "   " + location.getLatitude() + "   " +  addressACode + "距离：" + distance);
//            Toast.makeText(getApplicationContext(),"进入定位函数: " + location.getLongitude() + "   " + location.getLatitude() + "   " +  addressACode + "距离：" + distance, Toast.LENGTH_LONG).show();

            SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_W_UserInfo_Key, MODE_MULTI_PROCESS);
            final String u = sp.getString("UserName", "");

            final String a = location.getAddrStr();
            final String lo = location.getLongitude() + "";
            final String la = location.getLatitude() + "";
            sp.edit().putString("CurrAddrStr", a).apply();
            sp.edit().putString("CurrLongitude", lo).apply();
            sp.edit().putString("CurrLatitude", la).apply();

            // Toast.makeText(getApplicationContext(),"\t"+location.getLocType(),Toast.LENGTH_LONG).show();
            MLog.i("TrackingService MyLocationListener:\t" + location.getLocType());
            if (location == null) {

                if (needClose) {
                    closeService();
                }
                //定位返回空值时，重新定位
                if (againBoolean) {
                    try {
                        Thread.sleep(30 * 1000);
                        againBoolean = false;
                        int r = mLocationClient.requestLocation();
                        Log.d("LM", "定位返回空值时，重新定位:" + r);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            // 判断定位是否失败应该依据error code的值更加可靠, 上传坐标信息
            if (!isLocateAvailable(location.getLocType())) {
                if (needClose) {
                    closeService();
                }
                // 20161103 陈翔 调试
                if (againBoolean) {
                    try {
                        Thread.sleep(30 * 1000);
                        againBoolean = false;
                        int j = mLocationClient.requestLocation();
                        Log.d("LM", "定位返回空值时，重新定位:" + j);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                Log.d("LM", "定位返回错误码两次，放弃本次定位");
                return;
            }

            if (mLat == 0 || mLng == 0) {

                Log.d("LM", "首次定位");

                mLat = location.getLatitude();
                mLng = location.getLongitude();

                new Thread() {
                    public void run() {

                        if(uploadChannel == true) {

                            // 关闭上传位置通道
                            Log.d("LM", "关闭上传位置通道");
                            uploadChannel = false;
                            String result = uploadLocation(mLat, mLng, location.getTime(), location.getAddrStr(), u, location.getLocType() + "");
                            Log.d("LM", result);

                            try {  Thread.sleep(1000 * 10); } catch (InterruptedException e) { e.printStackTrace(); }
                            Log.d("LM", "打开上传位置通道");
                            uploadChannel = true;
                        }else {

                            Log.d("LM", "碰壁啦");
                        }

                    }
                }.start();
                //   uploadLocation(mLat,mLng,DateUtil.formateWithTime(DateUtil.getDateTime(System.currentTimeMillis()-1000*60*60*24*100)));
                //  Toast.makeText(mContext,"首次定位，mLat:"+mLat+"\tmLng;"+mLng+location.getTime(),Toast.LENGTH_LONG).show();
            } else {

                Log.d("LM", "非首次定位");
                final double lat = location.getLatitude();
                final double lng = location.getLongitude();
                new Thread() {

                    public void run() {

                        String result = uploadLocation(lat, lng, location.getTime(), location.getAddrStr(), u, location.getLocType() + "");
                        Log.d("LM", result);
                    }
                }.start();
                //   uploadLocation(mLat,mLng,DateUtil.formateWithTime(DateUtil.getDateTime(System.currentTimeMillis()-1000*60*24*100)));
                //Toast.makeText(mContext,"持续定位，mLat:"+mLat+"\tmLng;"+mLng+location.getTime(),Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * @auther: Tom
     * created at 2016/3/25 14:53
     * 调试是否成功定位的方法
     */
    private String getlocationReturnCode(int i, String address) {
        switch (i) {
            case -1:
                locationaddress = "定位返回值为空|BD0";
                break;
            case 1:
                locationaddress = "重定失败service没有启动|BD1";
                break;
            case 2:
                locationaddress = "重定失败无监听函数|BD2";
                break;
            case 6:
                locationaddress = "重定失败请求太频繁|BD6";
                break;
            case 7:
                locationaddress = "请求百度定位连接失败|BD9";
                break;
            default:
                locationaddress = address + "|BD" + i;
                break;
        }
        return locationaddress;
    }

    private void closeService() {
        stopLocate();
        stopSelf();
        stopForeground(true);
        if (mReceiver != null) {
            try {
                unregisterReceiver(mReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String uploadLocation(final double lat, final double lng, final String locationtime,
                                 final String vehicleLocation, final String cellphone, final String code) {
        stopLocate();
        /**
         * 在网络不可用时 缓存到本地文件中
         */
        if (!isNetworkAvailable()) {
            saveCacheLocation(lat, lng, locationtime);
            MLog.w("TrackingService.uploadLocation.无网络，lat:" + lat + "lng:" + lng + "缓存到本地");
            return "没有网络，打回";
        }

//        /**
//         * 上传缓存数据
//         */
//        List<LocationContineTime> locationList = LocationFileHelper.readFromFile2(mFileName);
//        if (locationList != null && locationList.size() > 0) {
//            //2016-03-15 修改，将缓存位置信息以一定数量上传
//            saveCacheLocation(lat, lng, locationtime);
//            locationList = LocationFileHelper.readFromFile2(mFileName);
//            MLog.w("有缓存数据，将点位置信息保存到缓存文件张再上传TrackingService.locationList.size():" + locationList.size());
//            // 王文望 后期 缓存
////            UploadCacheLocationUtil.uploadCacheLocation(mContext, mRequestQueue, mFileName, mUserId, locationList);
//            return "有缓存点，待上传";
//        } else {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);

        }


        Log.d("LM", "上传定位点，网络请求");
        SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_W_UserInfo_Key, MODE_MULTI_PROCESS);

        // 当前时间
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String endDate2 = sdf2.format(new Date());

        // 服务器设定上传时间
        int serverUploadTime = sp.getInt(Constants.SP_SubmitLngSpan_Key, Constants.SP_SubmitLngSpan_Value_Default);

        // 上一次成功上传坐标的时间
        String startDate2 = sp.getString(Constants.SP_LastUploadLngSuccessDate_Key, endDate2);
        long spanTime = Tools.getTimeExpend(startDate2, endDate2) + 4000;
        Log.d("LM", "已间隔：" + spanTime + "，目标：" + serverUploadTime);

        // 离上一次上传成功的坐标
        Log.d("LM", "上次上传成功坐标：" + mLng + "，" + mLat);

        double distance = DistanceUtil.getDistance(new LatLng(mLat, mLng), new LatLng(lat, lng));

        final String LoginActiveFirstStart = sp.getString(Constants.SP_LoginActiveFirstStart_Key, "");

        if (cellphone.equals("")) {

            return "Tools检查不通过，未读取用户信息，打回";
        }

        if ((spanTime < serverUploadTime) && LoginActiveFirstStart.equals(Constants.SP_LoginActiveFirstStart_Value_NO)) {

            return "Tools检查不通过，提交时间间隔不达标，打回";
        }

        if(LoginActiveFirstStart.equals(Constants.SP_LoginActiveFirstStart_Value_YES)) {

            Log.d("LM", "Tools检查通过：第一次打开Activity拥有特权");
        }

        if (vehicleLocation == null) {

            return "Tools检查不通过，地址不能为null，打回";
        }

        if (vehicleLocation.equals("")) {

            return "Tools检查不通过，地址不能为空，打回";
        }

//        if ((lng + "").equals(lastLon) || (lat + "").equals(lastLat)) {
//
//            return "Tools检查不通过，坐标与上次成功上传相同，打回";
//        }

        // 两定位点距离超过1000公里视为异常定位放弃, distance<1000*50
        if((distance < Min_Distance || distance > 1000*500) && !LoginActiveFirstStart.equals(Constants.SP_LoginActiveFirstStart_Value_YES)) {

            return "Tools检查不通过，距离: " + distance + "，打回";
        }
        Log.d("LM", "距离: " + distance);

        Log.d("LM", "Tools检查通过：");

        String WhoStartTrackingService = sp.getString(Constants.SP_WhoStartTrackingService_Key, SP_WhoStartTrackingService_Value_Default);
        Log.d("LM", "WhoStartTrackingService: " + WhoStartTrackingService);
        if(!WhoStartTrackingService.equals("")) {
            WhoStartTrackingService = "|" + WhoStartTrackingService;
        }

        String os =  Build.VERSION.RELEASE + "|" + android.os.Build.MODEL + "|" + StringUtils.getVersionName(mContext);

        String display = Tools.GetDisplayStatus(mContext);
        String charging = Tools.GetChargingStatus(mContext);

        String params1 =
                "{" +
                        "\"cellphone\":\"" + cellphone + "\"," +
                        "\"userName\":\"" + "" + "\"," +
                        "\"vehicleLocation\":\"" + vehicleLocation + "\"," +
                        "\"lon\":\"" + lng + "\"," +
                        "\"lat\":\"" + lat + "\"," +
                        "\"uuid\":\"" + "android" + "\"," +
                        "\"code\":\"" + code + WhoStartTrackingService + "\"," +
                        "\"brightscreen\":\"" + display + "\"," +
                        "\"charging\":\"" + charging + "\"," +
                        "\"os\":\"" + os + "\"" +
                        "}";

        Log.d("LM", "params1: " + params1);

        Map<String, String> params = new HashMap<>();
        params.put("params", params1);
        byte[] data = getRequestData(params, "utf-8").toString().getBytes();//获得请求体

        try {

            URL url = new URL(Constants.URL.SAAS_API_BASE + "timingTracking.do");

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(5000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                Log.d("LM", "上传位置成功");

                InputStream inptStream = httpURLConnection.getInputStream();

                // 记录最后一次成功上传位置时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String startDate = sdf.format(new Date());
                sp.edit().putString(Constants.SP_LastUploadLngSuccessDate_Key, startDate).apply();
                Log.d("LM", "记录最后一次成功上传位置时间");

                // 记录距离最近的成功上传坐标，用于相同坐标不上传功能
                mLat = lat;
                mLng = lng;
                Log.d("LM", "记录距离最近的成功上传坐标");

                sp.edit().putString(Constants.SP_LoginActiveFirstStart_Key, Constants.SP_LoginActiveFirstStart_Value_NO).apply();
                Log.d("LM", "标为不是第一次打开Activty");

                if (needClose) {
                    closeService();
                }

                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {

            //e.printStackTrace();

            MLog.w("上传位置信息失败，将点位置信息保存到缓存文件，error" + "\t,time" + DataUtil.getStringTime(System.currentTimeMillis()));
            saveCacheLocation(lat, lng, locationtime);
            /**
             * 之所以在error中postMsg 是为了在activity中取消listView的刷新状态
             */
            if (needClose) {
                closeService();
            }
            e.printStackTrace();

            return "err: " + e.getMessage();
        }
//        }
        return "上传位置函数执行完毕（不能确定已经成功上传）";
    }

    /**
     * 用于传递给服务端是排序用的id
     */
    private int saveCacheLocationId = 0;

    private void saveCacheLocation(double lat, double lng, String locationtime) {
        LocationContineTime location = new LocationContineTime();
        location.id = saveCacheLocationId + "";
        saveCacheLocationId++;
        if (mUserId == null) {
            mUserId = SharedPreferencesUtils.getUserId();
        }
        location.userIdx = (mUserId);
        location.ADDRESS = locationaddress;
        location.CORDINATEX = lat;
        location.CORDINATEY = lng;
        if (locationtime != null) {
            location.TIME = locationtime;
        } else {
            location.TIME = DataUtil.getStringTime(System.currentTimeMillis());
        }
        MLog.w("locationaddress" + locationaddress);
        locationaddress = "BDCode";
        if (mFileName == null) {
            mFileName = getApplicationContext().getFilesDir().getAbsolutePath()
                    + File.separator + "Location" + File.separator + mUserId + ".log";
        }
        File file = new File(mFileName);
        MLog.w("缓存文件位置.filePath:" + file.getAbsolutePath());
        if (!file.exists()) {
            try {
                boolean makeFile = file.createNewFile();
                MLog.w("TrackingService.saceCacheLocation.本地无缓存文件，创建缓存文件：" + makeFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!LocationFileHelper.saveInFile(location, mFileName)) {
            //2016.08.30 离线存储点返回失败则排序id回退回去
            saveCacheLocationId--;
        }
    }

    /**
     * 用于显示Notification 防止该服务被系统回收
     */
    @SuppressWarnings("deprecation")
//    public void showNotification() {
//        Notification notification = new Notification(R.mipmap.ic_launcher, getText(R.string.app_name), System.currentTimeMillis());
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_NO_CREATE);
//       notification.setLatestEventInfo(this, "定位服务后台运行中", "", pendingIntent);
//
//        notification.flags = Notification.FLAG_ONGOING_EVENT;
//        notification.flags |= Notification.FLAG_NO_CLEAR;
//        notification.flags |= Notification.FLAG_HIGH_PRIORITY;
//        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
//        startForeground(1, notification);
//    }
    public void showNotification() {
        //   NotificationManager manager= (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getText(R.string.app_name));
        builder.setContentText("正运输物流订单中，请保持App定位服务开启");
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;//标识正在运行的事件
        notification.flags |= Notification.FLAG_NO_CLEAR;//防止通知被点击清除
        notification.flags |= Notification.FLAG_HIGH_PRIORITY;//设置为高优先级的通知
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;//表示正在运行的服务
        //   notification.defaults=Notification.DEFAULT_SOUND;//设置通知响铃
        //  notification.defaults|=Notification.DEFAULT_VIBRATE;//设置通知震动（配合相关授权）
        startForeground(R.string.app_name, notification);//以app_name的id为标识来启动和管理通知
        //  manager.notify(R.string.app_name,notification);//以app_name的id为标识来启动和管理通知
        MLog.i("*****showNotification**********");
    }

    /**
     * 初始化定位客户端
     */
    public void initLocationClient() {
        LocationClientOption option = new LocationClientOption();
        option.setProdName(mContext.getPackageName());
        MLog.w("ProdName:" + mContext.getPackageName());
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        option.setLocationMode(tempMode);// 设置定位模式
        option.setScanSpan(mScanSpanTime);//设置上传位置时间间隔
        //  option.setScanSpan(1*60*1000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setTimeOut(10 * 1000); // 网络定位的超时时间
        mLocationClient.setLocOption(option);
    }

    /**
     * 判断是否会定位失败 ，errorCode是百度定位返回的错误代码
     *
     * @param errorCode
     * @return
     */
    public boolean isLocateAvailable(int errorCode) {
        //2016.08.30 陈翔 注销了65 ： 定位缓存的结果；68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
        //  return (errorCode == 161 ||errorCode == 61||errorCode==66||errorCode==65||errorCode==68);
        return (errorCode == 161 || errorCode == 61 || errorCode == 66);
    }

    /**
     * 开始定位
     */
    public void startLocate() {
        //  2016.10.24 注释掉导致一次定位请求三次的现象
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
            MLog.i("mLocationThread.sleep:" + mScanSpanTime);
        } else if (mLocationClient != null) {
            mLocationClient.requestLocation();
        } else {
            mLocationClient = new LocationClient(this);
            myListener = new MyLocationListener();
            mLocationClient.registerLocationListener(myListener);
            initLocationClient();
            mLocationClient.start();
        }
//2016.11.19 放开测试锁屏后定位不持续问题
//        if (mLocationClient != null && !mLocationClient.isStarted()) {
//            mLocationClient.start();
//        }
    }

    /**
     * 停止定位
     */
    public void stopLocate() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    @Override
    public void onDestroy() {
        isLoop = false;
        super.onDestroy();
        stopSelf();
        stopForeground(true);
        stopLocate();
        unregisterReceiver(mReceiver);
        // releaseWakeLock();
    }


    private class StopReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            needClose = true;
            mLocationClient.start();
        }
    }
//    public  static  class LocationReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            MLog.e("LocationReceiver.onReceive 广播定位");
//            AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            Intent intent1=new Intent(context,LocationReceiver.class);
//            intent.setAction("com.kaidongyuan.app.kdydriver.locationReceiver");
//            PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//            Long triggerAtime= System.currentTimeMillis()+ 1000 * 60 * 1;
//            Long interval= 1000 * 60 * 2L;
//            //针对不同版本的AndroidSDK,采用不同方法的闹钟唤醒定位广播
//            if (Build.VERSION.SDK_INT>=23){
//                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerAtime, pendingIntent);
//            }else if (Build.VERSION.SDK_INT>=19){
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerAtime,pendingIntent);
//            }else {
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtime, interval, pendingIntent);
//            }
//        }
//    }

    /**
     * 检测当前手机是否联网
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    private Dialog mNetDialog;

    /**
     * 显示 Dialog 提示用户连接网络
     */
    private void setContactNetDialog() {
        MLog.w("TrackingService.setContactNetDialog");
        if (mNetDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
            builder.setTitle(getText(R.string.app_name) + "提示：\n点击确定开启网络服务");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    mNetDialog.cancel();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mNetDialog.cancel();
                }
            });
            mNetDialog = builder.create();
            mNetDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        }
        mNetDialog.show();
    }
}













