package com.kaidongyuan.app.kdydriver.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.LocationClient;
import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.utils.nomalutils.AnimationUtil;
import com.kaidongyuan.app.basemodule.utils.nomalutils.BitmapUtil;
import com.kaidongyuan.app.basemodule.utils.nomalutils.FileUtil;
import com.kaidongyuan.app.basemodule.widget.AutographView;
import com.kaidongyuan.app.basemodule.widget.MLog;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.app.AppManager;
import com.kaidongyuan.app.kdydriver.bean.order.LocationContineTime;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;
import com.kaidongyuan.app.kdydriver.ui.base.BaseFragmentActivity;
import com.kaidongyuan.app.kdydriver.ui.fragment.IndexFragment;
import com.kaidongyuan.app.kdydriver.ui.fragment.OrderListFragment;
import com.kaidongyuan.app.kdydriver.utils.LocationFileHelper;
import com.kaidongyuan.app.kdydriver.utils.SharedPreferencesUtils;
import com.kaidongyuan.app.kdydriver.utils.baidumapUtils.DataUtil;
import com.kaidongyuan.app.kdydriver.utils.baidumapUtils.UploadCacheLocationUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2016/10/19.
 */
public class OrderPayActivity extends BaseFragmentActivity implements AsyncHttpCallback,View.OnClickListener {
    private SlidingTitleView mslidingTitleView;
    private String orderIDX;
    private String payStyle=Constants.URL.DelivePay;//用订单交付和批量交付的URL作为区别标志
    private final String Tag_Pay = "Tag_Pay";//交付订单Tag
    private final String Tag_pays="Tag_Pays";//批量交付Tag
    private ImageView imageView1,imageView2;
    private Button button1,button2,cancelbutton,submitbutton,autographbutton;
    private AutographView mautographView;
    private OrderAsyncHttpClient mClient;
    /** 保存图片和签名的文件夹 */
    private final String mCacheFileName = "kaidongyuan";
    /**　签名保存的绝对路径　*/
    private  String autographFilePath;
    /** 照片1保存的文件名 */
    private final String mPictureFileName = "orderImage.jpg";
    /**　照片1调用系统相机是的请求码　*/
    private final int SystemCapture = 10;
    /**　照片保存的绝对路径　*/
    private String mPictureFilePath;
    /** 照片2保存的文件名 */
    private final String mPictureFileName2 = "orderImage2.jpg";
    /**照片2调用系统相机是的请求码2　*/
    private final int SystemCapture2 = 11;
    /**　照片2保存的绝对路径　*/
    private String mPictureFilePath2;
    /** 储存添加照片是的临时文件名 */
    private String mTempPictureFileName;
    /** 储存添加照片时的临时请求码 */
    private int mTempRequestCode;
    /** 储存添加照片时的临时文件路径 */
    private String mTempPictureFilePath;
    /** 储存添加照片时的临时Bitmap */
    private Bitmap mTempbitmap;
    /**　签名和照片文件宽度 单位（px）　*/
    private final int mBitmapWidth = 400;
    /** 签名和照片的质量 */
    private final int mPictureQuity = 60;
    private EditText et_orderBackNum;//回单单号
    private Snackbar choicsSnackbar;
    private boolean  onlyphotograph=false;
    /*** 20180122 交付时提交当前定位坐标**/
    private RequestQueue mRequestQueue;
    private final static String Tag_Upload_Position = "Tag_Upload_Position";
    private double currentLocationLat,currntLocationLng;
    private String currentLocationAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderpay);
        initview();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initview() {
        mslidingTitleView= (SlidingTitleView) findViewById(R.id.orderpay_titelview);
        mslidingTitleView.setText("订单交付");
        mslidingTitleView.setMode(SlidingTitleView.MODE_BACK);
        Intent intent=getIntent();
        if (intent.hasExtra("order_id")){
            orderIDX=getIntent().getStringExtra("order_id");
            payStyle=Constants.URL.DelivePay;
        }else if (intent.hasExtra("orderids")){
            orderIDX=getIntent().getStringExtra("orderids");
            payStyle=Constants.URL.DriverListPay;
        }
        if (intent.hasExtra("onlyphotograph")){
            onlyphotograph=getIntent().getBooleanExtra("onlyphotograph",false);
        }
        imageView1= (ImageView) findViewById(R.id.imageView_picture);
        imageView1.setOnClickListener(this);
        imageView2= (ImageView) findViewById(R.id.imageView_picture2);
        imageView2.setOnClickListener(this);
        button1= (Button) findViewById(R.id.button_addPicture);
        button1.setOnClickListener(this);
        button2= (Button) findViewById(R.id.button_addPicture2);
        button2.setOnClickListener(this);
        cancelbutton= (Button) findViewById(R.id.button_cancel);
        cancelbutton.setOnClickListener(this);
        submitbutton= (Button) findViewById(R.id.button_submit);
        submitbutton.setOnClickListener(this);
        mClient=new OrderAsyncHttpClient(this,this);
        mautographView= (AutographView) findViewById(R.id.autographView_write);
        if (intent.hasExtra("order_currentLocation_address")){
            currentLocationAds = getIntent().getStringExtra("order_currentLocation_address");
            List<String> data=new ArrayList<>();
            data.add(currentLocationAds);
            mautographView.setData(data);
        }
        if (intent.hasExtra("order_currentLocation_lng")&&intent.hasExtra("order_currentLocation_lat")){
            currntLocationLng=intent.getDoubleExtra("order_currentLocation_lng",0);
            currentLocationLat=intent.getDoubleExtra("order_currentLocation_lat",0);
        }
        autographbutton= (Button) findViewById(R.id.button_againAutograph);
        autographbutton.setOnClickListener(this);
        et_orderBackNum= (EditText) findViewById(R.id.edit_orderBackNum);
    }

    /**
     * 20180122  交付时提交当前定位坐标
     */
    private void updatalocation(){
        if (currntLocationLng<=0&&currentLocationLat<=0){
            MLog.w("没有定位到正确的经纬度坐标，（"+currentLocationLat+","+currntLocationLng+"）");
            return;
        }
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this.getApplicationContext());

        }
        String url = Constants.URL.CurrentLocaltion;
        MLog.w("上传位置信息URL：" + url + "?" + "strUserIdx=" + SharedPreferencesUtils.getUserId() + "&cordinateX=" + currntLocationLng + "&cordinateY=" + currentLocationLat + "&strLicense=");
        StringRequest mStringRequest = new StringRequest(Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                com.alibaba.fastjson.JSONObject jo = JSON.parseObject(response);
                int type = Integer.parseInt(jo.getString("type"));
                if(type>=1) {
                    MLog.w("上传位置信息成功，response:" + response +"\t,locationtime:"+ DataUtil.getStringTime(System.currentTimeMillis()));
                    currntLocationLng=0;
                    currentLocationLat=0;
                }else{
                    MLog.w("上传位置信息失败，response" + response + "\t,time" + DataUtil.getStringTime(System.currentTimeMillis()));

                }
        }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MLog.w("上传位置信息失败，error" + "\t,time" + DataUtil.getStringTime(System.currentTimeMillis()));
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("strUserIdx", SharedPreferencesUtils.getUserId());
                params.put("cordinateX", currntLocationLng + "");
                params.put("cordinateY", currentLocationLat + "");
                params.put("address",currentLocationAds+"|Pay" );
                params.put("strLicense", "");
                params.put("date", DataUtil.getStringTime(System.currentTimeMillis()) + "");
                MLog.i("params:"+params.toString());
                return params;
            }
        };
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000, 1, 1.0f));  // 设置超时
        mStringRequest.setTag(Tag_Upload_Position);
        mRequestQueue.add(mStringRequest);
    }

    @Override
    protected void onDestroy() {
        try {
            if (mClient!=null){
                mClient.cancleRequest(Tag_Pay,Tag_pays);
                mClient=null;
            }
            if (mautographView!=null){
                mautographView.clear();
                mautographView=null;
            }
            if (mRequestQueue!=null){
                mRequestQueue.cancelAll(Tag_Upload_Position);
                mRequestQueue=null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void postSuccessMsg(String msg, String request_tag) {
        if (msg.equals("error")){
            if (request_tag.equals(Tag_Pay)||request_tag.equals(Tag_pays)){
                showToastMsg("订单提交失败:");
            }
            MLog.e("postSuccessMsg error");
            finish();
        }else if (request_tag.equals(Tag_Pay)){
            try {
                JSONObject jo= JSON.parseObject(msg);
                if (jo.getInteger("type")==1){
                    showToastMsg("订单交付成功");
                    OrderListFragment.isrefresh=true;
                    AppManager.getAppManager().finishActivity(OrderDetailActivity.class);
                    finish();
                }else {
                    showToastMsg("提交失败:"+msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (request_tag.equals(Tag_pays)){
            try {
                JSONObject jo=JSON.parseObject(msg);
                if (jo.getInteger("type")==1){
                    showToastMsg("批量交付成功");
                    OrderListFragment.isrefresh=true;
                    AppManager.getAppManager().finishActivity(OrderDetailActivity.class);
                    AppManager.getAppManager().finishActivity(OrderListChoiceActivity.class);
                    finish();
                }else {
                    showToastMsg("提交失败:"+msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void muploadCachlocation() {
        String mFileName=getApplicationContext().getFilesDir().getAbsolutePath();
        List<LocationContineTime> locationList = LocationFileHelper.readFromFile2(mFileName);
        String mUserId = SharedPreferencesUtils.getUserId();
        RequestQueue mRequestQueue=Volley.newRequestQueue(getMContext());
        UploadCacheLocationUtil.uploadCacheLocation(getMContext(), mRequestQueue, mFileName, mUserId, locationList);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView_picture:
                if (choicsSnackbar!=null&&choicsSnackbar.isShown()){
                    choicsSnackbar.dismiss();
                    return;
                }
                if (mPictureFilePath==null||mPictureFilePath.length()<=0){
                    addPicture(mPictureFileName, SystemCapture);
                }else {
                    Intent intent=new Intent(this,PhotoActivity.class);
                    intent.putExtra("picturePath",mPictureFilePath);
                    startActivity(intent);
                }
                break;
            case R.id.imageView_picture2:
                if (choicsSnackbar!=null&&choicsSnackbar.isShown()){
                    choicsSnackbar.dismiss();
                    return;
                }
                if (mPictureFilePath2==null||mPictureFilePath2.length()<=0){
                    addPicture(mPictureFileName2,SystemCapture2);
                }else {
                    Intent intent=new Intent(this,PhotoActivity.class);
                    intent.putExtra("picturePath",mPictureFilePath2);
                    startActivity(intent);
                }
                break;
            case R.id.button_addPicture:
                if (choicsSnackbar!=null&&choicsSnackbar.isShown()){
                    choicsSnackbar.dismiss();
                    return;
                }
                addPicture(mPictureFileName,SystemCapture);
                break;
            case R.id.button_addPicture2:
                if (choicsSnackbar!=null&&choicsSnackbar.isShown()){
                    choicsSnackbar.dismiss();
                    return;
                }
                addPicture(mPictureFileName2,SystemCapture2);
                break;
            case R.id.button_againAutograph:
                mautographView.clear();
                break;
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_submit:
                if (saveAutographInLocaFile()){
                    switch (payStyle){
                        case Constants.URL.DelivePay:updataOrderWithPicture();
                            break;
                        case Constants.URL.DriverListPay:
                            updataOrdersWithPicture();
                            break;
                        default:
                            showToastMsg("订单数据丢失，提交失败");
                            break;
                    }
                }
                break;
        }
    }

    private void updataOrderWithPicture() {
        if ((mPictureFilePath!=null&&mPictureFilePath.length()>0)
                &&(mPictureFilePath2!=null&&mPictureFilePath2.length()>0)
                &&(autographFilePath!=null&&autographFilePath.length()>0)){
            Bitmap pictureBitmap=BitmapUtil.resizeImage(mPictureFilePath,mBitmapWidth);
            Bitmap pictureBitmap2=BitmapUtil.resizeImage(mPictureFilePath2,mBitmapWidth);
            Bitmap autographBitmap=BitmapUtil.resizeImage(autographFilePath,mBitmapWidth);
            if (pictureBitmap!=null&&pictureBitmap2!=null&&autographBitmap!=null){
                try {
                    String strpicture=BitmapUtil.changeBitmapToString(pictureBitmap);
                    String strpicture2=BitmapUtil.changeBitmapToString(pictureBitmap2);
                    String strautograph=BitmapUtil.changeBitmapToString(autographBitmap);
                    String strorderBackNum=et_orderBackNum.getText().toString();
                    Map<String,String> params=new HashMap<>();
//                    params.put("strOrderIdx",orderIDX);
//                    params.put("strLicense","");
//                    params.put("PictureFile1",strpicture);
//                    params.put("PictureFile2",strpicture2);
//                    params.put("AutographFile",strautograph);
//                    params.put("strDeliveNo",strorderBackNum);
//                    mClient.sendRequest(Constants.URL.DelivePay,params,Tag_Pay);
//              20171106 修改为3.1以后版本统一用DriverListPay接口来进行交付订单
                    params.put("strOrdersIdx",orderIDX);
                    params.put("strLicense","");
                    params.put("PictureFile1",strpicture);
                    params.put("PictureFile2",strpicture2);
                    params.put("AutographFile",strautograph);
                    params.put("strDeliveNo",strorderBackNum);
                    mClient.sendRequest(Constants.URL.DriverListPay,params,Tag_Pay);
                }catch (Exception e){
                    showToastMsg("请正确填写回单单号~");
                    e.printStackTrace();
                }
            }else {
                showToastMsg("请将照片和签名上传完整!");
            }
        }else {
            showToastMsg("请将照片和签名上传完整!");
        }
    }
    private void updataOrdersWithPicture() {
        if ((mPictureFilePath!=null&&mPictureFilePath.length()>0)
                &&(mPictureFilePath2!=null&&mPictureFilePath2.length()>0)
                &&(autographFilePath!=null&&autographFilePath.length()>0)){
            Bitmap pictureBitmap=BitmapUtil.resizeImage(mPictureFilePath,mBitmapWidth);
            Bitmap pictureBitmap2=BitmapUtil.resizeImage(mPictureFilePath2,mBitmapWidth);
            Bitmap autographBitmap=BitmapUtil.resizeImage(autographFilePath,mBitmapWidth);
            if (pictureBitmap!=null&&pictureBitmap2!=null&&autographBitmap!=null){
                try {
                    String strpicture=BitmapUtil.changeBitmapToString(pictureBitmap);
                    String strpicture2=BitmapUtil.changeBitmapToString(pictureBitmap2);
                    String strautograph=BitmapUtil.changeBitmapToString(autographBitmap);
                    String strorderBackNum=et_orderBackNum.getText().toString();
                    Map<String,String> params=new HashMap<>();
                    params.put("strOrdersIdx",orderIDX);
                    params.put("strLicense","");
                    params.put("PictureFile1",strpicture);
                    params.put("PictureFile2",strpicture2);
                    params.put("AutographFile",strautograph);
                    params.put("strDeliveNo",strorderBackNum);
                    mClient.sendRequest(Constants.URL.DriverListPay,params,Tag_pays);
                }catch (Exception e){
                    showToastMsg("请正确填写回单单号~");
                    e.printStackTrace();
                }
            }else {
                showToastMsg("请将照片和签名上传完整!");
            }
        }else {
            showToastMsg("请将照片和签名上传完整!");
        }
    }
    private boolean saveAutographInLocaFile() {
        Bitmap bitmap=mautographView.getPaintBitmap();
        if (bitmap!=null){
            bitmap=BitmapUtil.resizeImage(bitmap,mBitmapWidth);
            autographFilePath=BitmapUtil.writeBimapToFile(bitmap,"autograph.jpg",mCacheFileName,mPictureQuity);
            if (autographFilePath==null||autographFilePath.length()<=0){
                showToastMsg("签字失败！",Toast.LENGTH_SHORT);
                return false;
            }else {
                return true;
            }
        }else {
            showToastMsg("签字失败！");
            return false;
        }
    }

    private void addPicture(String mPictureFileName, int systemCapture) {
        mTempPictureFileName=mPictureFileName;
        mTempRequestCode=systemCapture;
        showUpdataChoice();
    }

    private void showUpdataChoice() {
        // tv_snackbar.setPadding(50,50,50,50);
        if (onlyphotograph){
            choicsSnackbar = Snackbar.make(findViewById(R.id.activity_orderpay),"",Snackbar.LENGTH_INDEFINITE);
            View v= choicsSnackbar.getView();
            v.setBackgroundColor(getResources().getColor(R.color.details_text));
        }else {
            choicsSnackbar = Snackbar.make(findViewById(R.id.activity_orderpay),"相册选取",Snackbar.LENGTH_INDEFINITE);
            View v= choicsSnackbar.getView();
            v.setBackgroundColor(getResources().getColor(R.color.details_text));
            final TextView tv_snackbar= (TextView) v.findViewById(R.id.snackbar_text);
            tv_snackbar.setGravity(Gravity.CENTER);
            tv_snackbar.setTextColor(getResources().getColor(R.color.white));
            tv_snackbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //从相册获取照片上传
                    if (choicsSnackbar !=null) {
                        choicsSnackbar.dismiss();
                    }
                    mTempRequestCode *= 2;
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, mTempRequestCode);
                }
            });
        }

        choicsSnackbar.setAction("拍照上传", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File dirFile = FileUtil.getCacheDirFile(mCacheFileName);
                if (dirFile == null) {
                    showToastMsg("请先授权读写sd卡权限~");
                    return;
                }
                File pictureFile = new File(dirFile, mTempPictureFileName);
                mTempPictureFilePath = pictureFile.getAbsolutePath();
                Uri pictureUri = Uri.fromFile(pictureFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                startActivityForResult(intent, mTempRequestCode);
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case SystemCapture:
                    Bitmap bitmap0= BitmapUtil.resizeImage(mTempPictureFilePath,mBitmapWidth);
                    getPictureResultHandle(imageView1, button1, bitmap0, mPictureFileName);
                    updatalocation();
                    break;
                case SystemCapture*2:
                    Uri uri=data.getData();
                    Bitmap bitmap1=BitmapUtil.getBitmap(getApplicationContext(),uri,mBitmapWidth);
                    getPictureResultHandle(imageView1,button1,bitmap1,mPictureFileName);
                    updatalocation();
                    break;
                case SystemCapture2:
                    Bitmap bitmap2=BitmapUtil.resizeImage(mTempPictureFilePath,mBitmapWidth);
                    getPictureResultHandle(imageView2,button2,bitmap2,mPictureFileName2);
                    break;
                case SystemCapture2*2:
                    Uri uri1=data.getData();
                    Bitmap bitmap3=BitmapUtil.getBitmap(getApplicationContext(),uri1,mBitmapWidth);
                    getPictureResultHandle(imageView2,button2,bitmap3,mPictureFileName2);
                    break;
                default:
                    showToastMsg("图片传输失败，请重新取图", Toast.LENGTH_LONG);
                    break;
            }

        }

    }



    @Override
    public void onBackPressed() {
        if (choicsSnackbar!=null&&choicsSnackbar.isShown()){
            choicsSnackbar.dismiss();
        }else {
            super.onBackPressed();
        }
    }

    private void getPictureResultHandle(ImageView imageView1, Button button1, Bitmap bitmap, String mPictureFileName) {
        if (bitmap!=null){
            imageView1.setImageBitmap(bitmap);
            ScaleAnimation sa=AnimationUtil.createScaleAnimation(0.3f,1f,1000,true);
            imageView1.startAnimation(sa);
            button1.setText("重新上传");
            String picturePath=BitmapUtil.writeBimapToFile(bitmap,mPictureFileName,mCacheFileName,mPictureQuity);
            if (mTempRequestCode==SystemCapture||mTempRequestCode==SystemCapture*2){
                mPictureFilePath=picturePath;
            }else if (mTempRequestCode==SystemCapture2||mTempRequestCode==SystemCapture2*2){
                mPictureFilePath2=picturePath;
            }
        }
    }
}
